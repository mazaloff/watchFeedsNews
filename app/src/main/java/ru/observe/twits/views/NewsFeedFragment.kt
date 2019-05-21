package ru.observe.twits.views

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.os.Bundle
import android.databinding.DataBindingUtil
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.*
import android.widget.Toast
import dagger.android.support.AndroidSupportInjection
import dagger.android.support.DaggerFragment
import ru.observe.twits.BuildConfig

import ru.observe.twits.R
import ru.observe.twits.data.Resource
import ru.observe.twits.databinding.AcNewsFeedBinding
import ru.observe.twits.uimodels.ItemNewsFeed
import ru.observe.twits.viewmodels.NewsFeedViewAdapter
import ru.observe.twits.viewmodels.NewsFeedViewModel
import ru.observe.twits.viewmodels.NewsFeedViewModelFactory
import javax.inject.Inject

class NewsFeedFragment : DaggerFragment(), NewsFeedViewAdapter.OnItemClickListener {

    @Inject
    lateinit var newsFeedViewModelFactory: NewsFeedViewModelFactory

    private lateinit var newsFeedViewModel: NewsFeedViewModel

    private val newsFeedViewAdapter = NewsFeedViewAdapter(listOf<ItemNewsFeed>(), this)

    private lateinit var bindingSelf: AcNewsFeedBinding
    private lateinit var newsFeedActivity: NewsFeedActivity

    private lateinit var linkNews: String
    private lateinit var typeNews: String

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        setHasOptionsMenu(true);

        newsFeedViewModel = ViewModelProviders.of(this, newsFeedViewModelFactory).get(NewsFeedViewModel::class.java)

        bindingSelf = DataBindingUtil.inflate<AcNewsFeedBinding>(
            inflater,
            R.layout.ac_news_feed, container, false
        )

        newsFeedActivity = (bindingSelf.root.context as NewsFeedActivity)

        arguments?.apply {
            getString("linkNews")?.let {
                linkNews = it
            }
            getString("typeNews")?.let {
                typeNews = it
            }
        }

        return bindingSelf.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        bindingSelf.viewModel = newsFeedViewModel
        bindingSelf.executePendingBindings()

        bindingSelf.act1RecView.layoutManager = LinearLayoutManager(bindingSelf.root.context)
        bindingSelf.act1RecView.adapter = newsFeedViewAdapter

        if (savedInstanceState == null) {
            bindingSelf.viewModel?.loadFeed(typeNews, linkNews)
        }

        bindingSelf.viewModel?.resourceData?.observe(this,
            Observer { resource ->
                if (resource != null) {
                    if (resource.status == Resource.Status.COMPLETED && resource.data != null) {
                        newsFeedViewAdapter.replaceData(resource.data.items)
                    } else if (resource.status == Resource.Status.COMPLETED) {
                        newsFeedViewAdapter.replaceData(listOf<ItemNewsFeed>())
                        var textException = when (resource.exception) {
                            null -> ""
                            else -> "\n" + resource.exception.message
                        }
                        if (textException.isNotEmpty()) {
                            Log.e(BuildConfig.TAG, textException)
                        }
                        if (!BuildConfig.DEBUG) {
                            textException = ""
                        }else if (resource.exception != null) {
                            resource.exception.printStackTrace();
                        }
                        Toast.makeText(
                            newsFeedActivity,
                            "${getString(R.string.exception_response_data)}. $textException", Toast.LENGTH_LONG
                        )
                            .show()
                    }
                }
            }
        )

    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.menu_news_feeds_refresh -> {
                bindingSelf.viewModel?.loadFeed(typeNews, linkNews)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroyView() {
        bindingSelf.viewModel?.cancel()
        super.onDestroyView()
    }

    override fun onItemClick(itemNewsFeed: ItemNewsFeed) {
        newsFeedActivity.showArticle(itemNewsFeed.link)
        if (itemNewsFeed.guid.isNotEmpty()) {
            newsFeedActivity.playMusic(itemNewsFeed.guid, itemNewsFeed.link, itemNewsFeed.title)
        }
    }

}

