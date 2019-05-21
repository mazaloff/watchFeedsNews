package ru.observe.twits.views

import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
internal abstract class NewsFeedActivityModule {

    @ContributesAndroidInjector()
    internal abstract fun newsFeedsActivity(): NewsFeedActivity

}
