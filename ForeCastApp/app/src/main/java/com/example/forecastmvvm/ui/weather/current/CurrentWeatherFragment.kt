package com.example.forecastmvvm.ui.weather.current

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.example.forecastmvvm.R
import com.example.forecastmvvm.data.ApixuWeatherApiService
import com.example.forecastmvvm.data.network.ConnectivityInterceptorImpl
import com.example.forecastmvvm.data.network.WeatherNetworkDataSourceImpl
import kotlinx.android.synthetic.main.current_weather_fragment.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.*

class CurrentWeatherFragment : Fragment() {

    companion object {
        fun newInstance() =
            CurrentWeatherFragment()
    }

    private lateinit var viewModel: CurrentWeatherViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.current_weather_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(CurrentWeatherViewModel::class.java)

        // TODO: Use the ViewModel

        // call the current Weather service
        val apiService = ApixuWeatherApiService(ConnectivityInterceptorImpl(this.context!!))

        // create an instance to work with request data
        val weatherNetworkDataSource = WeatherNetworkDataSourceImpl(apiService)

        // handle response data
        weatherNetworkDataSource.downloadCurrentWeather.observe(this, androidx.lifecycle.Observer {
            textView.text = it.location.toString() + "\n" + it.request.toString() + "\n" + it.currentWeatherEntry.toString();
        })

        GlobalScope.launch (Dispatchers.Main) {
            // call the api to get the data
           weatherNetworkDataSource.fetchCurrentWeather("London", "en")

        }
    }

}
