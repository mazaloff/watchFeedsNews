package ru.observe.twits.viewmodels

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.squareup.picasso.Picasso

import ru.observe.twits.databinding.ItemChannelBinding

import ru.observe.twits.uimodels.ItemChannel
import android.content.Context
import android.support.v7.util.DiffUtil
import android.widget.ImageView
import ru.observe.twits.tools.CircleTransform
import kotlin.math.min
import android.os.Parcelable


class ChannelsViewAdapter(
    private var items: List<ItemChannel>,
    private val listener: OnItemLinkClickListener
) :
    RecyclerView.Adapter<ChannelsViewAdapter.ViewHolder>() {

    interface OnItemLinkClickListener {
        fun onItemLinkClick(itemChannel: ItemChannel)
    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(p0.context)
        val binding = ItemChannelBinding.inflate(layoutInflater, p0, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(p0: ViewHolder, p1: Int) {
        p0.bind(items[p1], listener)
    }

    override fun getItemCount(): Int = items.size

    fun replaceData(list: List<ItemChannel>) {
       items = list
       notifyDataSetChanged()
    }

    class ViewHolder(var binding: ItemChannelBinding) : RecyclerView.ViewHolder(binding.root) {

        private val heightPx = dpToPx(binding.root.context, 64.toFloat())
        private val widthPx = dpToPx(binding.root.context, 100.toFloat())

        private fun dpToPx(context: Context, dp: Float): Int {
            return (dp * context.resources.displayMetrics.density).toInt()
        }

        fun bind(itemChannel: ItemChannel, listener: OnItemLinkClickListener?) {

            binding.item = itemChannel

            if (itemChannel.newsFeed == null) {
                binding.executePendingBindings()
                return
            }

            val imageCount = binding.itemRootLayout.childCount
            val itemsSize = itemChannel.newsFeed!!.items.size

            if (listener != null) {
                for (i in 0 until imageCount) {
                    binding.itemRootLayout.getChildAt(i).setOnClickListener { listener.onItemLinkClick(itemChannel) }
                }
                binding.root.setOnClickListener { listener.onItemLinkClick(itemChannel) }
            }

            for (i in 0 until min(imageCount, itemsSize)) {
                val urlImage = itemChannel.newsFeed!!.items[i].thumbnail
                if (urlImage.isNotEmpty()) {
                    Picasso.with(binding.root.context).load(urlImage)
                        .resize(widthPx, heightPx)
                        .centerCrop()
                        .transform(CircleTransform(15, 0))
                        .into(binding.itemRootLayout.getChildAt(i) as ImageView)
                }
            }

            binding.executePendingBindings()
        }
    }

}