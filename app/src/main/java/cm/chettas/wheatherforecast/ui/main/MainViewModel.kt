package cm.chettas.wheatherforecast.ui.main

import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.*
import cm.chettas.wheatherforecast.data.HighestTempDay
import cm.chettas.wheatherforecast.data.States
import cm.chettas.wheatherforecast.data.WheatherForecastItem
import cm.chettas.wheatherforecast.data.WheatherForecastResponse
import cm.chettas.wheatherforecast.data.local.ForecastEntity
import cm.chettas.wheatherforecast.repositories.MainRepository
import cm.chettas.wheatherforecast.repositories.workmanager.ForecastWorkManager
import cm.chettas.wheatherforecast.util.*
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import kotlin.math.log

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: MainRepository,
    private val dispatcherProvider: DispatcherProvider,
    @ApplicationContext private val context: Context
): ViewModel() {

    private val highestTempInDayWiseList = MutableList(5) { MutableList(0) { 0 } }
    private val lowestTempInDayWiseList = MutableList(5) { MutableList(0) { 0 } }
    private val result = mutableListOf<HighestTempDay>()

    private val _forecastList = MutableLiveData<Resource<List<WheatherForecastResponse>>>()
    val forecastList : LiveData<Resource<List<WheatherForecastResponse>>>
        get() = _forecastList

    private val _similarTempList = MutableLiveData<ResultEvent<List<HighestTempDay>>>()
    val similarTempList : LiveData<ResultEvent<List<HighestTempDay>>>
        get() = _similarTempList

    private val _highestTempInDaywiseList = MutableLiveData<ResultEvent<List<HighestTempDay>>>()
    val highestTempInDaywiseList : LiveData<ResultEvent<List<HighestTempDay>>>
        get() = _highestTempInDaywiseList

    private val _lowestTempInDaywiseList = MutableLiveData<ResultEvent<List<HighestTempDay>>>()
    val lowestTempInDaywiseList : LiveData<ResultEvent<List<HighestTempDay>>>
        get() = _lowestTempInDaywiseList

    init {
        schedulePeriodicWork()
    }

    fun getForecastList(states: List<States>){
        viewModelScope.launch {
            when(val result = repository.getForecastByStatesFromLocal(states)){
                is Resource.Error -> _forecastList.postValue(Resource.Error(result.message ?: "Error"))
                is Resource.Loading ->  _forecastList.postValue(Resource.Loading())
                is Resource.Success ->  result.data?.let { loadInitialData(it,states) }
            }
        }
    }

    private fun loadInitialData(data: List<List<ForecastEntity>>, states: List<States>) {
        if (data.isNotEmpty()) {
            data.map {
                if (it.isNotEmpty()) {
                    getAvgTemperature(it)
                    getDayWiseHighestTemp(it)
                    getDayWiseLowestTemp(it)
                }
            }
            statesWithHighestTempDayWise(states)
            statesWithLowestTempDayWise(states)
            statesWithSimilarTemp()
        }
    }

    private fun statesWithSimilarTemp() = viewModelScope.launch(dispatcherProvider.io){
        val grouped = result.groupBy { it.temp }
        for ((_, list) in grouped) {
            if (list.size > 1) {
                _similarTempList.postValue(ResultEvent.Success(list))
                return@launch
            }
        }
        _similarTempList.postValue(ResultEvent.Error("Failed to get Similar Temp"))
    }

    private fun getAvgTemperature(list: List<ForecastEntity>) {
        var average = 0
        list.map {
            average += it.temperature.toInt()
        }
        average /= list.size
        result.add(HighestTempDay(day = 0, state = list[0].stateName, temp = average.toString()))
    }

    private fun getDayWiseHighestTemp(list: List<ForecastEntity>) {
        Log.d("************", "getDayWiseHighestTemp: $list")
        var index= 0
        //iterate to 5 day
        getNextFiveDays().forEach {
            //filter to daywise list
            val dayWiseList = list.filter { item ->
                dateFormat(item.timeOfDataForecasted)  == dateFormat(it)
            }
            //get max temp of the day
            val maxTempInDay = dayWiseList.maxOf {
                it.temperature
            }.toInt()
            //store to list by state
            highestTempInDayWiseList[index].add(maxTempInDay)
            index++
        }
    }

    private fun getDayWiseLowestTemp(list: List<ForecastEntity>) {
        var index= 0
        //iterate to 5 day
        getNextFiveDays().forEach {
            //filter to daywise list
            val dayWiseList = list.filter { item ->
                dateFormat(item.timeOfDataForecasted)  == dateFormat(it)
            }
            //get max temp of the day
            val minTempInDay = dayWiseList.minOf {
                it.temperature
            }.toInt()
            //store to list by state
            lowestTempInDayWiseList[index].add(minTempInDay)
            index++
        }
    }

    private fun statesWithHighestTempDayWise(states: List<States>) = viewModelScope.launch(dispatcherProvider.io){
        val list = mutableListOf<HighestTempDay>()
        highestTempInDayWiseList.mapIndexed { index, it ->
            val tepMax = it.maxOrNull()
            val statePos = it.indexOf(tepMax)
            val name = states[statePos].name
            list.add(HighestTempDay(day=index+1, state = name, temp = tepMax.toString()))
            tepMax
        }
        if (list.isNotEmpty())
         _highestTempInDaywiseList.postValue(ResultEvent.Success(list))
        else
         _highestTempInDaywiseList.postValue(ResultEvent.Error("Failed to get Highest Temp Day Wise"))
    }

    private fun statesWithLowestTempDayWise(states: List<States>) = viewModelScope.launch(dispatcherProvider.io){
        val list = mutableListOf<HighestTempDay>()
        lowestTempInDayWiseList.mapIndexed { index, it ->
            val tepMax = it.minOrNull()
            val statePos = it.indexOf(tepMax)
            val name = states[statePos].name
            list.add(HighestTempDay(day=index+1, state = name, temp = tepMax.toString()))
            tepMax
        }
        if (list.isNotEmpty())
            _lowestTempInDaywiseList.postValue(ResultEvent.Success(list))
        else
            _lowestTempInDaywiseList.postValue(ResultEvent.Error("Failed to get Lowest Temp Day Wise"))
    }

    fun schedulePeriodicWork() {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        //30 min request
        val workRequest = PeriodicWorkRequestBuilder<ForecastWorkManager>(15, TimeUnit.MINUTES)
            .setConstraints(constraints)
            .build()

        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            "refresh_data",
            ExistingPeriodicWorkPolicy.KEEP,
            workRequest
        )
    }
}