package com.example.forecastmvvm.ui.weather.current

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity

import com.example.forecastmvvm.R
import com.example.forecastmvvm.data.ApixuWeatherApiService
import com.example.forecastmvvm.data.network.ConnectivityInterceptorImpl
import com.example.forecastmvvm.data.network.WeatherNetworkDataSourceImpl
import com.example.forecastmvvm.internal.glide.GlideApp
import com.example.forecastmvvm.ui.base.ScopeFragment
import kotlinx.android.synthetic.main.current_weather_fragment.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.internal.Internal.instance
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance
import java.util.*

class CurrentWeatherFragment : ScopeFragment(), KodeinAware {

    override  val kodein by closestKodein()
    private val viewModalFactory: CurrentWeatherViewModalFactory by instance()

//    companion object {
//        fun newInstance() =
//            CurrentWeatherFragment()
//    }

    private lateinit var viewModel: CurrentWeatherViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.current_weather_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Log.i("TDebug", "CurentWeatherFragment onActivityCreated" )
        viewModel = ViewModelProviders.of(this, viewModalFactory)
            .get(CurrentWeatherViewModel::class.java)

       bindUI()

        // call the current Weather service
//        val apiService = ApixuWeatherApiService(ConnectivityInterceptorImpl(this.context!!))
//
//        // create an instance to work with request data
//        val weatherNetworkDataSource = WeatherNetworkDataSourceImpl(apiService)
//
//        // handle response data
//        weatherNetworkDataSource.downloadCurrentWeather.observe(this, androidx.lifecycle.Observer {
//            textView.text = it.location.toString() + "\n" + it.request.toString() + "\n" + it.currentWeatherEntry.toString();
//        })
//
//        GlobalScope.launch (Dispatchers.Main) {
//            // call the api to get the data
//           weatherNetworkDataSource.fetchCurrentWeather("London", "en")
//
//        }
    }

    private fun bindUI() = launch {
        Log.i("TDebug", "CurentWeatherFragment bindUI" )
        val currentWeather = viewModel.weather.await()
        currentWeather.observe(this@CurrentWeatherFragment, androidx.lifecycle.Observer {

            if(it == null){
                Log.i("TDebug", "CurentWeatherFragment bindUI return NULL" )
                return@Observer
            }

            //Log.i("TDebug", "CurentWeatherFragment bindUI has data" + it.weatherDescriptions.toString() )

            group_loading.visibility = View.GONE

            updateLocation("Da Nang")
            updateDateToday()
            updateTemperature(it.temperature, it.feelslike)
            updateOtherInfo(it.precip, it.windSpeed, it.visibility)

            GlideApp.with(this@CurrentWeatherFragment)
                .load(it.weatherIcons[0])
                .into(imageView_condition_icon)

            textView_condition.text = it.weatherDescriptions[0]

        })
    }

    private  fun updateLocation(location: String){
        (activity as? AppCompatActivity)?.supportActionBar?.title = location
    }

    private fun updateDateToday(){
        (activity as? AppCompatActivity)?.supportActionBar?.subtitle = "Today"
    }

    private fun updateTemperature(tempareture: Int, feelLike: Int){
        val tempType = if (viewModel.isImperial) "°F" else "°C"
        textView_temperature.text = "$tempareture$tempType"
        textView_feels_like_temperature.text = "Feels like $feelLike$tempType"
    }

    private fun updateOtherInfo(precip: Int, wind: Int, visibility: Int){
        textView_precipitation.text = "precipitation: $precip mm"
        textView_wind.text = "Wind speed: $wind m/s"
        textView_visibility.text = "Visibitily: $visibility km"
    }
}
