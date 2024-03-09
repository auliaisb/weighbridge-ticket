package net.auliaisb.sawitproweighbridgeticket.di.module

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import net.auliaisb.sawitproweighbridgeticket.data.repo.TicketRepository
import net.auliaisb.sawitproweighbridgeticket.data.room.TicketDao
import net.auliaisb.sawitproweighbridgeticket.data.service.TicketService

@InstallIn(ViewModelComponent::class)
@Module
object RepoModule {
    @Provides
    fun provideTicketRepo(ticketService: TicketService, ticketDao: TicketDao): TicketRepository {
        return TicketRepository(ticketService, ticketDao)
    }
}