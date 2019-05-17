package ru.observe.twits.uimodels.twit

data class TwitRss(
    val rss: Rss
)

data class Rss(
    val channel: Channel
)

data class Channel(
    val item: List<Item>
)

data class Item(
    val content: Content,
    val description: String,
    val guid: Guid,
    val link: String,
    val pubDate: String,
    val title: List<TitleX>
)

data class Content(
    val thumbnail: Thumbnail,
    val url: String
)

data class Thumbnail(
    val url: String
)

data class Guid(
    val content: String
)

data class TitleX(
    val content: String
)
