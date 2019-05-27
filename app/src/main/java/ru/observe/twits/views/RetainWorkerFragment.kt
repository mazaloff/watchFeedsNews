package ru.observe.twits.views

import android.os.Bundle
import android.support.v4.app.Fragment

import ru.observe.twits.viewmodels.ChannelsViewModel
import ru.observe.twits.viewmodels.NewsFeedViewModel

class RetainWorkerFragment : Fragment() {

    var channelsViewModel: ChannelsViewModel? = null
    var newsFeedViewModel: NewsFeedViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
    }

}