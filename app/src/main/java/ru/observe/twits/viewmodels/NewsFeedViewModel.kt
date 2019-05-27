package ru.observe.twits.viewmodels

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.Transformations
import android.arch.paging.PagedList
import android.arch.paging.LivePagedListBuilder
import android.arch.lifecycle.LiveData

import java.util.concurrent.Executors

import ru.observe.twits.data.Resource
import ru.observe.twits.uimodels.ItemNewsFeed
import ru.observe.twits.data.FeedRepository
import ru.observe.twits.data.FeedDataFactory
import java.util.concurrent.ExecutorService

class NewsFeedViewModel(var feedRepository: FeedRepository
): ViewModel()  {

    var networkState: LiveData<Resource.Status>
    var articleLiveData: LiveData<PagedList<ItemNewsFeed>>
    private var executor: ExecutorService = Executors.newFixedThreadPool(5)

    init {

        val feedDataFactory = FeedDataFactory("", "", feedRepository)
        networkState = Transformations.switchMap(
            feedDataFactory.mutableLiveData
        ) { dataSource -> dataSource.networkState }

        val pagedListConfig = PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setInitialLoadSizeHint(10)
            .setPageSize(10)
            .build()

        articleLiveData = LivePagedListBuilder(feedDataFactory, pagedListConfig)
            .setFetchExecutor(executor)
            .build()
    }

    fun loadNewsFeed(strType: String, url: String) {
        val feedDataFactory = FeedDataFactory(strType, url, feedRepository)
        networkState = Transformations.switchMap(
            feedDataFactory.mutableLiveData
        ) { dataSource -> dataSource.networkState }

        val pagedListConfig = PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setInitialLoadSizeHint(10)
            .setPageSize(10)
            .build()

        articleLiveData = LivePagedListBuilder(feedDataFactory, pagedListConfig)
            .setFetchExecutor(executor)
            .build()
    }

}