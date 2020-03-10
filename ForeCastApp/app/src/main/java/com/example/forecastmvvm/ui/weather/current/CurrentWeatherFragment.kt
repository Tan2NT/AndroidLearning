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

const val BASE_ICON_URL : String = "https://www.weatherbit.io/static/img/icons/"
const val ICON_EXTENTION : String = ".png"

class CurrentWeatherFragment : ScopeFragment(), KodeinAware {

    override  val kodein by closestKodein()
    private val viewModalFactory: CurrentWeatherViewModalFactory by instance()

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
    }

    private fun bindUI() = launch {
        Log.i("TDebug", "CurentWeatherFragment bindUI" )

        val currentWeather = viewModel.weather.await()
        currentWeather.observe(this@CurrentWeatherFragment, androidx.lifecycle.Observer {

            if(it == null){
                Log.i("TDebug", "CurentWeatherFragment bindUI return NULL" )
                return@Observer
            }

            Log.i("TDebug", "CurentWeatherFragment bindUI has data" + it.weatherDescriptionWeatherbit.description )

            group_loading.visibility = View.GONE
            updateDateToday()
            updateLocation(it.cityName)
            updateTemperature(it.temp, it.precip)
            updateOtherInfo(it.precip, it.windSpd, it.vis)

            GlideApp.with(this@CurrentWeatherFragment)
                .load(BASE_ICON_URL + it.weatherDescriptionWeatherbit.icon + ICON_EXTENTION)
                .into(imageView_condition_icon)

            textView_condition.text = it.weatherDescriptionWeatherbit.description

        })
    }

    private  fun updateLocation(location: String){
        (activity as? AppCompatActivity)?.supportActionBar?.title = location
    }

    private fun updateDateToday(){
        (activity as? AppCompatActivity)?.supportActionBar?.subtitle = "Today"
    }

    private fun updateTemperature(tempareture: Double, feelLike: Double){
        val tempType = if (viewModel.isImperial) "°F" else "°C"
        textView_temperature.text = "$tempareture$tempType"
        textView_feels_like_temperature.text = "Feels like $feelLike$tempType"
    }

    private fun updateOtherInfo(precip: Double, wind: Double, visibility: Double){
        textView_precipitation.text = "precipitation: $precip mm"
        textView_wind.text = "Wind speed: $wind m/s"
        textView_visibility.text = "Visibitily: $visibility km"
    }
}
