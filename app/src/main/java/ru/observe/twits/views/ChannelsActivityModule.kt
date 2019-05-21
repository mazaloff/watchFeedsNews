package ru.observe.twits.views

import dagger.Module
import dagger.Provides
import dagger.android.ContributesAndroidInjector
import ru.observe.twits.data.FeedRepository
import ru.observe.twits.viewmodels.ChannelsViewModelFactory

@Module
internal abstract class ChannelsActivityModule {

    @Module
    companion object {

        @JvmStatic
        @Provides
        internal fun providesMainViewModelFactory(feedRepository: FeedRepository)
                : ChannelsViewModelFactory {
            return ChannelsViewModelFactory(feedRepository)
        }
    }

    @ContributesAndroidInjector()
    internal abstract fun mainActivity(): ChannelsActivity

}