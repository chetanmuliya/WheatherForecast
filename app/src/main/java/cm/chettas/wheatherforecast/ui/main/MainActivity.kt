package cm.chettas.wheatherforecast.ui.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import cm.chettas.wheatherforecast.R
import cm.chettas.wheatherforecast.data.States
import cm.chettas.wheatherforecast.databinding.ActivityMainBinding
import cm.chettas.wheatherforecast.ui.adapter.TemperatureAdapter
import cm.chettas.wheatherforecast.util.Resource
import cm.chettas.wheatherforecast.util.ResultEvent
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by viewModels()
    private lateinit var similarAdapter: TemperatureAdapter
    private lateinit var highestAdapter: TemperatureAdapter
    private lateinit var lowestAdapter: TemperatureAdapter
    private lateinit var binding: ActivityMainBinding
    private val list = listOf(
        States("Maharashtra"),States("Kerala"),
        States("Karnataka"),States("Gujarat"),
        States("Tamil Nadu"),States("Rajasthan"),
        States("Punjab"),States("Haryana"),
        States("Uttar Pradesh"),States("Assam")
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        setUpUi()
        setUpViewModel()
    }

    private fun setUpUi() {
        similarAdapter = TemperatureAdapter()
        binding.rvSimilarTemp.adapter = similarAdapter
        highestAdapter = TemperatureAdapter()
        binding.rvHighestTemp.adapter = highestAdapter
        lowestAdapter = TemperatureAdapter()
        binding.rvLowestTemp.adapter = lowestAdapter
    }

    private fun setUpViewModel() {
        viewModel.getForecastList(list)
        viewModel.forecastList.observe(this,Observer{
            when(it) {
                is Resource.Error -> Log.d("*************", "setUpViewModel: Error ${it.message}")
                is Resource.Loading -> Log.d("*************", "setUpViewModel: ${it.message}")
                is Resource.Success -> Log.d("*************", "setUpViewModel: ${it.message}")
            }
        })
        //setting similar
        viewModel.similarTempList.observe(this, Observer {
            when(it){
                is ResultEvent.Error -> {
                    Toast.makeText(this,it.message,Toast.LENGTH_SHORT).show()
                    hideProgressBar()
                }
                is ResultEvent.Success -> it.data?.let { list ->
                    similarAdapter.addList(list)
                    hideProgressBar()
                }
            }
        })
        viewModel.highestTempInDaywiseList.observe(this, Observer {
            when(it){
                is ResultEvent.Error -> {
                    Toast.makeText(this,it.message,Toast.LENGTH_SHORT).show()
                    hideProgressBar()
                }
                is ResultEvent.Success -> it.data?.let { list ->
                    highestAdapter.addList(list)
                    hideProgressBar()
                }
            }
        })
        viewModel.lowestTempInDaywiseList.observe(this, Observer {
            when(it){
                is ResultEvent.Error -> {
                    Toast.makeText(this,it.message,Toast.LENGTH_SHORT).show()
                    hideProgressBar()
                }
                is ResultEvent.Success -> it.data?.let { list ->
                    lowestAdapter.addList(list)
                    hideProgressBar()
                }
            }
        })
    }

    private fun hideProgressBar() {
        binding.progressBar.visibility = View.GONE
    }
}