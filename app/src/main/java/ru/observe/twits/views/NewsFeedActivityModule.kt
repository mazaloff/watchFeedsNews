package ru.observe.twits.views

import dagger.Module
import dagger.Provides
import dagger.android.ContributesAndroidInjector
import ru.observe.twits.data.FeedRepository
import ru.observe.twits.viewmodels.NewsFeedViewModelFactory

@Module
internal abstract class NewsFeedActivityModule {

    @Module
    companion object {

        @JvmStatic
        @Provides
        internal fun providesNewsFeedViewModelFactory(feedRepository: FeedRepository)
                : NewsFeedViewModelFactory {
            return NewsFeedViewModelFactory(feedRepository)
        }

    }

    @ContributesAndroidInjector()
    internal abstract fun newsFeedsActivity(): NewsFeedActivity

}
