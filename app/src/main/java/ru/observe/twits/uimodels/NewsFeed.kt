package ru.observe.twits.uimodels

import ru.observe.twits.uimodels.bbc.BbcRss
import ru.observe.twits.uimodels.lenta.LentaRss
import ru.observe.twits.uimodels.twit.TwitRss

class NewsFeed constructor(val items: MutableList<ItemNewsFeed>) {

    constructor(from: BbcRss) : this(
        from.rss.channel.item.mapTo(
            mutableListOf<ItemNewsFeed>(), { item ->
                ItemNewsFeed(
                    item.title,
                    item.link,
                    item.thumbnail.url,
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
                    item.content.thumbnail.url,
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
                    item.enclosure.url,
                    item.description,
                    item.guid
                )
            }
        ))

}