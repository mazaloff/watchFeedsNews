package ru.observe.twits.tools

import ru.observe.twits.AppWatchTwits
import ru.observe.twits.R


class Localizations {

    companion object {
        val serviceLoad = AppWatchTwits.res.getString(R.string.service_load) ?: ""
        val serviceStop = AppWatchTwits.res.getString(R.string.service_stop) ?: ""
        val servicePause = AppWatchTwits.res.getString(R.string.service_pause) ?: ""
        val serviceResume = AppWatchTwits.res.getString(R.string.service_resume) ?: ""
    }

}
