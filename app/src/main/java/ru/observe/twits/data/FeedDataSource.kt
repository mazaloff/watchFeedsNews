package ru.observe.twits.data

import android.arch.lifecycle.MutableLiveData
import android.arch.paging.PositionalDataSource
import android.util.Log
import kotlinx.coroutines.*
import ru.observe.twits.BuildConfig
import ru.observe.twits.uimodels.ItemNewsFeed
import ru.observe.twits.uimodels.NewsFeed

internal class FeedDataSource(
    private val strType: String,
    private val url: String,
    private val feedRepository: FeedRepository
) : PositionalDataSource<ItemNewsFeed>(),
    CoroutineScope {

    private var job = Job()
    override var coroutineContext = job + Dispatchers.IO

    private var currentFeed: NewsFeed? = null

    internal val initialLoading = MutableLiveData<Resource.Status>()
    internal val networkState = MutableLiveData<Resource.Status>()

    override fun loadInitial(params: LoadInitialParams, callback: LoadInitialCallback<ItemNewsFeed>) {

        if (strType.isEmpty() || url.isEmpty()) return
        Log.d(
            BuildConfig.TAG, "loadInitial LOADING, requestedStartPosition = " + params.requestedStartPosition +
                    ", requestedLoadSize = " + params.requestedLoadSize
        )
        if (!job.isActive) {
            job = Job()
            coroutineContext = job + Dispatchers.IO
        }

        initialLoading.postValue(Resource.Status.LOADING)
        networkState.postValue(Resource.Status.LOADING)

        launch {
            try {
                withTimeout(30000) {
                    delay(500)
                    try {
                        feedRepository.getFeed(strType, url,
                            object : FeedRepository.OnReadyCallback {
                                override suspend fun onDataReady(data: NewsFeed) {
                                    withContext(Dispatchers.Main) {
                                        currentFeed = data
                                        initialLoading.value = Resource.Status.COMPLETED
                                        networkState.value = Resource.Status.COMPLETED
                                        Log.d(
                                            BuildConfig.TAG, "loadInitial COMPLETED, requestedStartPosition = " + params.requestedStartPosition +
                                                    ", requestedLoadSize = " + params.requestedLoadSize
                                        )
                                        val totalCount = data.items.size
                                        val position = computeInitialLoadPosition(params, totalCount)
                                        val loadSize = computeInitialLoadSize(params, position, totalCount)
                                        val result = data.loadRange(position, loadSize)
                                        callback.onResult(result, position, totalCount)
                                    }
                                }
                            }
                        )
                    } catch (e: Exception) {
                        withContext(Dispatchers.Main) {
                            initialLoading.value = Resource.Status.FAILED
                            networkState.value = Resource.Status.FAILED
                            e.printStackTrace()
                        }
                    }
                }
            } catch (e: TimeoutCancellationException) {
                withContext(Dispatchers.Main) {
                    initialLoading.value = Resource.Status.FAILED
                    networkState.value = Resource.Status.FAILED
                    e.printStackTrace()
                }
            }
        }
    }

    override fun loadRange(params: LoadRangeParams, callback: LoadRangeCallback<ItemNewsFeed>) {

        if (strType.isEmpty() || url.isEmpty()) return

        networkState.postValue(Resource.Status.LOADING)
        Log.d(BuildConfig.TAG, "loadRange LOADING, startPosition = " + params.startPosition + ", loadSize = " + params.loadSize)

        if (currentFeed == null) {

            if (!job.isActive) {
                job = Job()
                coroutineContext = job + Dispatchers.IO
            }
            launch {
                delay(500)
                try {
                    withTimeout(30000) {
                        try {
                            feedRepository.getFeed(strType, url,
                                object : FeedRepository.OnReadyCallback {
                                    override suspend fun onDataReady(data: NewsFeed) {
                                        withContext(Dispatchers.Main) {
                                            currentFeed = data
                                            initialLoading.value = Resource.Status.COMPLETED
                                            networkState.value = Resource.Status.COMPLETED
                                            Log.d(BuildConfig.TAG, "loadRange LOADING, startPosition = " + params.startPosition + ", loadSize = " + params.loadSize)
                                            val result = data.loadRange(params.startPosition, params.loadSize)
                                            callback.onResult(result)
                                        }
                                    }
                                }
                            )
                        } catch (e: Exception) {
                            withContext(Dispatchers.Main) {
                                initialLoading.value = Resource.Status.FAILED
                                networkState.value = Resource.Status.FAILED
                                e.printStackTrace()
                            }
                        }
                    }
                } catch (e: TimeoutCancellationException) {
                    withContext(Dispatchers.Main) {
                        initialLoading.value = Resource.Status.FAILED
                        networkState.value = Resource.Status.FAILED
                        e.printStackTrace()
                    }
                }
            }

        } else {
            launch {
                delay(500)
                withContext(Dispatchers.Main) {
                    currentFeed?.let {
                        val result = it.loadRange(params.startPosition, params.loadSize)
                        callback.onResult(result)
                    }
                    initialLoading.value = Resource.Status.COMPLETED
                    networkState.value = Resource.Status.COMPLETED
                    Log.d(BuildConfig.TAG, "loadRange COMPLETED, startPosition = " + params.startPosition + ", loadSize = " + params.loadSize)
                }
            }
        }

    }

}