package ru.observe.twits.views

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import dagger.android.AndroidInjection
import dagger.android.support.DaggerAppCompatActivity
import ru.observe.twits.R

import ru.observe.twits.databinding.ActivityMainBinding
import ru.observe.twits.uimodels.ItemLink
import ru.observe.twits.viewmodels.MainViewAdapter
import ru.observe.twits.viewmodels.MainViewModel
import ru.observe.twits.viewmodels.MainViewModelFactory
import javax.inject.Inject

class MainActivity : DaggerAppCompatActivity(), MainViewAdapter.OnItemLinkClickListener {

    override fun onItemLinkClick(itemLink: ItemLink) {
       Intent(this, NewsFeedsActivity::class.java)
           .putExtra("linkNews", itemLink.link)
           .putExtra("typeNews", itemLink.type.toString())
           .apply { startActivity(this) }
    }

    @Inject
    lateinit var mainViewModelFactory: MainViewModelFactory

    private lateinit var binding: ActivityMainBinding
    private val mainViewAdapter = MainViewAdapter(arrayListOf(), this)

    private lateinit var viewModel : MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        viewModel = ViewModelProviders.of(this, mainViewModelFactory).get(MainViewModel::class.java)

        binding.viewModel = viewModel
        binding.executePendingBindings()

        binding.mainRecView.layoutManager = LinearLayoutManager(this)
        binding.mainRecView.adapter = mainViewAdapter

        if (savedInstanceState == null) {
            binding.viewModel?.loadLinks()
        }

        binding.viewModel?.resourceData?.observe(this,
            Observer { resource ->
                if (resource != null && resource.data?.items != null) {
                    mainViewAdapter.replaceData(resource.data.items)
                }
            }
        )

    }

}