package ru.observe.twits.data

import android.arch.lifecycle.MutableLiveData
import android.arch.paging.DataSource

import ru.observe.twits.uimodels.ItemNewsFeed


class FeedDataFactory(
    private val strType: String,
    private val url: String,
    private val feedRepository: FeedRepository
) : DataSource.Factory<Int, ItemNewsFeed>() {

    internal val mutableLiveData = MutableLiveData<FeedDataSource>()

    override fun create(): DataSource<Int, ItemNewsFeed> {
        val feedDataSource = FeedDataSource(strType, url, feedRepository)
        mutableLiveData.postValue(feedDataSource)
        return feedDataSource
    }
}