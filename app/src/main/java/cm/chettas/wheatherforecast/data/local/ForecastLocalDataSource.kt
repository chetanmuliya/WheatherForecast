package cm.chettas.wheatherforecast.data.local

import javax.inject.Inject


class ForecastLocalDataSource @Inject constructor(
    private val forecastDao: ForecastDao,
    private val stateDao: StateDao,
) {

    fun saveState(stateEnity: StateEnity)= stateDao.insertForecast(stateEnity)

    fun getAllStates(): List<StateEnity> = stateDao.getAllState()

    fun isStatePresent(stateName: String): StateEnity?{
        return stateDao.getState(stateName)
    }

    fun getAllDailyWeathersByStateId(stateName: String): List<ForecastEntity> {
        return forecastDao.getAllDailyWeathersByStateId(stateName)
    }

    suspend fun deleteDailyWeathersBystateIdAndInsert(
        stateName: String,
        forecast: List<ForecastEntity>
    ) = forecastDao.deleteDailyWeathersBySateIdAndInsert(stateName, forecast)

}