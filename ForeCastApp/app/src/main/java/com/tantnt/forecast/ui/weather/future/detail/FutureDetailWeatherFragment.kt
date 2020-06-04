package com.tantnt.forecast.ui.weather.future.detail

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer

import com.tantnt.forecast.R
import com.tantnt.forecast.data.db.entity.LocalDateTimeConverter
import com.tantnt.forecast.internal.DateNotFoundException
import com.tantnt.forecast.internal.glide.GlideApp
import com.tantnt.forecast.ui.base.ScopeFragment
import com.tantnt.forecast.ui.weather.current.BASE_ICON_URL
import com.tantnt.forecast.ui.weather.current.ICON_EXTENTION
import kotlinx.android.synthetic.main.future_detail_weather_fragment.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.factory
import org.threeten.bp.LocalDate

class FutureDetailWeatherFragment : ScopeFragment(), KodeinAware {

    override val kodein by closestKodein()

    private val viewModelFactoryInstanceFactory
            : ((LocalDate) -> FutureDetailWeatherViewModalFactory) by factory()

    private lateinit var viewModel: FutureDetailWeatherViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.future_detail_weather_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val safeArg = arguments?.let {
            FutureDetailWeatherFragmentArgs.fromBundle(it)
        }

        val date = LocalDateTimeConverter.toDate(safeArg?.dateString) ?: throw DateNotFoundException()

        viewModel = ViewModelProviders.of(this, viewModelFactoryInstanceFactory(date)).get(FutureDetailWeatherViewModel::class.java)
        // TODO: Use the ViewModel

        bindUI()

        (activity as? AppCompatActivity)?.supportActionBar?.title = safeArg?.dateString
    }

    private fun bindUI() = launch(Dispatchers.Main) {
        val futureWeather = viewModel.weather.await()
        Log.i("TDebug", "DetailFragment - bindUI")
        futureWeather.observe(this@FutureDetailWeatherFragment, Observer {
            updateLocation(viewModel.getRequestedCity())
            updateTemperature(it.temp, it.minTemp, it.maxTemp)
            updateDate(it.datetime.toString())
            updateOtherInfo(it.precip, it.windSpd, it.vis)

            GlideApp.with(this@FutureDetailWeatherFragment)
                .load(BASE_ICON_URL + it.icon + ICON_EXTENTION)
                .into(imageView_condition_icon)
        })
    }

    private  fun updateLocation(location: String){
        (activity as? AppCompatActivity)?.supportActionBar?.title = location
    }

    private fun updateDate(dateString : String){
        (activity as? AppCompatActivity)?.supportActionBar?.subtitle = dateString
    }

    private fun updateTemperature(tempareture: Double, minTemp: Double, maxTemp: Double){
        val tempType = if (viewModel.isImperial) "°F" else "°C"
        textView_temperature.text = "$tempareture$tempType"
        textView_min_max_temperature.text = "Min: ${minTemp} ${tempType} - Max: ${maxTemp} ${tempType}"
    }

    private fun updateOtherInfo(precip: Double, wind: Double, visibility: Double){
        textView_precipitation.text = "precipitation: $precip mm"
        textView_wind.text = "Wind speed: $wind m/s"
        textView_visibility.text = "Visibitily: $visibility km"
    }

}
