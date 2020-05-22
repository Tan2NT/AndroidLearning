package com.tantnt.forecast.ui.weather.future.list

import com.tantnt.forecast.R
import com.tantnt.forecast.data.db.unitlocalized.future.list.UnitSpecificsimpleFutureWeatherEntry
import com.tantnt.forecast.internal.glide.GlideApp
import com.tantnt.forecast.ui.weather.current.BASE_ICON_URL
import com.tantnt.forecast.ui.weather.current.ICON_EXTENTION
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.item_future_weather.*
import kotlinx.android.synthetic.main.item_future_weather.imageView_condition_icon
import kotlinx.android.synthetic.main.item_future_weather.textView_condition
import org.threeten.bp.format.DateTimeFormatter
import org.threeten.bp.format.FormatStyle

class FutureWeatherItem (
    val weatherEntry : UnitSpecificsimpleFutureWeatherEntry
) : Item() {
    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.apply {
            textView_condition.text = weatherEntry.description
            updateTemperature()
            updateDate()
            updateWeatherIcon()
        }
    }

    override fun getLayout(): Int {
        return R.layout.item_future_weather
    }

    private fun ViewHolder.updateTemperature(){
        textView_futureItem_temperature.text = weatherEntry.temp.toString() + " °C"
    }

    private fun ViewHolder.updateDate(){
        val dateFommatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)
        textView_date.text = weatherEntry.datetime.format(dateFommatter)
    }

    private fun ViewHolder.updateWeatherIcon(){
        GlideApp.with(this.containerView)
            .load(BASE_ICON_URL + weatherEntry.icon + ICON_EXTENTION)
            .into(imageView_condition_icon)
    }
}