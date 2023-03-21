package cm.chettas.wheatherforecast.di

import android.content.Context
import androidx.viewbinding.BuildConfig
import cm.chettas.wheatherforecast.data.local.ForecastLocalDataSource
import cm.chettas.wheatherforecast.data.remote.ForecastService
import cm.chettas.wheatherforecast.repositories.MainRepository
import cm.chettas.wheatherforecast.repositories.MainRepositoryImpl
import cm.chettas.wheatherforecast.util.Constants
import cm.chettas.wheatherforecast.util.DispatcherProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    fun getBaseUrl() = Constants.BASE_URL

    @Singleton
    @Provides
    fun getHttpClient(): OkHttpClient {
        return if (BuildConfig.DEBUG){
            val loggingInterceptor = HttpLoggingInterceptor()
            loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
            OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .build()
        }else{
            OkHttpClient
                .Builder()
                .build()
        }
    }

    @Singleton
    @Provides
    fun getRetrofit(okkHttpClient: OkHttpClient, baseUrl: String): Retrofit {
        return  Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(okkHttpClient)
            .build()
    }

    @Singleton
    @Provides
    fun getForecastService(retrofit: Retrofit) =
        retrofit.create(ForecastService::class.java)

    @Singleton
    @Provides
    fun provideDispatchers(): DispatcherProvider = object : DispatcherProvider {
        override val main: CoroutineDispatcher
            get() = Dispatchers.Main
        override val io: CoroutineDispatcher
            get() = Dispatchers.IO
        override val default: CoroutineDispatcher
            get() = Dispatchers.Default
        override val unconfined: CoroutineDispatcher
            get() = Dispatchers.Unconfined
    }
}