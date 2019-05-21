package ru.observe.twits.views

import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.util.AndroidRuntimeException
import android.view.Menu
import android.view.MenuItem
import android.view.View
import dagger.android.AndroidInjection
import dagger.android.support.DaggerAppCompatActivity
import ru.observe.twits.R

import ru.observe.twits.databinding.AcPlaceNewsFeedBinding
import ru.observe.twits.services.PlayService

class NewsFeedActivity : DaggerAppCompatActivity() {

    private lateinit var bindingFragment: AcPlaceNewsFeedBinding

    private var urlWeb: String? = null
    private lateinit var linkNews: String
    private lateinit var typeNews: String

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)

        onNewIntent(intent);

        bindingFragment = DataBindingUtil.setContentView(this, R.layout.ac_place_news_feed)

        if (savedInstanceState != null) return

        val fragment = NewsFeedFragment()
        fragment.arguments = Bundle().apply {
            putString("linkNews", linkNews)
            putString("typeNews", typeNews)
        }

        supportFragmentManager.beginTransaction().replace(
            bindingFragment.fragmentPlace.id, fragment
        ).commitAllowingStateLoss()

        urlWeb?.let { showArticle(it)}
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        intent?.extras?.let {
            linkNews = it.getString("linkNews") ?: throw AndroidRuntimeException("not linkNews")
            typeNews = it.getString("typeNews") ?: throw AndroidRuntimeException("not typeNews")
            urlWeb = it.getString("urlWeb")
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.news_feed, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.menu_news_feeds_refresh -> {
                return false
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount > 0) {
            supportFragmentManager.popBackStack()
        }else {
            super.onBackPressed()
        }
    }

    fun showArticle(url: String) {
        val fragment = BrowserFragment()
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
                beginTransaction().add(framePlace.id, fragment).addToBackStack("newsFeeds").commitAllowingStateLoss()
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