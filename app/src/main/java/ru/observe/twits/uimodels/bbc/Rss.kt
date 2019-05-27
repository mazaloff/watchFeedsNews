package ru.observe.twits.uimodels.bbc

data class BbcRss(
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
    val guid: Guid,
    val link: String,
    val pubDate: String,
    val thumbnail: Thumbnail?,
    val title: String
)

data class Thumbnail(
    val url: String
)

data class Guid(
    val content: String
)