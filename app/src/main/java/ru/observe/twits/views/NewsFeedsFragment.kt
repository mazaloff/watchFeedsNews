package ru.observe.twits.views

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.os.Bundle
import android.databinding.DataBindingUtil
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import dagger.android.support.AndroidSupportInjection
import dagger.android.support.DaggerFragment

import ru.observe.twits.R
import ru.observe.twits.databinding.ActivityNewsFeedsBinding
import ru.observe.twits.uimodels.ItemFeed
import ru.observe.twits.viewmodels.FeedViewAdapter
import ru.observe.twits.viewmodels.FeedViewModel
import ru.observe.twits.viewmodels.FeedViewModelFactory
import javax.inject.Inject

class NewsFeedsFragment : DaggerFragment(), FeedViewAdapter.OnItemClickListener {

    @Inject lateinit var feedViewModelFactory: FeedViewModelFactory

    private lateinit var viewModel : FeedViewModel

    private val feedRecyclerViewAdapter = FeedViewAdapter(ArrayList<ItemFeed>(), this)

    private lateinit var bindingMain: ActivityNewsFeedsBinding
    private lateinit var mainActivity: NewsFeedsActivity

    private lateinit var linkNews: String
    private lateinit var typeNews: String

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        viewModel = ViewModelProviders.of(this, feedViewModelFactory).get(FeedViewModel::class.java)

        bindingMain = DataBindingUtil.inflate<ActivityNewsFeedsBinding>(inflater,
            R.layout.activity_news_feeds, container, false
        )

        mainActivity = (bindingMain.root.context as NewsFeedsActivity)

        arguments?.apply {
            getString("linkNews")?.let {
                linkNews = it
            }
            getString("typeNews")?.let {
                typeNews = it
            }
        }

        return bindingMain.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        bindingMain.viewModel = viewModel
        bindingMain.executePendingBindings()

        bindingMain.act1RecView.layoutManager = LinearLayoutManager(bindingMain.root.context)
        bindingMain.act1RecView.adapter = feedRecyclerViewAdapter

        if (savedInstanceState == null) {
            bindingMain.viewModel?.loadFeed(typeNews, linkNews)
        }

        bindingMain.viewModel?.resourceData?.observe(this,
            Observer { data ->
                if (data != null) {
                    feedRecyclerViewAdapter.replaceData(data.items)
                }
            }
        )

    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.e("finish", "finished fragment")
        bindingMain.viewModel?.cancel()
    }

    override fun onItemClick(itemFeed: ItemFeed) {
        mainActivity.showArticle(itemFeed.link)
        if (itemFeed.guid.isNotEmpty()) {
            mainActivity.playMusic(itemFeed.guid, itemFeed.link, itemFeed.title)
        }
    }

}

