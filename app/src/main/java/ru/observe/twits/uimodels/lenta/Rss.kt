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

class Item(
    val description: String,
    val enclosure: Enclosure?,
    guid: String,
    link: String,
    val pubDate: String,
    val title: String
) {
    val link = link
        get() = if (!field.startsWith("https://m.lenta.ru"))
            field.replace("https://lenta", "https://m.lenta") else field
    val guid = guid
        get() = if (!field.startsWith("https://m.lenta.ru"))
            field.replace("https://lenta", "https://m.lenta") else field
}

data class Enclosure(
    val url: String
)
