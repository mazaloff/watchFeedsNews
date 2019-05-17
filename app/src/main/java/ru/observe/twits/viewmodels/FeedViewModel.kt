package ru.observe.twits.viewmodels

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.util.Log
import kotlinx.coroutines.*

import ru.observe.twits.data.FeedRepository
import ru.observe.twits.tools.NonNullObservableField
import ru.observe.twits.uimodels.Feed

class FeedViewModel(private var feedRepository: FeedRepository) : ViewModel(), CoroutineScope {

    private var job = Job()
    override var coroutineContext = job + Dispatchers.IO

    val resourceData = MutableLiveData<Feed>()

    val isLoading = NonNullObservableField(false)

    fun loadFeed(strType: String, url: String) {
        Log.d("network", "load Feed")
        isLoading.set(true)
        if (!job.isActive) {
            job = Job()
            coroutineContext = job + Dispatchers.IO
        }
        launch {
            delay(100)
            feedRepository.getFeed(strType, url,
                object : FeedRepository.OnReadyCallback {
                    override suspend fun onDataReady(data: Feed) {
                        withContext(Dispatchers.Main) {
                            isLoading.set(false)
                            resourceData.value = data
                        }
                    }
                }
            )
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