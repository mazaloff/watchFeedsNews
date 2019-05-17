package ru.observe.twits.viewmodels

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import ru.observe.twits.R

import ru.observe.twits.databinding.ItemLinkBinding
import java.util.ArrayList

import ru.observe.twits.uimodels.ItemLink
import ru.observe.twits.uimodels.TypeLink

class MainViewAdapter(private var items: ArrayList<ItemLink>,
                      private val listener: OnItemLinkClickListener):
    RecyclerView.Adapter<MainViewAdapter.ViewHolder>() {

    interface OnItemLinkClickListener {
        fun onItemLinkClick(itemLink: ItemLink)
    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(p0.context)
        val binding = ItemLinkBinding.inflate(layoutInflater, p0, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(p0: ViewHolder, p1: Int) {
        p0.bind(items[p1], listener)
    }

    override fun getItemCount(): Int = items.size

    fun replaceData(arrayList: ArrayList<ItemLink>) {
        items = arrayList
        notifyDataSetChanged()
    }

    class ViewHolder(var binding: ItemLinkBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(itemLink: ItemLink, listener: OnItemLinkClickListener?) {
            binding.item = itemLink

            if (listener != null) {
                binding.root.setOnClickListener { listener.onItemLinkClick(itemLink) }
            }

            if (itemLink.type == TypeLink.BBC) {
                binding.itemLinkImage.setImageResource(R.mipmap.logo_bbc)
            }else {
                binding.itemLinkImage.setImageResource(R.mipmap.logo_twit)
            }

            binding.executePendingBindings()
        }
    }

}