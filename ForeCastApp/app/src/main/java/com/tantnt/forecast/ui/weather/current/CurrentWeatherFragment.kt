package com.tantnt.forecast.ui.weather.current

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide

import com.tantnt.forecast.R
import com.tantnt.forecast.data.db.entity.LocalDateTimeConverter
import com.tantnt.forecast.ui.base.ScopeFragment
import kotlinx.android.synthetic.main.current_weather_fragment.*
import kotlinx.coroutines.launch
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein

import org.kodein.di.generic.instance
import org.threeten.bp.LocalDate

const val BASE_ICON_URL : String = "https://www.weatherbit.io/static/img/icons/"
const val ICON_EXTENTION : String = ".png"

class CurrentWeatherFragment : ScopeFragment(), KodeinAware {

    private val TAG : String = "TDebug"
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
        Log.d(TAG, "CurentWeatherFragment onActivityCreated" )
        viewModel = ViewModelProviders.of(this, viewModalFactory)
            .get(CurrentWeatherViewModel::class.java)

        viewModel.getCurrentWeather()

       bindUI()

        (activity as? AppCompatActivity)?.supportActionBar?.title = getString(R.string.to_day)
    }

    private fun bindUI() = launch {
        Log.d(TAG, "CurentWeatherFragment bindUI" )

        val currentWeather = viewModel.weather.await()
        currentWeather.observe(this@CurrentWeatherFragment, androidx.lifecycle.Observer {

            if(it == null){
                return@Observer
            }

            group_loading.visibility = View.GONE
            updateDateToday()
            updateLocation(it.cityName)
            updateTemperature(it.temp, it.appTemp)
            updateOtherInfo(it.precip, it.windSpd, it.vis)

            Glide.with(this@CurrentWeatherFragment)
                .load(BASE_ICON_URL + it.weatherIcon + ICON_EXTENTION)
                .into(imageView_condition_icon)

            textView_condition.text = it.weatherDescription

        })
    }

    private  fun updateLocation(location: String){
        (activity as? AppCompatActivity)?.supportActionBar?.title = location
    }

    private fun updateDateToday(){
        val dateString = LocalDateTimeConverter.toDateString(LocalDate.now())!!
        (activity as? AppCompatActivity)?.supportActionBar?.subtitle = getString(R.string.to_day) + " " + dateString
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
