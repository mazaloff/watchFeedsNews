package ru.observe.twits.views

import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.os.PersistableBundle
import android.support.v4.app.FragmentManager
import android.util.AndroidRuntimeException
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import dagger.android.AndroidInjection
import dagger.android.support.DaggerAppCompatActivity
import ru.observe.twits.BuildConfig
import ru.observe.twits.R

import ru.observe.twits.databinding.AcPlaceNewsFeedBinding
import ru.observe.twits.services.PlayService
import ru.observe.twits.viewmodels.NewsFeedViewModel
import ru.observe.twits.viewmodels.NewsFeedViewModelFactory
import javax.inject.Inject

class NewsFeedActivity : DaggerAppCompatActivity() {

    companion object {
        private const val PARAM_LINK_NEWS = "PARAM_LINK_NEWS"
        private const val PARAM_TYPE_NEWS = "PARAM_TYPE_NEWS"
        private const val PARAM_URL_WEB = "PARAM_URL_WEB"

        private const val TAG_NEWS_FEED_VIEW_MODEL = "TAG_NEWS_FEED_VIEW_MODEL"
    }

    @Inject
    lateinit var feedViewModelFactory: NewsFeedViewModelFactory
    lateinit var newsFeedViewModel: NewsFeedViewModel

    private lateinit var bindingFragment: AcPlaceNewsFeedBinding

    private var urlWeb: String? = null
    private lateinit var linkNews: String
    private lateinit var typeNews: String

    private val nameActivity = this::class.java.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)

        Log.d(BuildConfig.TAG, "$nameActivity - onCreate")

        onNewIntent(intent)

        restoreNewsFeedViewModel()

        bindingFragment = DataBindingUtil.setContentView(this, R.layout.ac_place_news_feed)

        if (savedInstanceState != null) {
            onRestoreInstanceState(savedInstanceState)
        }

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

    override fun onStart() {
        super.onStart()
        Log.d(BuildConfig.TAG, "$nameActivity - onStart")
    }

    override fun onResume() {
        super.onResume()
        Log.d(BuildConfig.TAG, "$nameActivity - onResume")
    }

    override fun onPause() {
        super.onPause()
        Log.d(BuildConfig.TAG, "$nameActivity - onPause")
    }

    override fun onStop() {
        super.onStop()
        Log.d(BuildConfig.TAG, "$nameActivity - onStop")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(BuildConfig.TAG, "$nameActivity - onDestroy")
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        Log.d(BuildConfig.TAG, "$nameActivity - onSaveInstanceState")
        outState?.apply {
            putString(PARAM_LINK_NEWS, linkNews)
            putString(PARAM_TYPE_NEWS, typeNews)
        }

    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        super.onRestoreInstanceState(savedInstanceState)
        Log.d(BuildConfig.TAG, "$nameActivity - onRestoreInstanceState")
        savedInstanceState?.apply {
            linkNews = getString(PARAM_LINK_NEWS, "")
            typeNews = getString(PARAM_TYPE_NEWS, "")
            urlWeb = getString(PARAM_URL_WEB)
        }
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
            bindingFragment.fragmentSite?.apply {
                supportFragmentManager.popBackStack("showUrl", FragmentManager.POP_BACK_STACK_INCLUSIVE)
                visibility = View.GONE
                return}
            supportFragmentManager.popBackStack("showFeed", FragmentManager.POP_BACK_STACK_INCLUSIVE)
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
                beginTransaction().replace(frameSite.id, fragment).addToBackStack("showUrl").commitAllowingStateLoss()
            }
        } else {
            val framePlace = bindingFragment.fragmentPlace
            supportFragmentManager.apply {
                beginTransaction().add(framePlace.id, fragment).addToBackStack("showFeed").commitAllowingStateLoss()
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

    private fun restoreNewsFeedViewModel() {
        val retainWorkerFragment = supportFragmentManager.findFragmentByTag(TAG_NEWS_FEED_VIEW_MODEL)
        if (retainWorkerFragment == null) {
            val newRetainWorkerFragment = RetainWorkerFragment()
            newsFeedViewModel = ViewModelProviders.of(this, feedViewModelFactory).get(NewsFeedViewModel::class.java)
            newRetainWorkerFragment.newsFeedViewModel = newsFeedViewModel
            supportFragmentManager.beginTransaction().add(newRetainWorkerFragment, TAG_NEWS_FEED_VIEW_MODEL).commit()
            Log.d(BuildConfig.TAG, "$nameActivity - create new newsFeedViewModel")
        } else {
            newsFeedViewModel =
                (retainWorkerFragment as RetainWorkerFragment).newsFeedViewModel
                    ?: ViewModelProviders.of(this, feedViewModelFactory).get(NewsFeedViewModel::class.java)
            Log.d(BuildConfig.TAG, "$nameActivity - restored old newsFeedViewModel")
        }
    }

}