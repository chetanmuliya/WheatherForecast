package cm.chettas.wheatherforecast.data.local

import android.util.Log
import cm.chettas.wheatherforecast.data.Mapper
import cm.chettas.wheatherforecast.data.WheatherForecastItem
import cm.chettas.wheatherforecast.data.WheatherForecastResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object LocalDataSourceUtil {

    @JvmStatic
    suspend fun saveFiveDayForecastWeather(
        fiveDayForecastLocalDataSource: ForecastLocalDataSource,
        response: List<WheatherForecastResponse>
    ): List<List<ForecastEntity>> = withContext(Dispatchers.IO) {

        val responseToForecastEntityList = Mapper.responseToListOfListForecastEntity(response)

        response.map {
            val state = Mapper.responseToState(it)
            fiveDayForecastLocalDataSource.saveState(state)
            val forecast = Mapper.responseToListDailyWeatherEntity(it)

            fiveDayForecastLocalDataSource.deleteDailyWeathersBystateIdAndInsert(
                forecast = forecast,
                stateName = state.name
            )
        }

        return@withContext responseToForecastEntityList
    }

    @JvmStatic
    suspend fun saveSingleStateForecastWeather(
        fiveDayForecastLocalDataSource: ForecastLocalDataSource,
        response: WheatherForecastResponse?
    ): List<ForecastEntity> = withContext(Dispatchers.IO) {

        if (response == null)
            return@withContext emptyList()

        val state = Mapper.responseToState(response)
        fiveDayForecastLocalDataSource.saveState(state)
        val forecast = Mapper.responseToListDailyWeatherEntity(response)

        fiveDayForecastLocalDataSource.deleteDailyWeathersBystateIdAndInsert(
            forecast = forecast,
            stateName = state.name
        )


        return@withContext forecast
    }

}