package ru.observe.twits.services

import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import android.os.IBinder
import android.support.v4.app.NotificationCompat
import ru.observe.twits.views.NewsFeedActivity
import java.util.*
import kotlin.concurrent.schedule

import ru.observe.twits.R
import ru.observe.twits.managers.NetManager
import ru.observe.twits.tools.Localizations

class PlayService : Service() {

    private var player: MediaPlayer? = null
    private var notification: NotificationCompat.Builder? = null
    private var currentPosition = 0

    private var currentContentText = Localizations.serviceLoad

    private val strStop = Localizations.serviceStop
    private val strPause = Localizations.servicePause
    private val strResume = Localizations.serviceResume

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    private fun timeSecondsToStr(seconds: Int): String {
        val hour = seconds / 3600
        val minute = (seconds % 3600) / 60
        val second = seconds % 60
        return String.format("%02d:%02d:%02d", hour, minute, second)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        if ((NetManager(applicationContext).isConnectedToInternet != true)
            || (intent?.action == strStop)
        ) {
            player?.stop()
            (getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager).cancel(333)
            stopSelf()
            return START_NOT_STICKY
        }

        if (intent?.action == strPause) {
            currentPosition = player?.currentPosition ?: 0
        }

        player?.stop()

        val title = intent?.extras?.getString("title") ?: return START_NOT_STICKY
        val urlMp3 = intent.extras?.getString("urlMp3") ?: return START_NOT_STICKY
        val urlWeb = intent.extras?.getString("urlWeb") ?: return START_NOT_STICKY
        val linkNews = intent.extras?.getString("linkNews") ?: return START_NOT_STICKY
        val typeNews = intent.extras?.getString("typeNews") ?: return START_NOT_STICKY

        if (!urlMp3.endsWith("mp3")) {
            (getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager).cancel(333)
            stopSelf()
            return START_NOT_STICKY
        }

        player = MediaPlayer()
        player?.setDataSource(this, Uri.parse(urlMp3))
        player?.prepareAsync()
        player?.setOnPreparedListener { p ->

            if (intent.action != strPause) {
                p.seekTo(currentPosition)
                p.start()
            }

            Timer().schedule(1000, 1000) {
                if (!p.isPlaying) {
                    cancel()
                    return@schedule
                }
                currentContentText = timeSecondsToStr((p.duration - p.currentPosition) / 1000)
                notification?.setContentText(currentContentText)
                (getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager)
                    .notify(333, notification?.build())
            }
        }

        val notificationIntend = Intent(this, NewsFeedActivity::class.java)
            .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            .putExtra("urlWeb", urlWeb)
            .putExtra("linkNews", linkNews)
            .putExtra("typeNews", typeNews)

        val iStop = Intent(this, PlayService::class.java).setAction(strStop)
        val piStop = PendingIntent.getService(this, 0, iStop, PendingIntent.FLAG_CANCEL_CURRENT)

        val currentOnOffAction = if (intent.action != strPause) strPause else strResume
        val iPause = Intent(this, PlayService::class.java).setAction(currentOnOffAction)
            .putExtra("title", title)
            .putExtra("urlMp3", urlMp3)
            .putExtra("urlWeb", urlWeb)
            .putExtra("linkNews", linkNews)
            .putExtra("typeNews", typeNews)

        val piPause = PendingIntent.getService(this, 0, iPause, PendingIntent.FLAG_UPDATE_CURRENT)

        notification = NotificationCompat.Builder(this, "1")
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle(title)
            .setContentText(currentContentText)
            .setContentIntent(
                PendingIntent.getActivities(
                    this, 0,
                    arrayOf<Intent?>(notificationIntend), PendingIntent.FLAG_CANCEL_CURRENT
                )
            )
            .addAction(R.mipmap.ic_launcher, currentOnOffAction, piPause)
            .addAction(R.mipmap.ic_launcher, strStop, piStop)
            .setOngoing(false)

        (getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager).notify(333, notification?.build())

        return START_NOT_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        player?.stop()
    }

}