package ru.observe.twits.uimodels

import ru.observe.twits.uimodels.bbc.BbcRss
import ru.observe.twits.uimodels.twit.TwitRss

class Feed constructor(val items: ArrayList<ItemFeed>) {

    constructor(from: BbcRss) : this(
        from.rss.channel.item.mapTo(
            ArrayList<ItemFeed>(), { item ->
                ItemFeed(
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
            ArrayList<ItemFeed>(), { item ->
                ItemFeed(
                    item.title[0].content,
                    item.link,
                    item.content.thumbnail.url,
                    item.description,
                    item.guid.content
                )
            }
        ))

}