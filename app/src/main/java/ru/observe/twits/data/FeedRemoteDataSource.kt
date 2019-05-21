package ru.observe.twits.data

import ru.observe.twits.servers.ServerRequests
import ru.observe.twits.uimodels.NewsFeed
import ru.observe.twits.uimodels.TypeChannel

class FeedRemoteDataSource {

    interface OnRemoteDataReady {
        suspend fun onRemoteDataReady(data : NewsFeed)
    }

    suspend fun getFeed(type: TypeChannel, url: String, onRemoteDataReady: OnRemoteDataReady) {
        val data = ServerRequests.createRequest(type, url)
        onRemoteDataReady.onRemoteDataReady(data)
    }

}