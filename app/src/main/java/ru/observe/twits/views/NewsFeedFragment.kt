package ru.observe.twits.views

import android.arch.lifecycle.Observer
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup

import ru.observe.twits.BuildConfig
import ru.observe.twits.R
import ru.observe.twits.data.Resource
import ru.observe.twits.databinding.AcNewsFeedBinding
import ru.observe.twits.uimodels.ItemChannel
import ru.observe.twits.uimodels.ItemNewsFeed
import ru.observe.twits.viewmodels.NewsFeedViewAdapter
import ru.observe.twits.viewmodels.NewsFeedViewModel


class NewsFeedFragment : Fragment(), NewsFeedViewAdapter.OnItemClickListener {

    private lateinit var newsFeedViewModel: NewsFeedViewModel

    private lateinit var binding: AcNewsFeedBinding

    private lateinit var itemChannel: ItemChannel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        setHasOptionsMenu(true);

        binding = DataBindingUtil.inflate<AcNewsFeedBinding>(
            inflater,
            R.layout.ac_news_feed, container, false
        )

        arguments?.apply {
            getSerializable(ItemChannel::class.java.simpleName)?.let {
                itemChannel = it as ItemChannel
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
            newsFeedViewModel.loadItemChannel(itemChannel)
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
        newsFeedViewModel.invalidatedState.observe(this,
            Observer {
                Log.d(BuildConfig.TAG, "invalidated State example")
            }
        )
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.menu_news_feeds_refresh -> {
                newsFeedViewModel.loadItemChannel(itemChannel)
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

