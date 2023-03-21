package cm.chettas.wheatherforecast.repositories

import android.util.Log
import cm.chettas.wheatherforecast.data.Mapper
import cm.chettas.wheatherforecast.data.States
import cm.chettas.wheatherforecast.data.local.ForecastEntity
import cm.chettas.wheatherforecast.data.local.ForecastLocalDataSource
import cm.chettas.wheatherforecast.data.local.LocalDataSourceUtil
import cm.chettas.wheatherforecast.data.remote.ForecastService
import cm.chettas.wheatherforecast.util.Resource
import kotlinx.coroutines.*
import javax.inject.Inject

class MainRepositoryImpl @Inject constructor(
    private val service: ForecastService,
    private val local: ForecastLocalDataSource,
): MainRepository {

    override suspend fun getForecastByStates(states: List<States>): Resource<List<List<ForecastEntity>>> {
        return try {

            val results = mutableListOf<List<ForecastEntity>>()

            Dispatchers.IO {
                val request = states.map {
                    async { service.getForecastByState(it.name) }
                }
                results.addAll(
                    LocalDataSourceUtil.saveFiveDayForecastWeather(
                        local,
                        request.awaitAll().mapNotNull {
                            it.body()
                        })
                )
            }
            return Resource.Success(results)
        }catch (e: Exception){
            Resource.Error(e.message ?: "Something went wrong")
        }
    }

    override suspend fun getForecastByStatesFromLocal(states: List<States>): Resource<List<List<ForecastEntity>>> {
        return try {

            val results = mutableListOf<List<ForecastEntity>>()

            Dispatchers.IO {
                val request = states.map {
                    val isStatePresent = local.isStatePresent(it.name)
                    if (isStatePresent != null) {
                        async { local.getAllDailyWeathersByStateId(it.name) }
                    } else {
                        async { getForecastByState(it.name) }
                    }

                }
                results.addAll(request.awaitAll().map { it })
            }
            //if local empty get it from network
            if (results.first().isEmpty()) {
                getForecastByStates(states)
            }
            Resource.Success(results)
        }catch (e: Exception){
            Resource.Error(e.message ?: "Something went wrong")
        }
    }

    override suspend fun updateLocal(): Resource<List<List<ForecastEntity>>> {
        if (local.getAllStates().isNotEmpty()){
            return getForecastByStates(Mapper.stateEntityToState(local.getAllStates()))
        }
        return Resource.Error("Already Updated")
    }

    private suspend fun getForecastByState(state: String): List<ForecastEntity> {
        return try {

            val results = mutableListOf<ForecastEntity>()

            Dispatchers.IO {

                val request = async { service.getForecastByState(state) }

                results.addAll(
                    LocalDataSourceUtil.saveSingleStateForecastWeather(
                        local,
                        request.await().body()
                    )
                )
            }
            results
        }catch (e: Exception){
            emptyList()
        }
    }
}