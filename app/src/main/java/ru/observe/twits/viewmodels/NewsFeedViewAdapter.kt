package ru.observe.twits.viewmodels

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.text.Html
import android.view.LayoutInflater
import android.view.ViewGroup

import com.squareup.picasso.Picasso

import ru.observe.twits.databinding.ItemNewsFeedBinding
import ru.observe.twits.tools.CircleTransform
import ru.observe.twits.uimodels.ItemNewsFeed

class NewsFeedViewAdapter(private var items: List<ItemNewsFeed>,
                          private val listener: OnItemClickListener):
    RecyclerView.Adapter<NewsFeedViewAdapter.ViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(itemNewsFeed: ItemNewsFeed)
    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(p0.context)
        val binding = ItemNewsFeedBinding.inflate(layoutInflater, p0, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(p0: ViewHolder, p1: Int) {
        p0.bind(items[p1], listener)
    }

    override fun getItemCount(): Int = items.size

    fun replaceData(list: List<ItemNewsFeed>) {
        items = list
        notifyDataSetChanged()
    }

    class ViewHolder(var binding: ItemNewsFeedBinding): RecyclerView.ViewHolder(binding.root) {

        private val heightPx = dpToPx(binding.root.context, 64.toFloat())
        private val widthPx = dpToPx(binding.root.context, 100.toFloat())

        private fun dpToPx(context: Context, dp: Float): Int {
            return (dp * context.resources.displayMetrics.density).toInt()
        }

        fun bind(itemNewsFeed: ItemNewsFeed, listener: OnItemClickListener?) {
            binding.item = itemNewsFeed

            @Suppress("DEPRECATION")
            binding.itemDesc.text = Html.fromHtml(itemNewsFeed.description)

            Picasso.with(binding.root.context).load(itemNewsFeed.thumbnail)
                .resize(widthPx, heightPx)
                .centerCrop()
                .transform(CircleTransform(15,0))
                .into(binding.itemThumb)

            if (listener != null) {
                binding.root.setOnClickListener { listener.onItemClick(itemNewsFeed) }
            }
            binding.executePendingBindings()
        }
    }

}