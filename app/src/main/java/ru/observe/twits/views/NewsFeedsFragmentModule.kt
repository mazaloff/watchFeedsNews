package ru.observe.twits.views

import dagger.Module
import dagger.Provides
import dagger.android.ContributesAndroidInjector
import ru.observe.twits.data.FeedRepository
import ru.observe.twits.viewmodels.FeedViewModelFactory

@Module
internal abstract class NewsFeedsFragmentModule {

    @Module
    companion object {

        @JvmStatic
        @Provides
        internal fun providesFeedViewModelFactory(feedRepository: FeedRepository)
                : FeedViewModelFactory {
            return FeedViewModelFactory(feedRepository)
        }
    }

    @ContributesAndroidInjector()
    internal abstract fun mainFragment(): NewsFeedsFragment

}