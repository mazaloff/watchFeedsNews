package ru.observe.twits.services

import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.support.v4.app.NotificationCompat
import ru.observe.twits.views.NewsFeedActivity

import ru.observe.twits.R
import ru.observe.twits.managers.NetManager
import ru.observe.twits.tools.Localizations
import ru.observe.twits.uimodels.ItemChannel


class PlayService : Service(), PlayHaMeR.Callback {

    companion object {
        const val NOTIFICATION_ID = 911
        const val CHANNEL_ID = "ru.observe.twits.services"
        const val CHANNEL_NAME = "PLAY MP3";
    }

    private lateinit var mManager: NotificationManager
    private lateinit var mBuilder: NotificationCompat.Builder

    private var currentPosition = 0
    private var currentContentText = Localizations.serviceLoad

    private val strStop = Localizations.serviceStop
    private val strPause = Localizations.servicePause
    private val strResume = Localizations.serviceResume

    private lateinit var mPlayHaMeR: PlayHaMeR

    override fun onCreate() {
        super.onCreate()
        mManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        mPlayHaMeR = PlayHaMeR(this, "PlayHaMeRThread")
        mPlayHaMeR.start()
        mPlayHaMeR.prepareHandler()
    }

    private fun getNotificationBuilder(): NotificationCompat.Builder {
        return if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            NotificationCompat.Builder(this)
        } else {
            if (mManager.getNotificationChannel(CHANNEL_ID) == null) {
                mManager.createNotificationChannel(
                    NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_LOW)
                )
            }
            NotificationCompat.Builder(this, CHANNEL_ID)
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        NetManager(applicationContext).isConnectedToInternet?.let {isConnected ->
            if (!isConnected) {
                mPlayHaMeR.performStop()
                return START_NOT_STICKY
            }
        }

        mBuilder = getNotificationBuilder().apply { setSmallIcon(R.mipmap.ic_launcher) }

        val title = intent?.extras?.getString("title") ?: return START_NOT_STICKY
        val urlMp3 = intent.extras?.getString("urlMp3") ?: return START_NOT_STICKY
        val urlWeb  = intent.extras?.getString("urlWeb") ?: return START_NOT_STICKY
        val itemChannel = intent.extras?.getSerializable(ItemChannel::class.java.simpleName)
            ?: return START_NOT_STICKY

        if (!urlMp3.endsWith("mp3")) {
            mPlayHaMeR.performStop()
            return START_NOT_STICKY
        }

        when (intent.action) {
            strStop ->  mPlayHaMeR.performStop()
            strPause -> mPlayHaMeR.performPause()
            strResume -> mPlayHaMeR.performResume(currentPosition)
            else -> mPlayHaMeR.performPlay(urlMp3)
        }

        val strPauseResume = if (intent.action != strPause) strPause else strResume

        val notificationIntend = Intent(this, NewsFeedActivity::class.java)
            .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            .putExtra("urlWeb", urlWeb)
            .putExtra(ItemChannel::class.java.simpleName, itemChannel)

        val iStop = Intent(this, PlayService::class.java).setAction(strStop)
            .putExtra("title", title)
            .putExtra("urlMp3", urlMp3)
            .putExtra("urlWeb", urlWeb)
            .putExtra(ItemChannel::class.java.simpleName, itemChannel)
        val piStop = PendingIntent.getService(this, 0, iStop, PendingIntent.FLAG_CANCEL_CURRENT)

        val iPause = Intent(this, PlayService::class.java).setAction(strPauseResume)
            .putExtra("title", title)
            .putExtra("urlMp3", urlMp3)
            .putExtra("urlWeb", urlWeb)
            .putExtra(ItemChannel::class.java.simpleName, itemChannel)
        val piPause = PendingIntent.getService(this, 0, iPause, PendingIntent.FLAG_UPDATE_CURRENT)

        mBuilder.apply {
            setContentTitle(title)
            setContentText(currentContentText)
            setContentIntent(
                PendingIntent.getActivities(
                    this@PlayService, 0,
                    arrayOf<Intent?>(notificationIntend), PendingIntent.FLAG_CANCEL_CURRENT
                )
            )
            addAction(R.mipmap.ic_launcher, strPauseResume, piPause)
            addAction(R.mipmap.ic_launcher, strStop, piStop)
            setOngoing(false)
        }

        mManager.notify(NOTIFICATION_ID, mBuilder.build())

        return START_NOT_STICKY
    }

    override fun onSendProgress(contentText: String) {
        mBuilder.setContentText(contentText)
        mManager.notify(NOTIFICATION_ID, mBuilder.build())
    }

    override fun onStopPlay() {
        mManager.cancel(NOTIFICATION_ID)
        stopSelf()
    }

    override fun onPausePlay(position: Int) {
        currentPosition = position
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onDestroy() {
        mPlayHaMeR.quit()
        super.onDestroy()
    }

}