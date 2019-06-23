package ru.observe.twits.services

import android.annotation.SuppressLint
import android.media.MediaPlayer
import android.net.Uri
import android.os.Handler
import android.os.HandlerThread
import android.os.Looper
import android.os.Message
import android.util.Log
import ru.observe.twits.BuildConfig
import java.util.*
import kotlin.concurrent.schedule

@Suppress("CAST_NEVER_SUCCEEDS")
class PlayHaMeR(private val mPlayService: PlayService, name: String) : HandlerThread(name) {

    companion object {
        const val MESSAGE_PLAY = 1454111
        const val MESSAGE_STOP = 1454112
        const val MESSAGE_PAUSE = 1454113
        const val MESSAGE_RESUME = 1454114
    }

    interface Callback {
        fun onSendProgress(contentText: String)
        fun onStopPlay()
        fun onPausePlay(position: Int)
    }

    private lateinit var mMainHandler: Handler
    private lateinit var mBackgroundHandler: Handler

    private var mPlayer: MediaPlayer = MediaPlayer()

    // UI Thread prepare
    fun prepareHandler() {
        super.onLooperPrepared()

        mMainHandler = Handler(Looper.getMainLooper())
        mBackgroundHandler = @SuppressLint("HandlerLeak")
        object: Handler(looper) {
            override fun handleMessage(msg: Message?) {
                when (msg?.what) {
                    MESSAGE_PLAY -> startPlay(msg.obj as String)
                    MESSAGE_PAUSE -> pausePlay()
                    MESSAGE_RESUME -> resumePlay(msg.obj as Int)
                    MESSAGE_STOP -> stopPlay()
                }
            }
        }

    }

    private fun startPlay(inputData: String) {
        if (mPlayer.isPlaying) {
            mPlayer.stop()
        }
        mPlayer = MediaPlayer()
        mPlayer.setDataSource(mPlayService, Uri.parse(inputData))
        mPlayer.prepare()
        mPlayer.start()
        scheduleProgress()
    }

    private fun resumePlay(position: Int) {
        mPlayer.seekTo(position)
        mPlayer.start()
        scheduleProgress()
    }

    private fun stopPlay() {
        if (mPlayer.isPlaying) {
            mPlayer.stop()
        }
        mMainHandler.post {
            // UI Thread
            mPlayService.onStopPlay()
        }
    }

    private fun pausePlay() {
        if (mPlayer.isPlaying) {
            mPlayer.pause()
        }
        val contentText = timeSecondsToStr((mPlayer.duration - mPlayer.currentPosition) / 1000)
        mMainHandler.post {
            // UI Thread
            mPlayService.onPausePlay(mPlayer.currentPosition)
            mPlayService.onSendProgress(contentText)
        }
    }

    private fun scheduleProgress() {
        mPlayer.apply {
            Timer().schedule(1000, 1000) {
                if (!isPlaying) {
                    return@schedule
                }
                val contentText = timeSecondsToStr((duration - currentPosition) / 1000)
                mMainHandler.post {
                    // UI Thread
                    mPlayService.onSendProgress(contentText)
                }
            }
        }
    }

    // UI Thread post messages
    fun performPlay(url: String) {
        mBackgroundHandler
            .obtainMessage(MESSAGE_PLAY, url)
            .sendToTarget()
    }

    // UI Thread post messages
    fun performStop() {
        mBackgroundHandler
            .obtainMessage(MESSAGE_STOP)
            .sendToTarget()
    }

    // UI Thread post messages
    fun performPause() {
        mBackgroundHandler
            .obtainMessage(MESSAGE_PAUSE)
            .sendToTarget()
    }

    // UI Thread post messages
    fun performResume(position: Int) {
        mBackgroundHandler
            .obtainMessage(MESSAGE_RESUME, position)
            .sendToTarget()
    }

    private fun timeSecondsToStr(seconds: Int): String {
        val hour = seconds / 3600
        val minute = (seconds % 3600) / 60
        val second = seconds % 60
        return String.format("%02d:%02d:%02d", hour, minute, second)
    }

}