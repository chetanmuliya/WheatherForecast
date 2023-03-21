package cm.chettas.wheatherforecast.data

import cm.chettas.wheatherforecast.data.local.ForecastEntity
import cm.chettas.wheatherforecast.data.local.StateEnity
import cm.chettas.wheatherforecast.util.debug
import java.util.*

object Mapper{

    @JvmStatic
    fun responseToListOfListForecastEntity(response: List<WheatherForecastResponse>): List<List<ForecastEntity>> {
        return response.map {
            it.run {
                responseToListDailyWeatherEntity(it)
            }
        }
    }

    @JvmStatic
    fun responseToListDailyWeatherEntity(response: WheatherForecastResponse): List<ForecastEntity> {
        return response.list?.map {
            it.run {
                ForecastEntity(
                    timeOfDataForecasted = Date(((dt ?: 0) * 1_000)),
                    temperature = main.temp ?: 0.0,
                    stateName = response.city?.name ?: ""
                )
            }
        }.orEmpty().onEach { debug(it.id, "@#@#") }
    }

    @JvmStatic
    fun responseToState(response: WheatherForecastResponse): StateEnity {
        val city = response.city
        return StateEnity(
            id = city?.id ?: Long.MIN_VALUE,
            name = city?.name ?: "",
            country = city?.country ?: "",
            lng = city?.coord?.lon ?: Double.NEGATIVE_INFINITY,
            lat = city?.coord?.lat ?: Double.NEGATIVE_INFINITY,
            zoneId = "" // not need
        )
    }

    fun stateEntityToState(allStates: List<StateEnity>): List<States> {
        return allStates.map {
             it.run {
               States (
                    name = it.name
               )
             }
        }
    }
}