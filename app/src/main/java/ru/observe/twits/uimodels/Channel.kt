package ru.observe.twits.uimodels

class Channel(val type: TypeChannel) {

    val items = mutableListOf<ItemChannel>()

    init {

        if (type == TypeChannel.GAZETA){
            items.add(
                ItemChannel(
                    "Газета.Ru - Новости дня",
                    "https://www.gazeta.ru/export/rss/lenta.xml", type
                )
            )
            items.add(
                ItemChannel(
                    "Новости политики",
                    "https://www.gazeta.ru/export/rss/politics.xml", type
                )
            )
            items.add(
                ItemChannel(
                    "Деловые новости",
                    "https://www.gazeta.ru/export/rss/business.xml", type
                )
            )
            items.add(
                ItemChannel(
                    "Общество. Новости",
                    "https://www.gazeta.ru/export/rss/social.xml", type
                )
            )
            items.add(
                ItemChannel(
                    "Военные новости",
                    "https://www.gazeta.ru/export/rss/army.xml", type
                )
            )
            items.add(
                ItemChannel(
                    "Новости культуры",
                    "https://www.gazeta.ru/export/rss/culture.xml", type
                )
            )
            items.add(
                ItemChannel(
                    "Новости науки",
                    "https://www.gazeta.ru/export/rss/science.xml", type
                )
            )
            items.add(
                ItemChannel(
                    "Автоновости",
                    "https://www.gazeta.ru/export/rss/auto.xml", type
                )
            )
            items.add(
                ItemChannel(
                    "Стильные новости",
                    "https://www.gazeta.ru/export/rss/lifestyle.xml", type
                )
            )
            items.add(
                ItemChannel(
                    "Новости спорта",
                    "https://www.gazeta.ru/export/rss/sport.xml", type
                )
            )
        }

        if (type == TypeChannel.LENTA) {
            items.add(
                ItemChannel(
                    "Новости ТОП-7",
                    "https://lenta.ru/rss/top7", type
                )
            )
            items.add(
                ItemChannel(
                    "Новости Россия",
                    "https://lenta.ru/rss/articles/russia", type
                )
            )
            items.add(
                ItemChannel(
                    "Новости Мира",
                    "https://lenta.ru/rss/articles/world", type
                )
            )
            items.add(
                ItemChannel(
                    "Бывший СССР",
                    "https://lenta.ru/rss/articles/ussr", type
                )
            )
            items.add(
                ItemChannel(
                        "Экономика",
                    "https://lenta.ru/rss/articles/economics", type
                )
            )
            items.add(
                ItemChannel(
                    "Силовые структуры",
                    "https://lenta.ru/rss/articles/forces", type
                )
            )
            items.add(
                ItemChannel(
                    "Наука и техника",
                    "https://lenta.ru/rss/articles/science", type
                )
            )
            items.add(
                ItemChannel(
                    "Культура",
                    "https://lenta.ru/rss/articles/culture", type
                )
            )
            items.add(
                ItemChannel(
                    "Спорт",
                    "https://lenta.ru/rss/articles/sport", type
                )
            )
            items.add(
                ItemChannel(
                    "Интернет и СМИ",
                    "https://lenta.ru/rss/articles/forces", type
                )
            )
            items.add(
                ItemChannel(
                    "Стиль и ценности",
                    "https://lenta.ru/rss/articles/style", type
                )
            )
            items.add(
                ItemChannel(
                    "Из жизни",
                    "https://lenta.ru/rss/articles/life", type
                )
            )
            items.add(
                ItemChannel(
                    "Дом",
                    "https://lenta.ru/rss/articles/realty", type
                )
            )
        }
        if (type == TypeChannel.BBC) {
            items.add(
                ItemChannel(
                    "Top Stories",
                    "http://feeds.bbci.co.uk/news/rss.xml", type
                )
            )
            items.add(
                ItemChannel(
                    "World",
                    "http://feeds.bbci.co.uk/news/world/rss.xml", type
                )
            )
            items.add(
                ItemChannel(
                    "UK",
                    "http://feeds.bbci.co.uk/news/uk/rss.xml", type
                )
            )
            items.add(
                ItemChannel(
                    "Business",
                    "http://feeds.bbci.co.uk/news/business/rss.xml", type
                )
            )
            items.add(
                ItemChannel(
                    "Politics",
                    "http://feeds.bbci.co.uk/news/politics/rss.xml", type
                )
            )
            items.add(
                ItemChannel(
                    "Health",
                    "http://feeds.bbci.co.uk/news/health/rss.xml", type
                )
            )
            items.add(
                ItemChannel(
                    "Education & Family",
                    "http://feeds.bbci.co.uk/news/education/rss.xml", type
                )
            )
            items.add(
                ItemChannel(
                    "Science & Environment",
                    "http://feeds.bbci.co.uk/news/science_and_environment/rss.xml", type
                )
            )
            items.add(
                ItemChannel(
                    "Technology",
                    "http://feeds.bbci.co.uk/news/technology/rss.xml", type
                )
            )
            items.add(
                ItemChannel(
                    "Entertainment & Arts",
                    "http://feeds.bbci.co.uk/news/entertainment_and_arts/rss.xml", type
                )
            )
        }
        if (type == TypeChannel.TWiT) {
            items.add(
                ItemChannel(
                    "All TWiT.tv Shows",
                    "http://feeds.twit.tv/brickhouse.xml", type
                )
            )
            items.add(
                ItemChannel(
                    "All About Android",
                    "http://feeds.twit.tv/aaa.xml", type
                )
            )
            items.add(
                ItemChannel(
                    "FLOSS Weekly",
                    "http://feeds.twit.tv/floss.xml", type
                )
            )
            items.add(
                ItemChannel(
                    "Ham Nation",
                    "http://feeds.twit.tv/hn.xml", type
                )
            )
            items.add(
                ItemChannel(
                    "Hands-On Tech",
                    "http://feeds.twit.tv/hot.xml", type
                )
            )
            items.add(
                ItemChannel(
                    "iOS Today",
                    "http://feeds.twit.tv/ipad.xml", type
                )
            )
            items.add(
                ItemChannel(
                    "MacBreak Weekly",
                    "http://feeds.twit.tv/mbw.xml", type
                )
            )
            items.add(
                ItemChannel(
                    "Security Now",
                    "http://feeds.twit.tv/sn.xml", type
                )
            )
            items.add(
                ItemChannel(
                    "The Tech Guy",
                    "http://feeds.twit.tv/kfi.xml", type
                )
            )
            items.add(
                ItemChannel(
                    "Tech News Weekly",
                    "http://feeds.twit.tv/tnw.xml", type
                )
            )
            items.add(
                ItemChannel(
                    "This Week in Computer Hardware",
                    "http://feeds.twit.tv/twich.xml", type
                )
            )
            items.add(
                ItemChannel(
                    "This Week in Enterprise Tech",
                    "http://feeds.twit.tv/twiet.xml", type
                )
            )
            items.add(
                ItemChannel(
                    "This Week in Google",
                    "http://feeds.twit.tv/twig.xml", type
                )
            )
            items.add(
                ItemChannel(
                    "This Week in Tech",
                    "http://feeds.twit.tv/twit.xml", type
                )
            )
            items.add(
                ItemChannel(
                    "TWiT Specials",
                    "http://feeds.twit.tv/ces.xml", type
                )
            )
            items.add(
                ItemChannel(
                    "Valley of Genius",
                    "http://feeds.twit.tv/vog.xml", type
                )
            )
            items.add(
                ItemChannel(
                    "Windows Weekly",
                    "http://feeds.twit.tv/ww.xml", type
                )
            )
            items.add(
                ItemChannel(
                    "TWiT Bits",
                    "http://feeds.twit.tv/bits.xml", type
                )
            )
            items.add(
                ItemChannel(
                    "Radio Leo",
                    "http://feeds.twit.tv/leo.xml", type
                )
            )
        }
    }

}