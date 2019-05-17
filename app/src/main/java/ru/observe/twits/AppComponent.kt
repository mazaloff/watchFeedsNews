package ru.observe.twits

import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import ru.observe.twits.views.NewsFeedsActivityModule
import ru.observe.twits.views.NewsFeedsFragmentModule
import javax.inject.Singleton

@Singleton
@Component(
    modules = [AndroidSupportInjectionModule::class,
        AppModule::class,
        NewsFeedsActivityModule::class,
        NewsFeedsFragmentModule::class]
)
interface AppComponent : AndroidInjector<AppWatchTwits> {

    @Component.Builder
    abstract class Builder : AndroidInjector.Builder<AppWatchTwits>()
}
