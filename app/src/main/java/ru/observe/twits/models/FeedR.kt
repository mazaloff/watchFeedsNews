package ru.observe.twits.models

import io.realm.RealmList
import io.realm.RealmObject

open class FeedR(
    var items: RealmList<ItemFeedR> = RealmList()
) : RealmObject()


