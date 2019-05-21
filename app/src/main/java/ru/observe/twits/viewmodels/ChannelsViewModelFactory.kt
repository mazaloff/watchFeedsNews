package ru.observe.twits.viewmodels

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import ru.observe.twits.data.FeedRepository

@Suppress("UNCHECKED_CAST")
class ChannelsViewModelFactory(private val repository: FeedRepository)
    : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ChannelsViewModel::class.java)) {
            return ChannelsViewModel(repository) as T
        }

        throw IllegalArgumentException("Unknown ViewModel Class")
    }

}