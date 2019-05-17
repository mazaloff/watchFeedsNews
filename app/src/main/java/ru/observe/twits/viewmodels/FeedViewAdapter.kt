package ru.observe.twits.viewmodels

import android.support.v7.widget.RecyclerView
import android.text.Html
import android.view.LayoutInflater
import android.view.ViewGroup

import com.squareup.picasso.Picasso
import java.util.ArrayList

import ru.observe.twits.databinding.ItemNewBinding
import ru.observe.twits.uimodels.ItemFeed

class FeedViewAdapter(private var items: ArrayList<ItemFeed>,
                      private val listener: OnItemClickListener):
    RecyclerView.Adapter<FeedViewAdapter.ViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(itemFeed: ItemFeed)
    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(p0.context)
        val binding = ItemNewBinding.inflate(layoutInflater, p0, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(p0: ViewHolder, p1: Int) {
        p0.bind(items[p1], listener)
    }

    override fun getItemCount(): Int = items.size

    fun replaceData(arrayList: ArrayList<ItemFeed>) {
        items = arrayList
        notifyDataSetChanged()
    }

    class ViewHolder(var binding: ItemNewBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(itemFeed: ItemFeed, listener: OnItemClickListener?) {
            binding.item = itemFeed

            @Suppress("DEPRECATION")
            binding.itemDesc.text = Html.fromHtml(itemFeed.description)

            Picasso.with(binding.root.context).load(itemFeed.thumbnail).into(binding.itemThumb)

            if (listener != null) {
                binding.root.setOnClickListener { listener.onItemClick(itemFeed) }
            }
            binding.executePendingBindings()
        }
    }

}