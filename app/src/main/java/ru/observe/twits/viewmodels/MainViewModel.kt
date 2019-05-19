package ru.observe.twits.viewmodels

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.util.Log
import kotlinx.coroutines.*
import ru.observe.twits.BuildConfig
import ru.observe.twits.data.FeedRepository
import ru.observe.twits.data.Resource
import ru.observe.twits.tools.NonNullObservableField
import ru.observe.twits.uimodels.Feed
import ru.observe.twits.uimodels.Links

class MainViewModel(private var feedRepository: FeedRepository) : ViewModel(), CoroutineScope {

    private var job = Job()
    override var coroutineContext = job + Dispatchers.IO

    val resourceData = MutableLiveData<Resource<Links>>()

    val isLoading = NonNullObservableField(false)

    fun loadLinks() {

        val links = Links

        Log.d(BuildConfig.TAG, "Load News Links")

        isLoading.set(true)
        resourceData.value = Resource(Resource.Status.LOADING, null, null)

        if (!job.isActive) {
            job = Job()
            coroutineContext = job + Dispatchers.IO
        }
        launch {
            try {
                for (item in links.items) {
                    feedRepository.getFeed(item.type.toString(), item.link,
                        object : FeedRepository.OnReadyCallback {
                            override suspend fun onDataReady(data: Feed) {item.feed = data}
                        })
                }
                withContext(Dispatchers.Main) {
                    isLoading.set(false)
                    resourceData.value = Resource(Resource.Status.COMPLETED, links, null)
                }
            } catch (e: Exception) {
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