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
import ru.observe.twits.uimodels.ItemChannel
import java.util.concurrent.ExecutorService

class NewsFeedViewModel(var feedRepository: FeedRepository
): ViewModel()  {

    var networkState: LiveData<Resource.Status>
    var invalidatedState: LiveData<Boolean>

    var articleLiveData: LiveData<PagedList<ItemNewsFeed>>
    private var executor: ExecutorService = Executors.newFixedThreadPool(5)

    init {

        val feedDataFactory = FeedDataFactory("", "", feedRepository)
        networkState = Transformations.switchMap(
            feedDataFactory.mutableLiveData
        ) { dataSource -> dataSource.networkState }
        invalidatedState = Transformations.switchMap(
            feedDataFactory.mutableLiveData
        ) { dataSource -> dataSource.invalidatedState }

        val pagedListConfig = PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setInitialLoadSizeHint(10)
            .setPageSize(10)
            .build()

        articleLiveData = LivePagedListBuilder(feedDataFactory, pagedListConfig)
            .setFetchExecutor(executor)
            .build()
    }

    fun loadItemChannel(itemChannel: ItemChannel) {
        val feedDataFactory = FeedDataFactory(itemChannel.type.toString(), itemChannel.link, feedRepository)
        networkState = Transformations.switchMap(
            feedDataFactory.mutableLiveData
        ) { dataSource -> dataSource.networkState }
        invalidatedState = Transformations.switchMap(
            feedDataFactory.mutableLiveData
        ) { dataSource -> dataSource.invalidatedState }

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