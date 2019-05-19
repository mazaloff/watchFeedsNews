package ru.observe.twits.views

import dagger.Module
import dagger.Provides
import dagger.android.ContributesAndroidInjector
import ru.observe.twits.data.FeedRepository
import ru.observe.twits.viewmodels.MainViewModelFactory

@Module
internal abstract class MainActivityModule {

    @Module
    companion object {

        @JvmStatic
        @Provides
        internal fun providesMainViewModelFactory(feedRepository: FeedRepository)
                : MainViewModelFactory {
            return MainViewModelFactory(feedRepository)
        }
    }

    @ContributesAndroidInjector()
    internal abstract fun mainActivity(): MainActivity

}