package ru.observe.twits.uimodels.lenta

data class LentaRss(
    val rss: Rss
)

data class Rss(
    val channel: Channel
)

data class Channel(
    val item: List<Item>
)

data class Item(
    val description: String,
    val enclosure: Enclosure,
    val guid: String,
    val link: String,
    val pubDate: String,
    val title: String
)

data class Enclosure(
    val url: String
)
