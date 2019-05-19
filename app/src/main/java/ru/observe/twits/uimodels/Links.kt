package ru.observe.twits.uimodels

object Links {

    val items = mutableListOf<ItemLink>()

    init {
        items.add(ItemLink("Top Stories", "http://feeds.bbci.co.uk/news/rss.xml", TypeLink.BBC))
        items.add(ItemLink("World", "http://feeds.bbci.co.uk/news/world/rss.xml", TypeLink.BBC))
        items.add(ItemLink("UK", "http://feeds.bbci.co.uk/news/uk/rss.xml", TypeLink.BBC))
        items.add(ItemLink("Business", "http://feeds.bbci.co.uk/news/business/rss.xml", TypeLink.BBC))
        items.add(ItemLink("Politics", "http://feeds.bbci.co.uk/news/politics/rss.xml", TypeLink.BBC))
        items.add(ItemLink("Health", "http://feeds.bbci.co.uk/news/health/rss.xml", TypeLink.BBC))
        items.add(ItemLink("Education & Family", "http://feeds.bbci.co.uk/news/education/rss.xml", TypeLink.BBC))
        items.add(ItemLink("Science & Environment", "http://feeds.bbci.co.uk/news/science_and_environment/rss.xml", TypeLink.BBC))
        items.add(ItemLink("Technology", "http://feeds.bbci.co.uk/news/technology/rss.xml", TypeLink.BBC))
        items.add(ItemLink("Entertainment & Arts", "http://feeds.bbci.co.uk/news/entertainment_and_arts/rss.xml", TypeLink.BBC))

        items.add(ItemLink("All TWiT.tv Shows", "http://feeds.twit.tv/brickhouse.xml", TypeLink.TWiT))
        items.add(ItemLink("All About Android", "http://feeds.twit.tv/aaa.xml", TypeLink.TWiT))
        items.add(ItemLink("FLOSS Weekly", "http://feeds.twit.tv/floss.xml", TypeLink.TWiT))
        items.add(ItemLink("Ham Nation", "http://feeds.twit.tv/hn.xml", TypeLink.TWiT))
        items.add(ItemLink("Hands-On Tech", "http://feeds.twit.tv/hot.xml", TypeLink.TWiT))
        items.add(ItemLink("iOS Today", "http://feeds.twit.tv/ipad.xml", TypeLink.TWiT))
        items.add(ItemLink("MacBreak Weekly", "http://feeds.twit.tv/mbw.xml", TypeLink.TWiT))
        items.add(ItemLink("Security Now", "http://feeds.twit.tv/sn.xml", TypeLink.TWiT))
        items.add(ItemLink("The Tech Guy", "http://feeds.twit.tv/kfi.xml", TypeLink.TWiT))
        items.add(ItemLink("Tech News Weekly", "http://feeds.twit.tv/tnw.xml", TypeLink.TWiT))
        items.add(ItemLink("This Week in Computer Hardware", "http://feeds.twit.tv/twich.xml", TypeLink.TWiT))
        items.add(ItemLink("This Week in Enterprise Tech", "http://feeds.twit.tv/twiet.xml", TypeLink.TWiT))
        items.add(ItemLink("This Week in Google", "http://feeds.twit.tv/twig.xml", TypeLink.TWiT))
        items.add(ItemLink("This Week in Tech", "http://feeds.twit.tv/twit.xml", TypeLink.TWiT))
        items.add(ItemLink("TWiT Specials", "http://feeds.twit.tv/ces.xml", TypeLink.TWiT))
        items.add(ItemLink("Valley of Genius", "http://feeds.twit.tv/vog.xml", TypeLink.TWiT))
        items.add(ItemLink("Windows Weekly", "http://feeds.twit.tv/ww.xml", TypeLink.TWiT))
        items.add(ItemLink("TWiT Bits", "http://feeds.twit.tv/bits.xml", TypeLink.TWiT))
        items.add(ItemLink("Radio Leo", "http://feeds.twit.tv/leo.xml", TypeLink.TWiT))
    }

}