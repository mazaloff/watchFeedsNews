package ru.observe.twits.uimodels.washingtonpost

data class WashingtonPostRss(
    val rss: Rss
)

data class Rss(
    val channel: Channel
)

data class Channel(
    val description: String,
    val item: List<Item>,
    val language: String,
    val link: List<Link>,
    val title: String,
    val updatePeriod: String
)

data class Link(
    val content: String,
    val href: String,
    val rel: String,
    val type: String
)

data class Item(
    val description: String,
    val guid: String,
    val link: String,
    val pubDate: String,
    val thumbnail: Thumbnail?,
    val title: String
)

data class Thumbnail(
    val url: String
)