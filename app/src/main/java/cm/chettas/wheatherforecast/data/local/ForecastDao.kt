package cm.chettas.wheatherforecast.data.local

import androidx.room.*

@Dao
interface ForecastDao {

    @Query("SELECT * FROM forecast WHERE state_name = :stateName ORDER BY data_time")
    fun getAllDailyWeathersByStateId(stateName: String): List<ForecastEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertForecast(forecast: ForecastEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertForecast(weathers: List<ForecastEntity>)

    @Query("DELETE FROM forecast WHERE state_name = :stateName")
    fun deleteAllDailyWeathersByStateId(stateName: String)

    @Transaction
    suspend fun deleteDailyWeathersBySateIdAndInsert(stateName: String, weathers: List<ForecastEntity>) {
        //deleteAllDailyWeathersByStateId(stateName)
        insertForecast(weathers)
    }
}