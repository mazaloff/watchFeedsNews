package ru.observe.twits.uimodels

import android.databinding.BaseObservable
import android.databinding.Bindable
import android.support.v7.util.DiffUtil
import com.android.databinding.library.baseAdapters.BR
import java.io.Serializable

class ItemNewsFeed(
    title: String,
    val link: String,
    val thumbnail: String,
    description: String?,
    val guid: String
): BaseObservable(), Serializable {
    @get:Bindable
    var title : String = title
        set(value) {
            field = value
            notifyPropertyChanged(BR.title)
        }
    @get:Bindable
    var description : String = description ?: ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.description)
        }

    override fun equals(other: Any?): Boolean {
        if (other is ItemNewsFeed) return false
        return this.guid == (other as ItemNewsFeed).guid
    }

    override fun hashCode(): Int {
        return this.guid.hashCode()
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ItemNewsFeed>() {
            override fun areItemsTheSame(oldItem: ItemNewsFeed, newItem: ItemNewsFeed): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: ItemNewsFeed, newItem: ItemNewsFeed): Boolean {
                return oldItem.guid == newItem.guid
            }
        }
    }

}