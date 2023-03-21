package cm.chettas.wheatherforecast.data.remote

import cm.chettas.wheatherforecast.data.WheatherForecastResponse
import cm.chettas.wheatherforecast.util.Constants
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


interface ForecastService {

    @GET("forecast")
    suspend fun getForecastByState(
        @Query("q") state: String,
        @Query("appid") appId: String = Constants.API_KEY,
        @Query("units") unit: String = "metric",
        @Query("exclude") exclude: String = "current,minutely,hourly,alerts"
    ): Response<WheatherForecastResponse>
}