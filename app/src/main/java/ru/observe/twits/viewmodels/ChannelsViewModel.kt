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
import ru.observe.twits.uimodels.ItemChannel
import ru.observe.twits.uimodels.Channel
import ru.observe.twits.uimodels.TypeChannel

class ChannelsViewModel(private var feedRepository: FeedRepository) : ViewModel(), CoroutineScope {

    private var job = Job()
    override var coroutineContext = job + Dispatchers.IO

    val resourceData = MutableLiveData<Resource<List<ItemChannel>>>()

    val isLoading = NonNullObservableField(false)

    fun loadLinks(typeChannel: TypeChannel) {

        val channel = Channel(typeChannel)
        val resultLinks = mutableListOf<ItemChannel>()

        Log.d(BuildConfig.TAG, "Load News Channel")

        isLoading.set(true)
        resourceData.value = Resource(Resource.Status.LOADING, channel.items, null)

        if (!job.isActive) {
            job = Job()
            coroutineContext = job + Dispatchers.IO
        }
        for (item in channel.items) {
            launch {
                try {
                    feedRepository.getFeed(item.type.toString(), item.link,
                        object : FeedRepository.OnReadyCallback {
                            override suspend fun onDataReady(data: NewsFeed) {
                                item.newsFeed = data
                            }
                        })
                    withContext(Dispatchers.Main) {
                        resultLinks += item
                        isLoading.set(false)
                        resourceData.value = Resource(Resource.Status.COMPLETED, channel.items, null)
                    }
                } catch (e: Exception) {
                    withContext(Dispatchers.Main) {
                        resultLinks += item
                        isLoading.set(false)
                        e.printStackTrace()
                        resourceData.value = Resource(Resource.Status.COMPLETED, channel.items, e)
                    }
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