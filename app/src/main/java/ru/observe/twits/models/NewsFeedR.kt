package ru.observe.twits.models

import io.realm.RealmList
import io.realm.RealmObject

open class NewsFeedR(
    var items: RealmList<ItemNewsFeedR> = RealmList()
) : RealmObject()


