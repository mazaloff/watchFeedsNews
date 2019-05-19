package ru.observe.twits.viewmodels

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.squareup.picasso.Picasso

import ru.observe.twits.databinding.ItemLinkBinding

import ru.observe.twits.uimodels.ItemLink
import android.content.Context


class MainViewAdapter(
    private var items: List<ItemLink>,
    private val listener: OnItemLinkClickListener
) :
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

    fun replaceData(list: List<ItemLink>) {
        items = list
        notifyDataSetChanged()
    }

    class ViewHolder(var binding: ItemLinkBinding) : RecyclerView.ViewHolder(binding.root) {

        private val heightPx = dpToPx(binding.root.context, 64.toFloat())
        private val widthPx = dpToPx(binding.root.context, 100.toFloat())

        private fun dpToPx(context: Context, dp: Float): Int {
            return (dp * context.resources.displayMetrics.density).toInt()
        }

        fun bind(itemLink: ItemLink, listener: OnItemLinkClickListener?) {
            binding.item = itemLink

            if (listener != null) {
                binding.itemLinkImage0.setOnClickListener { listener.onItemLinkClick(itemLink) }
                binding.itemLinkImage1.setOnClickListener { listener.onItemLinkClick(itemLink) }
                binding.itemLinkImage2.setOnClickListener { listener.onItemLinkClick(itemLink) }
                binding.itemLinkImage3.setOnClickListener { listener.onItemLinkClick(itemLink) }
                binding.root.setOnClickListener { listener.onItemLinkClick(itemLink) }
            }

            itemLink.feed?.let {
                for (i in 0..3) {
                    val itemFeed = it.items[i]
                    Picasso.with(binding.root.context).load(itemFeed.thumbnail)
                        .resize(widthPx, heightPx)
                        .into(
                            when (i) {
                                0 -> binding.itemLinkImage0
                                1 -> binding.itemLinkImage1
                                2 -> binding.itemLinkImage2
                                else -> binding.itemLinkImage3
                            }
                        )
                }
            }

            binding.executePendingBindings()
        }
    }

}