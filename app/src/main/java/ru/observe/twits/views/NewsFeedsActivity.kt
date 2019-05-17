package ru.observe.twits.views

import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.util.AndroidRuntimeException
import android.view.View
import dagger.android.AndroidInjection
import dagger.android.support.DaggerAppCompatActivity
import ru.observe.twits.R

import ru.observe.twits.databinding.ActivityFragmentBinding
import ru.observe.twits.databinding.ActivityMainBinding
import ru.observe.twits.services.PlayService

class NewsFeedsActivity : DaggerAppCompatActivity() {

    private lateinit var bindingMain: ActivityMainBinding
    private lateinit var bindingFragment: ActivityFragmentBinding

    private var urlWeb: String? = null
    private lateinit var linkNews: String
    private lateinit var typeNews: String

    private var returnPlayService = false

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)

        onNewIntent(intent);

        bindingMain = DataBindingUtil.setContentView(this, R.layout.activity_main)
        bindingFragment = DataBindingUtil.setContentView(this, R.layout.activity_fragment)

        if (savedInstanceState != null) return

        val fragment = NewsFeedsFragment()
        fragment.arguments = Bundle().apply {
            putString("linkNews", linkNews)
            putString("typeNews", typeNews)
        }
        supportFragmentManager.beginTransaction().replace(
            bindingFragment.fragmentPlace.id, fragment
        ).commitAllowingStateLoss()

        urlWeb?.let { showArticle(it) }

    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        intent?.extras?.let {
            linkNews = it.getString("linkNews") ?: throw AndroidRuntimeException("not linkNews")
            typeNews = it.getString("typeNews") ?: throw AndroidRuntimeException("not typeNews")
            urlWeb = it.getString("urlWeb")
        }
    }

    fun showArticle(url: String) {
        val fragment = ViewUrlFragment()
        fragment.arguments = Bundle().apply {
            putString("url", url)
        }

        val frameSite = bindingFragment.fragmentSite
        if (frameSite != null) {
            frameSite.visibility = View.VISIBLE
            supportFragmentManager.apply {
                beginTransaction().replace(frameSite.id, fragment).commitAllowingStateLoss()
            }
        } else {
            val framePlace = bindingFragment.fragmentPlace
            supportFragmentManager.apply {
                beginTransaction().add(framePlace.id, fragment).addToBackStack("main").commitAllowingStateLoss()
            }
        }
    }

    fun playMusic(mp3: String, url: String, title: String) {
        Intent(this, PlayService::class.java).apply {
            putExtra("title", title)
            putExtra("urlMp3", mp3)
            putExtra("urlWeb", url)
            putExtra("linkNews", linkNews)
            putExtra("typeNews", typeNews)
            startService(this)
        }
    }

}