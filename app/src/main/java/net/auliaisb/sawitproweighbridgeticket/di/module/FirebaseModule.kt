package net.auliaisb.sawitproweighbridgeticket.di.module

import com.google.firebase.Firebase
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.database
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import net.auliaisb.sawitproweighbridgeticket.BuildConfig

@Module
@InstallIn(ViewModelComponent::class)
object FirebaseModule {
    @Provides
    fun providesDatabaseModule(): FirebaseDatabase {
        return Firebase.database(BuildConfig.FIREBASE_DB_URL)
    }
}