package cm.chettas.wheatherforecast.repositories

import cm.chettas.wheatherforecast.data.States
import cm.chettas.wheatherforecast.data.WheatherForecastResponse
import cm.chettas.wheatherforecast.data.local.ForecastEntity
import cm.chettas.wheatherforecast.util.Resource
import kotlinx.coroutines.Deferred

interface MainRepository {
    suspend fun getForecastByStates(states: List<States>): Resource<List<List<ForecastEntity>>>
    suspend fun getForecastByStatesFromLocal(states: List<States>): Resource<List<List<ForecastEntity>>>
    suspend fun updateLocal(): Resource<List<List<ForecastEntity>>>
}