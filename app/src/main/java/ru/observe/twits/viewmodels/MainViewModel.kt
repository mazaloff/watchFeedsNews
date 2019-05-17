package ru.observe.twits.viewmodels

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import ru.observe.twits.tools.NonNullObservableField
import ru.observe.twits.uimodels.Links

class MainViewModel : ViewModel() {

    val resourceData = MutableLiveData<Links>()

    val isLoading = NonNullObservableField(false)

    fun loadLinks() {
        isLoading.set(true)
        resourceData.value = Links
        isLoading.set(false)
    }

}