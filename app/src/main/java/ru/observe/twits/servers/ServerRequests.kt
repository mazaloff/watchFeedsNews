package ru.observe.twits.servers

import java.lang.RuntimeException

import com.google.gson.Gson
import fr.arnaudguyon.xmltojsonlib.XmlToJson
import ru.observe.twits.uimodels.bbc.BbcRss
import ru.observe.twits.uimodels.NewsFeed
import ru.observe.twits.uimodels.TypeChannel
import ru.observe.twits.uimodels.gazeta.GazetaRss
import ru.observe.twits.uimodels.lenta.LentaRss
import ru.observe.twits.uimodels.twit.TwitRss
import ru.observe.twits.uimodels.washingtonpost.WashingtonPostRss
import java.lang.Exception
import java.net.*
import java.nio.charset.Charset
import java.nio.charset.UnsupportedCharsetException


object ServerRequests {

    fun createRequest(type: TypeChannel, url: String): NewsFeed {
        val urlCon = URL(url).openConnection() as HttpURLConnection
        urlCon.requestMethod = "GET";
        urlCon.setRequestProperty("Content-Type", "text/xml;charset=utf-8");
        urlCon.setRequestProperty("Connection", "keep-alive")
        urlCon.setRequestProperty("Cache-Control", "no-cache")
        urlCon.setRequestProperty("Accept", "text/xml")
        urlCon.setRequestProperty("User-Agent", "AppWatchTwits")
        urlCon.useCaches = false
        var xml = ""
        try {
            urlCon.connect()
            if (urlCon.responseCode == HttpURLConnection.HTTP_OK) {
                val arrayBytes = urlCon.inputStream.readBytes()
                val firstLine = arrayBytes.inputStream().bufferedReader().readLines()[0]
                if (firstLine.startsWith("<!")) {
                    return createRequest(type, url)
                }
                val charset = parserCharset(firstLine)
                xml = String(arrayBytes, charset)
            } else {
                RuntimeException(urlCon.responseMessage)
            }
        }catch (e: Exception) {
            e.printStackTrace()
        }
        finally {
            urlCon.disconnect()
        }
        val json = convertXmlToJson(xml)
        return when(type) {
            TypeChannel.LENTA -> NewsFeed(Gson().fromJson(json, LentaRss::class.java))
            TypeChannel.GAZETA -> NewsFeed(Gson().fromJson(json, GazetaRss::class.java))
            TypeChannel.BBC -> NewsFeed(Gson().fromJson(json, BbcRss::class.java))
            TypeChannel.WASHINGTONPOST -> NewsFeed(Gson().fromJson(json, WashingtonPostRss::class.java))
            TypeChannel.TWiT -> NewsFeed(Gson().fromJson(json, TwitRss::class.java))
        }
    }

    private fun parserCharset(stringInfo: String): Charset {
        val rx = """encoding="(\S+)"""".toRegex()
        val found = rx.findAll(stringInfo).toList()
        if (found.isNotEmpty()) {
            return try {
                charset(found[0].groupValues[1])
            }catch(e: UnsupportedCharsetException) {
                Charset.defaultCharset()
            }
        }
        return Charset.defaultCharset()
    }

    private fun convertXmlToJson(xml: String): String {

        var result = xml

        val rx = """<[/]?(\w*:)\w*|(\w*:)\w*=""".toRegex()
        val found = rx.findAll(xml)
        val setReplace: MutableSet<String> = hashSetOf()
        found.forEach { f ->
            if (f.groupValues[1] != "") {
                setReplace.add(f.groupValues[1])
            } else {
                setReplace.add(f.groupValues[2])
            }
        }

        for (elem in setReplace) {
            if (elem == "") continue
            result = result.replace(elem, "")
        }
        result = result.replace(",", "")
        result = result.replace("<br/>", "")

        if (result.isEmpty()) return result

        if (result[result.length - 1] == ']') {
            result = result.take(result.length - 1)
        }
        if (result[0] == '[') {
            result = result.takeLast(result.length - 1)
        }

        val xmlToJson = XmlToJson.Builder(result)
            .setAttributeName("", "")
            .build()
        return xmlToJson.toString()
    }

}
