package ru.observe.twits.views

import dagger.Module
import dagger.Provides
import dagger.android.ContributesAndroidInjector
import ru.observe.twits.data.FeedRepository
import ru.observe.twits.viewmodels.NewsFeedViewModelFactory

@Module
internal abstract class NewsFeedFragmentModule {

    @Module
    companion object {

        @JvmStatic
        @Provides
        internal fun providesFeedViewModelFactory(feedRepository: FeedRepository)
                : NewsFeedViewModelFactory {
            return NewsFeedViewModelFactory(feedRepository)
        }
    }

    @ContributesAndroidInjector()
    internal abstract fun mainFragment(): NewsFeedFragment

}