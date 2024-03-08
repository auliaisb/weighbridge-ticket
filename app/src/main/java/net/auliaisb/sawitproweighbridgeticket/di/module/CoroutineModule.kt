package net.auliaisb.sawitproweighbridgeticket.di.module

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

@Module
@InstallIn(SingletonComponent::class)
object CoroutineModule {
    @Provides
    fun providesIODispatcher() : CoroutineDispatcher {
        return Dispatchers.IO
    }
}