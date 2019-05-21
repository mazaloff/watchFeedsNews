package ru.observe.twits.data

import io.realm.Realm
import io.realm.RealmList

import ru.observe.twits.models.NewsFeedR
import ru.observe.twits.models.ItemNewsFeedR
import ru.observe.twits.uimodels.NewsFeed
import ru.observe.twits.uimodels.ItemNewsFeed

class FeedLocalDataSource {

    interface OnLocalDataReady {
        suspend fun onLocalDataReady(data: NewsFeed)
    }

    suspend fun getFeed(onLocalDataReady: OnLocalDataReady) {
        var newInstance: NewsFeed? = null
        Realm.getDefaultInstance().executeTransaction { realm ->
            val data = realm.where(NewsFeedR::class.java).findFirst() ?: return@executeTransaction
            newInstance =
                NewsFeed(data.items.mapTo(
                    mutableListOf<ItemNewsFeed>(), { item ->
                        ItemNewsFeed(
                            item.title,
                            item.link,
                            item.thumbnail,
                            item.description,
                            item.guid
                        )
                    }
                ))
        }
        newInstance?.let { onLocalDataReady.onLocalDataReady(it) }
    }

    fun saveData(data: NewsFeed) {
        val newInstance =
            NewsFeedR(data.items.mapTo(
                RealmList<ItemNewsFeedR>(), { item ->
                    ItemNewsFeedR(
                        item.title,
                        item.link,
                        item.thumbnail,
                        item.description,
                        item.guid
                    )
                }
            ))
        Realm.getDefaultInstance().executeTransaction { realm ->
            realm.where(NewsFeedR::class.java).findAll().forEach { obj ->
                obj.deleteFromRealm()
            }
            realm.copyToRealm(newInstance)
        }
    }

}