package ru.observe.twits.views

import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
internal abstract class NewsFeedsActivityModule {

    @ContributesAndroidInjector()
    internal abstract fun newsFeedsActivity(): NewsFeedsActivity

}
