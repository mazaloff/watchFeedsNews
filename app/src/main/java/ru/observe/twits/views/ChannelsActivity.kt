package ru.observe.twits.views

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import android.view.MenuItem
import dagger.android.AndroidInjection
import dagger.android.support.DaggerAppCompatActivity
import ru.observe.twits.R
import ru.observe.twits.data.Resource

import ru.observe.twits.databinding.AcChannelsBinding
import ru.observe.twits.uimodels.ItemChannel
import ru.observe.twits.uimodels.TypeChannel
import ru.observe.twits.viewmodels.ChannelsViewAdapter
import ru.observe.twits.viewmodels.ChannelsViewModel
import ru.observe.twits.viewmodels.ChannelsViewModelFactory
import javax.inject.Inject

class ChannelsActivity : DaggerAppCompatActivity(), ChannelsViewAdapter.OnItemLinkClickListener {

    override fun onItemLinkClick(itemChannel: ItemChannel) {
        Intent(this, NewsFeedActivity::class.java)
            .putExtra("linkNews", itemChannel.link)
            .putExtra("typeNews", itemChannel.type.toString())
            .apply { startActivity(this) }
    }

    @Inject
    lateinit var channelsViewModelFactory: ChannelsViewModelFactory

    private lateinit var currentTypeChannel: TypeChannel
    private lateinit var bindingSelf: AcChannelsBinding
    private val channelsViewAdapter = ChannelsViewAdapter(listOf(), this)

    private lateinit var channelsViewModel: ChannelsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)

        bindingSelf = DataBindingUtil.setContentView(this, R.layout.ac_channels)

        channelsViewModel = ViewModelProviders.of(this, channelsViewModelFactory).get(ChannelsViewModel::class.java)

        bindingSelf.viewModel = channelsViewModel
        bindingSelf.executePendingBindings()

        bindingSelf.mainRecView.layoutManager = LinearLayoutManager(this)
        bindingSelf.mainRecView.adapter = channelsViewAdapter

        if (savedInstanceState == null) {
            currentTypeChannel = TypeChannel.BBC
            bindingSelf.viewModel?.loadLinks(currentTypeChannel)
        }

        bindingSelf.viewModel?.resourceData?.observe(this,
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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.channels, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.menu_main_refresh -> {
                bindingSelf.viewModel?.loadLinks(currentTypeChannel)
                return true
            }
            R.id.menu_lenta -> {
                currentTypeChannel = TypeChannel.LENTA
                bindingSelf.viewModel?.loadLinks(currentTypeChannel)
                return true
            }
            R.id.menu_bbc -> {
                currentTypeChannel = TypeChannel.BBC
                bindingSelf.viewModel?.loadLinks(currentTypeChannel)
                return true
            }
            R.id.menu_twit -> {
                currentTypeChannel = TypeChannel.TWiT
                bindingSelf.viewModel?.loadLinks(currentTypeChannel)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

}