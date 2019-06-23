package ru.observe.twits.data

import android.arch.lifecycle.MutableLiveData
import android.arch.paging.DataSource

import ru.observe.twits.uimodels.ItemNewsFeed


class FeedDataFactory(
    strType: String,
    url: String,
    feedRepository: FeedRepository
) : DataSource.Factory<Int, ItemNewsFeed>(), DataSource.InvalidatedCallback {

    private val feedDataSource : FeedDataSource = FeedDataSource(strType, url, feedRepository)

    override fun onInvalidated() {
    }

    internal val mutableLiveData = MutableLiveData<FeedDataSource>()

    override fun create(): DataSource<Int, ItemNewsFeed> {
        mutableLiveData.postValue(feedDataSource)
        return feedDataSource
    }
}