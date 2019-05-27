package ru.observe.twits.viewmodels

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import ru.observe.twits.data.FeedRepository

@Suppress("UNCHECKED_CAST")
class NewsFeedViewModelFactory(private val feedRepository: FeedRepository)
    : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NewsFeedViewModel::class.java)) {
            return NewsFeedViewModel(feedRepository) as T
        }

        throw IllegalArgumentException("Unknown ViewModel Class")
    }

}