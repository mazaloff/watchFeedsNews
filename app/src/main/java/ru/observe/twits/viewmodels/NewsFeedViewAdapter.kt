package ru.observe.twits.viewmodels

import android.arch.paging.PagedListAdapter
import android.content.Context
import android.support.v7.widget.RecyclerView
import android.text.Html
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup

import com.squareup.picasso.Picasso
import ru.observe.twits.BuildConfig
import ru.observe.twits.R
import ru.observe.twits.data.Resource

import ru.observe.twits.databinding.ItemNewsFeedBinding
import ru.observe.twits.tools.CircleTransform
import ru.observe.twits.uimodels.ItemNewsFeed

import ru.observe.twits.tools.Constants


class NewsFeedViewAdapter(private val listener: OnItemClickListener
) :
    PagedListAdapter<ItemNewsFeed, NewsFeedViewAdapter.ViewHolder>(ItemNewsFeed.DIFF_CALLBACK) {

    private var networkState: Resource.Status? = null

    interface OnItemClickListener {
        fun onItemClick(itemNewsFeed: ItemNewsFeed)
    }

    private fun hasExtraRow(): Boolean {
        return networkState != null && networkState !== Resource.Status.LOADING
    }

    override fun getItemViewType(position: Int): Int {
        return if (hasExtraRow() && position == itemCount - 1) {
            Constants.TYPE_PROGRESS
        } else {
            Constants.TYPE_ITEM
        }
    }

    fun setNetworkState(newNetworkState: Resource.Status?) {
        val previousState = this.networkState
        val previousExtraRow = hasExtraRow()
        networkState = newNetworkState
        val newExtraRow = hasExtraRow()
        if (previousExtraRow != newExtraRow) {
            if (previousExtraRow) {
                Log.d(BuildConfig.TAG, "notifyItemRemoved $itemCount")
                notifyItemRemoved(itemCount)
            } else {
                Log.d(BuildConfig.TAG, "notifyItemInserted $itemCount")
                notifyItemInserted(itemCount)
            }
        } else if (newExtraRow && previousState !== newNetworkState) {
            Log.d(BuildConfig.TAG, "notifyItemChanged $itemCount")
            notifyItemChanged(itemCount - 1)
        }
    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(p0.context)
        val binding = ItemNewsFeedBinding.inflate(layoutInflater, p0, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(p0: ViewHolder, p1: Int) {
        val item = getItem(p1) ?: return
        p0.bind(item, listener)
    }

    class ViewHolder(var binding: ItemNewsFeedBinding) : RecyclerView.ViewHolder(binding.root) {

        private val heightPx = dpToPx(binding.root.context, 64.toFloat())
        private val widthPx = dpToPx(binding.root.context, 100.toFloat())

        private fun dpToPx(context: Context, dp: Float): Int {
            return (dp * context.resources.displayMetrics.density).toInt()
        }

        fun bind(itemNewsFeed: ItemNewsFeed, listener: OnItemClickListener?) {
            binding.item = itemNewsFeed

            @Suppress("DEPRECATION")
            binding.itemDesc.text = Html.fromHtml(itemNewsFeed.description)

            if (itemNewsFeed.thumbnail.isNotEmpty()) {
                Picasso.with(binding.root.context).load(itemNewsFeed.thumbnail)
                    .resize(widthPx, heightPx)
                    .centerCrop()
                    .transform(CircleTransform(15, 0))
                    .placeholder(R.mipmap.no_image)
                    .into(binding.itemThumb)
            }
            if (listener != null) {
                binding.root.setOnClickListener { listener.onItemClick(itemNewsFeed) }
            }
            binding.executePendingBindings()
        }
    }

}