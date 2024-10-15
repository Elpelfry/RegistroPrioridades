package edu.ucne.registroprioridades.di

import android.content.Context
import androidx.room.Room
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import edu.ucne.registroprioridades.data.local.database.TicketDb
import edu.ucne.registroprioridades.data.remote.api.TicketApi
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object AppModule {

    const val BASE_URL = "https://ticket-api.azurewebsites.net/"

    @Singleton
    @Provides
    fun providesMoshi(): Moshi {
        return Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
    }

    @Singleton
    @Provides
    fun provideTicketApi(moshi: Moshi): TicketApi {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
            .create(TicketApi::class.java)
    }

    @Provides
    @Singleton
    fun provideTicketDb(@ApplicationContext appContext: Context) =
        Room.databaseBuilder(
            appContext,
            TicketDb::class.java,
            "Ticket.db"
        ).fallbackToDestructiveMigration()
            .build()

    @Provides
    fun provideTicketDao(ticketDb: TicketDb) = ticketDb.ticketDao()
    @Provides
    fun providePrioridadDao(ticketDb: TicketDb) = ticketDb.prioridadDao()
    @Provides
    fun provideSistemaDao(ticketDb: TicketDb) = ticketDb.sistemaDao()
    @Provides
    fun provideUsuarioDao(ticketDb: TicketDb) = ticketDb.productDao()
}