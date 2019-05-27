package ru.observe.twits.views

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.content.SharedPreferences
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import dagger.android.AndroidInjection
import dagger.android.support.DaggerAppCompatActivity
import ru.observe.twits.BuildConfig
import ru.observe.twits.R
import ru.observe.twits.data.Resource
import ru.observe.twits.data.SharedHelper

import ru.observe.twits.databinding.AcChannelsBinding
import ru.observe.twits.uimodels.ItemChannel
import ru.observe.twits.uimodels.TypeChannel
import ru.observe.twits.viewmodels.ChannelsViewAdapter
import ru.observe.twits.viewmodels.ChannelsViewModel
import ru.observe.twits.viewmodels.ChannelsViewModelFactory
import javax.inject.Inject

class ChannelsActivity : DaggerAppCompatActivity(), ChannelsViewAdapter.OnItemLinkClickListener {

    companion object {
        private const val PARAM_CURRENT_TYPE_CHANNEL = "PARAM_CURRENT_TYPE_CHANNEL"
        private const val TAG_CHANNEL_VIEW_MODEL = "TAG_CHANNEL_VIEW_MODEL"
    }

    override fun onItemLinkClick(itemChannel: ItemChannel) {
        Intent(this, NewsFeedActivity::class.java)
            .putExtra("linkNews", itemChannel.link)
            .putExtra("typeNews", itemChannel.type.toString())
            .apply { startActivity(this) }
    }

    @Inject
    lateinit var channelsViewModelFactory: ChannelsViewModelFactory

    private lateinit var currentTypeChannel: TypeChannel
    private val channelsViewAdapter = ChannelsViewAdapter(listOf(), this)

    private lateinit var binding: AcChannelsBinding

    private lateinit var channelsViewModel: ChannelsViewModel

    private val nameActivity = this::class.java.simpleName

    private lateinit var mSharedHelper: SharedHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)

        mSharedHelper = SharedHelper(this)

        Log.d(BuildConfig.TAG, "$nameActivity - onCreate")

        binding = DataBindingUtil.setContentView(this, R.layout.ac_channels)

        restoreChannelsViewModel()

        binding.viewModel = channelsViewModel
        binding.executePendingBindings()

        binding.mainRecView.layoutManager = LinearLayoutManager(this)
        binding.mainRecView.adapter = channelsViewAdapter

        if (savedInstanceState == null) {
            currentTypeChannel = mSharedHelper.getTypeChannel()
        }else {
            onRestoreInstanceState(savedInstanceState)
        }

        binding.viewModel?.loadLinks(currentTypeChannel)

        binding.viewModel?.resourceData?.observe(this,
            Observer { resource ->
                if (resource == null) return@Observer
                if (resource.status == Resource.Status.COMPLETED && resource.data != null) {
                    channelsViewAdapter.replaceData(resource.data)
                }else if (resource.exception != null) {
                    resource.exception.printStackTrace();
                    channelsViewAdapter.replaceData(listOf())
                }else if (resource.status == Resource.Status.LOADING) {
                    channelsViewAdapter.replaceData(listOf())
                }
            }
        )

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
        mSharedHelper.setTypeChannel(currentTypeChannel)
        Log.d(BuildConfig.TAG, "$nameActivity - onDestroy")
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        Log.d(BuildConfig.TAG, "$nameActivity - onSaveInstanceState")
        outState?.apply {
            putSerializable(PARAM_CURRENT_TYPE_CHANNEL, currentTypeChannel)
        }
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        super.onRestoreInstanceState(savedInstanceState)
        Log.d(BuildConfig.TAG, "$nameActivity - onRestoreInstanceState")
        savedInstanceState?.apply {
            currentTypeChannel = get(PARAM_CURRENT_TYPE_CHANNEL) as TypeChannel
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.channels, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.menu_main_refresh -> {
                binding.viewModel?.loadLinks(currentTypeChannel)
                return true
            }
            R.id.menu_lenta -> {
                currentTypeChannel = TypeChannel.LENTA
                binding.viewModel?.loadLinks(currentTypeChannel)
                return true
            }
            R.id.menu_gazeta -> {
                currentTypeChannel = TypeChannel.GAZETA
                binding.viewModel?.loadLinks(currentTypeChannel)
                return true
            }
            R.id.menu_bbc -> {
                currentTypeChannel = TypeChannel.BBC
                binding.viewModel?.loadLinks(currentTypeChannel)
                return true
            }
            R.id.menu_twit -> {
                currentTypeChannel = TypeChannel.TWiT
                binding.viewModel?.loadLinks(currentTypeChannel)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun restoreChannelsViewModel() {
        val retainWorkerFragment = supportFragmentManager.findFragmentByTag(TAG_CHANNEL_VIEW_MODEL)
        if (retainWorkerFragment == null) {
            val newRetainWorkerFragment = RetainWorkerFragment()
            channelsViewModel = ViewModelProviders.of(this, channelsViewModelFactory).get(ChannelsViewModel::class.java)
            newRetainWorkerFragment.channelsViewModel = channelsViewModel
            supportFragmentManager.beginTransaction().add(newRetainWorkerFragment, TAG_CHANNEL_VIEW_MODEL).commit()
            Log.d(BuildConfig.TAG, "$nameActivity - create new channelsViewModel")
        }else {
            channelsViewModel = (retainWorkerFragment as RetainWorkerFragment).channelsViewModel ?:
                    ViewModelProviders.of(this, channelsViewModelFactory).get(ChannelsViewModel::class.java)
            Log.d(BuildConfig.TAG, "$nameActivity - restored old channelsViewModel")
        }
    }
}