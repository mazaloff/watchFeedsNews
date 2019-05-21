package ru.observe.twits.models

import io.realm.RealmObject

open class ItemNewsFeedR(
    var title: String = "",
    var link: String = "",
    var thumbnail: String = "",
    var description: String = "",
    var guid: String = ""
) : RealmObject()