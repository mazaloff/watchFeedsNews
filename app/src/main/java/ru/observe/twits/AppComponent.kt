package ru.observe.twits

import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import ru.observe.twits.views.ChannelsActivityModule
import ru.observe.twits.views.NewsFeedActivityModule
import javax.inject.Singleton

@Singleton
@Component(
    modules = [AndroidSupportInjectionModule::class,
        AppModule::class,
        ChannelsActivityModule::class,
        NewsFeedActivityModule::class]
)
interface AppComponent : AndroidInjector<AppWatchTwits> {

    @Component.Builder
    abstract class Builder : AndroidInjector.Builder<AppWatchTwits>()
}
