package ru.observe.twits.views

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import ru.observe.twits.R

class BrowserFragment:Fragment() {

    companion object {
        internal const val PARAM_URL = "PARAM_URL"
    }

    private lateinit var url: String
    private lateinit var vBrowser: WebView

    override fun onCreate(savedInstanceState: Bundle?) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState)
        url = arguments?.getString(PARAM_URL) ?: ""
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fr_browser, container, false)
        vBrowser = view.findViewById(R.id.fragment_browser)
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        vBrowser.loadUrl(url)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.menu_news_feeds_refresh -> {
                vBrowser.loadUrl(url)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

}