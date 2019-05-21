package ru.observe.twits.viewmodels

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.util.Log
import kotlinx.coroutines.*
import ru.observe.twits.BuildConfig

import ru.observe.twits.data.FeedRepository
import ru.observe.twits.data.Resource
import ru.observe.twits.tools.NonNullObservableField
import ru.observe.twits.uimodels.NewsFeed

class NewsFeedViewModel(private var feedRepository: FeedRepository) : ViewModel(), CoroutineScope {

    private var job = Job()
    override var coroutineContext = job + Dispatchers.IO

    val resourceData = MutableLiveData<Resource<NewsFeed>>()

    val isLoading = NonNullObservableField(false)

    fun loadFeed(strType: String, url: String) {

        Log.d(BuildConfig.TAG, "Load News Feeds")

        isLoading.set(true)
        resourceData.value = Resource(Resource.Status.LOADING, null, null)

        if (!job.isActive) {
            job = Job()
            coroutineContext = job + Dispatchers.IO
        }

        launch {
            try {
                withTimeout(4000) {
                    try {
                        feedRepository.getFeed(strType, url,
                            object : FeedRepository.OnReadyCallback {
                                override suspend fun onDataReady(data: NewsFeed) {
                                    withContext(Dispatchers.Main) {
                                        isLoading.set(false)
                                        resourceData.value = Resource(Resource.Status.COMPLETED, data, null)
                                    }
                                }
                            }
                        )
                    } catch (e: Exception) {
                        withContext(Dispatchers.Main) {
                            isLoading.set(false)
                            resourceData.value = Resource(Resource.Status.COMPLETED, null, e)
                        }
                    }
                }
            } catch (e: TimeoutCancellationException) {
                withContext(Dispatchers.Main) {
                    isLoading.set(false)
                    resourceData.value = Resource(Resource.Status.COMPLETED, null, e)
                }
            }
        }
    }

    override fun onCleared() {
        cancel()
        super.onCleared()
    }

    fun cancel() {
        job.cancel()
    }

}