package ru.observe.twits.data

import ru.observe.twits.managers.NetManager
import ru.observe.twits.uimodels.NewsFeed
import ru.observe.twits.uimodels.TypeChannel
import javax.inject.Inject

class FeedRepository @Inject constructor(var netManager: NetManager) {

    private val remoteDataSource = FeedRemoteDataSource()
    private val localDataSource = FeedLocalDataSource()

    interface OnReadyCallback {
        suspend fun onDataReady(data: NewsFeed)
    }

    suspend fun getFeed(strType: String, url: String, onReadyCallback: OnReadyCallback) {
        netManager.isConnectedToInternet?.let {

            if (it) {
                remoteDataSource.getFeed(TypeChannel.valueOf(strType), url, object : FeedRemoteDataSource.OnRemoteDataReady {
                    override suspend fun onRemoteDataReady(data: NewsFeed) {
                        localDataSource.saveData(data)
                        onReadyCallback.onDataReady(data)
                    }
                })
            } else {
                localDataSource.getFeed(object : FeedLocalDataSource.OnLocalDataReady {
                    override suspend fun onLocalDataReady(data: NewsFeed) {
                        onReadyCallback.onDataReady(data)
                    }
                })
            }
        }
    }

}