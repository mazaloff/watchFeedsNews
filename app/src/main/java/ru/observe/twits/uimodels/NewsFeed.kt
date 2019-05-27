package ru.observe.twits.uimodels

import ru.observe.twits.uimodels.bbc.BbcRss
import ru.observe.twits.uimodels.gazeta.GazetaRss
import ru.observe.twits.uimodels.lenta.LentaRss
import ru.observe.twits.uimodels.twit.TwitRss
import kotlin.math.min

class NewsFeed constructor(val items: MutableList<ItemNewsFeed>) {

    fun loadRange(startPosition: Int, loadSize: Int): List<ItemNewsFeed> {
        val resultList = mutableListOf<ItemNewsFeed>()
            val sizeItems = items.size
            if (startPosition <= min(sizeItems,startPosition+loadSize)) {
                for (elem in startPosition until min(sizeItems,startPosition+loadSize)){
                    resultList.add(items[elem])
                }
            }
        return resultList
    }

    constructor(from: BbcRss) : this(
        from.rss.channel.item.mapTo(
            mutableListOf<ItemNewsFeed>(), { item ->
                ItemNewsFeed(
                    item.title,
                    item.link,
                    item.thumbnail?.url ?: "",
                    item.description,
                    item.guid.content
                )
            }
        ))

    constructor(from: TwitRss) : this(
        from.rss.channel.item.mapTo(
            mutableListOf<ItemNewsFeed>(), { item ->
                ItemNewsFeed(
                    item.title[0].content,
                    item.link,
                    item.content.thumbnail?.url ?: "",
                    item.description,
                    item.guid.content
                )
            }
        ))

    constructor(from: LentaRss) : this(
        from.rss.channel.item.mapTo(
            mutableListOf<ItemNewsFeed>(), { item ->
                ItemNewsFeed(
                    item.title,
                    item.link,
                    item.enclosure?.url ?: "",
                    item.description,
                    item.guid
                )
            }
        ))

    constructor(from: GazetaRss) : this(
        from.rss.channel.item.mapTo(
            mutableListOf<ItemNewsFeed>(), { item ->
                ItemNewsFeed(
                    item.title,
                    item.link,
                    item.enclosure?.url ?: "",
                    item.description,
                    item.guid
                )
            }
        ))

}