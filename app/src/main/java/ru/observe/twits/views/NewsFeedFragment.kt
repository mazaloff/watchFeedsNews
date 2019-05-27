package ru.observe.twits.views

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.databinding.DataBindingUtil
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.*

import ru.observe.twits.R
import ru.observe.twits.databinding.AcNewsFeedBinding
import ru.observe.twits.uimodels.ItemNewsFeed
import ru.observe.twits.data.Resource
import ru.observe.twits.viewmodels.*


class NewsFeedFragment : Fragment(), NewsFeedViewAdapter.OnItemClickListener {

    private lateinit var newsFeedViewModel: NewsFeedViewModel

    private lateinit var binding: AcNewsFeedBinding

    private lateinit var linkNews: String
    private lateinit var typeNews: String

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        setHasOptionsMenu(true);

        binding = DataBindingUtil.inflate<AcNewsFeedBinding>(
            inflater,
            R.layout.ac_news_feed, container, false
        )

        arguments?.apply {
            getString("linkNews")?.let {
                linkNews = it
            }
            getString("typeNews")?.let {
                typeNews = it
            }
        }

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        newsFeedViewModel = (activity as NewsFeedActivity).newsFeedViewModel

        binding.viewModel = newsFeedViewModel
        binding.executePendingBindings()

        binding.newsFeedsRecView.layoutManager = LinearLayoutManager(binding.root.context)
        binding.newsFeedsRecView.adapter = NewsFeedViewAdapter(this)

        if (savedInstanceState == null) {
            newsFeedViewModel.loadNewsFeed(typeNews, linkNews)
        }

        observeNewsFeedModel()

    }

    private fun observeNewsFeedModel() {
        val adapter = (binding.newsFeedsRecView.adapter as NewsFeedViewAdapter)
        newsFeedViewModel.articleLiveData.observe(this,
            Observer { pagedList -> adapter.submitList(pagedList) }
        )
        newsFeedViewModel.networkState.observe(this,
            Observer { networkState ->
                val loading = networkState != null && networkState == Resource.Status.LOADING
                binding.loading.visibility = if (loading) View.VISIBLE else View.GONE
                adapter.setNetworkState(networkState)
            }
        )
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.menu_news_feeds_refresh -> {
                newsFeedViewModel.loadNewsFeed(typeNews, linkNews)
                observeNewsFeedModel()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onItemClick(itemNewsFeed: ItemNewsFeed) {
        val newsFeedActivity = (activity as NewsFeedActivity)
        newsFeedActivity.showArticle(itemNewsFeed.link)
        if (itemNewsFeed.guid.isNotEmpty()) {
            newsFeedActivity.playMusic(itemNewsFeed.guid, itemNewsFeed.link, itemNewsFeed.title)
        }
    }

}

