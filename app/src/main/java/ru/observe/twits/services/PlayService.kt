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
import ru.observe.twits.views.NewsFeedsActivity
import java.util.*
import kotlin.concurrent.schedule

import ru.observe.twits.R

class PlayService : Service() {

    private var player: MediaPlayer? = null
    private var notification: NotificationCompat.Builder? = null

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
        player?.stop()

        if (intent?.action == "stop") {
            (getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager).cancel(333)
            stopSelf()
            return START_NOT_STICKY
        }

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

            p.start()

            Timer().schedule(1000, 1000) {
                if (!p.isPlaying) {
                    cancel()
                    return@schedule
                }
                notification?.setContentText(timeSecondsToStr((p.duration - p.currentPosition) / 1000))
                (getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager)
                    .notify(333, notification?.build())

            }
        }

        val notificationIntend = Intent(this, NewsFeedsActivity::class.java)
            .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            .putExtra("linkNews", linkNews)
            .putExtra("typeNews", typeNews)
            .putExtra("urlWeb", urlWeb)

        val iStop = Intent(this, PlayService::class.java).setAction("stop")
        val piStop = PendingIntent.getService(this, 0, iStop, PendingIntent.FLAG_CANCEL_CURRENT)

        notification = NotificationCompat.Builder(this, "1")
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle(title)
            .setContentText("you can turn off playing mp3")
            .setContentIntent(
                PendingIntent.getActivities(
                    this, 0,
                    arrayOf<Intent?>(notificationIntend), PendingIntent.FLAG_CANCEL_CURRENT
                )
            )
            .addAction(R.mipmap.ic_launcher, "stop", piStop)
            .setOngoing(false)

        (getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager).notify(333, notification?.build())

        return START_NOT_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        player?.stop()
    }

}