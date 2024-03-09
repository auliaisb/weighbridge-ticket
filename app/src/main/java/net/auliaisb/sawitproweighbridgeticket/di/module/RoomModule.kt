package net.auliaisb.sawitproweighbridgeticket.di.module

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import net.auliaisb.sawitproweighbridgeticket.data.room.AppDatabase
import net.auliaisb.sawitproweighbridgeticket.data.room.TicketDao

@Module
@InstallIn(SingletonComponent::class)
object RoomModule {
    @Provides
    fun providesAppDb(@ApplicationContext appContext: Context): AppDatabase {
        return Room
            .databaseBuilder(appContext, AppDatabase::class.java, "WeighbridgeDB")
            .build()
    }

    @Provides
    fun providesTicketDao(appDatabase: AppDatabase): TicketDao {
        return appDatabase.ticketDao()
    }
}