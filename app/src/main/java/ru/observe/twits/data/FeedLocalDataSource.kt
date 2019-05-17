package ru.observe.twits.data

import io.realm.Realm
import io.realm.RealmList

import ru.observe.twits.models.FeedR
import ru.observe.twits.models.ItemFeedR
import ru.observe.twits.uimodels.Feed
import ru.observe.twits.uimodels.ItemFeed

class FeedLocalDataSource {

    interface OnLocalDataReady {
        suspend fun onLocalDataReady(data: Feed)
    }

    suspend fun getFeed(onLocalDataReady: OnLocalDataReady) {
        var newInstance: Feed? = null
        Realm.getDefaultInstance().executeTransaction { realm ->
            val data = realm.where(FeedR::class.java).findFirst() ?: return@executeTransaction
            newInstance =
                Feed(data.items.mapTo(
                    ArrayList<ItemFeed>(), { item ->
                        ItemFeed(
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

    fun saveData(data: Feed) {
        val newInstance =
            FeedR(data.items.mapTo(
                RealmList<ItemFeedR>(), { item ->
                    ItemFeedR(
                        item.title,
                        item.link,
                        item.thumbnail,
                        item.description,
                        item.guid
                    )
                }
            ))
        Realm.getDefaultInstance().executeTransaction { realm ->
            realm.where(FeedR::class.java).findAll().forEach { obj ->
                obj.deleteFromRealm()
            }
            realm.copyToRealm(newInstance)
        }
    }

}