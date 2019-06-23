package ru.observe.twits.uimodels

import android.databinding.BaseObservable
import android.databinding.Bindable
import com.android.databinding.library.baseAdapters.BR
import java.io.Serializable

class ItemChannel(name: String, val link: String, val type: TypeChannel, var newsFeed: NewsFeed? = null) : BaseObservable(),
    Serializable {

    @get:Bindable
    var name: String = name
        set(value) {
            field = value
            notifyPropertyChanged(BR.name)
        }

}