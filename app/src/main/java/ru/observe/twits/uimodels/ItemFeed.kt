package ru.observe.twits.uimodels

import android.databinding.BaseObservable
import android.databinding.Bindable
import com.android.databinding.library.baseAdapters.BR

class ItemFeed(
    title: String,
    val link: String,
    val thumbnail: String,
    description: String,
    val guid: String
): BaseObservable() {
    @get:Bindable
    var title : String = title
        set(value) {
            field = value
            notifyPropertyChanged(BR.title)
        }
    @get:Bindable
    var description : String = description
        set(value) {
            field = value
            notifyPropertyChanged(BR.description)
        }

}