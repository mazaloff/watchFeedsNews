package ru.observe.twits.viewmodels

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.text.Html
import android.view.LayoutInflater
import android.view.ViewGroup

import com.squareup.picasso.Picasso

import ru.observe.twits.databinding.ItemNewBinding
import ru.observe.twits.uimodels.ItemFeed

class FeedViewAdapter(private var items: List<ItemFeed>,
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

    fun replaceData(list: List<ItemFeed>) {
        items = list
        notifyDataSetChanged()
    }

    class ViewHolder(var binding: ItemNewBinding): RecyclerView.ViewHolder(binding.root) {

        private val heightPx = dpToPx(binding.root.context, 64.toFloat())
        private val widthPx = dpToPx(binding.root.context, 100.toFloat())

        private fun dpToPx(context: Context, dp: Float): Int {
            return (dp * context.resources.displayMetrics.density).toInt()
        }

        fun bind(itemFeed: ItemFeed, listener: OnItemClickListener?) {
            binding.item = itemFeed

            @Suppress("DEPRECATION")
            binding.itemDesc.text = Html.fromHtml(itemFeed.description)

            Picasso.with(binding.root.context).load(itemFeed.thumbnail)
                .resize(widthPx, heightPx)
                .into(binding.itemThumb)

            if (listener != null) {
                binding.root.setOnClickListener { listener.onItemClick(itemFeed) }
            }
            binding.executePendingBindings()
        }
    }

}