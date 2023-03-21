package cm.chettas.wheatherforecast.di

import android.app.Application
import android.content.Context
import androidx.room.Room
import cm.chettas.wheatherforecast.data.local.ForecastDatabase
import cm.chettas.wheatherforecast.data.local.ForecastLocalDataSource
import cm.chettas.wheatherforecast.data.remote.ForecastService
import cm.chettas.wheatherforecast.repositories.MainRepository
import cm.chettas.wheatherforecast.repositories.MainRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Singleton
    @Provides
    fun provideDataBase(@ApplicationContext context: Context): ForecastDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            ForecastDatabase::class.java,
            "ForecastDatabase.db"
        ).fallbackToDestructiveMigration().build()
    }

    @Singleton
    @Provides
    fun provideContext(application: Application): Context = application.applicationContext

    @Singleton
    @Provides
    fun provideLocalDataSource(database: ForecastDatabase):
            ForecastLocalDataSource = ForecastLocalDataSource(database.forecastDao(),database.stateDao())

    @Singleton
    @Provides
    fun provideMainRepository(service: ForecastService, local: ForecastLocalDataSource):
            MainRepository = MainRepositoryImpl(service,local)

}