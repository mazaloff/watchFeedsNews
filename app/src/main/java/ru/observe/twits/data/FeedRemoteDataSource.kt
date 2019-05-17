package ru.observe.twits.data

import ru.observe.twits.servers.ServerRequests
import ru.observe.twits.uimodels.Feed
import ru.observe.twits.uimodels.TypeLink

class FeedRemoteDataSource {

    interface OnRemoteDataReady {
        suspend fun onRemoteDataReady(data : Feed)
    }

    suspend fun getFeed(type: TypeLink, url: String, onRemoteDataReady: OnRemoteDataReady) {
        val data = ServerRequests.createRequest(type, url)
        onRemoteDataReady.onRemoteDataReady(data)
    }

}