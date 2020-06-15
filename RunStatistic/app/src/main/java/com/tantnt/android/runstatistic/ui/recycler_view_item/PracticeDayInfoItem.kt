package com.tantnt.android.runstatistic.ui.recycler_view_item

import com.tantnt.android.runstatistic.R
import com.tantnt.android.runstatistic.models.PracticeDayInfo
import com.tantnt.android.runstatistic.utils.TimeUtils
import com.tantnt.android.runstatistic.utils.around2Place
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item
import kotlinx.android.synthetic.main.fragment_practice_detail.*

/**
* PracticeDayViewItem - each item content info of all practices within a day
*/

class PracticeDayViewItem (
    val practiceDayInfo: PracticeDayInfo
) : Item() {
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.apply {
            day_time_text.text = practiceDayInfo.date.toString()
            distance_value.text = practiceDayInfo.totalDistance.around2Place().toString()
            calo_value.text = practiceDayInfo.totalCaloBurned.around2Place().toString()
            // replace speed by steps
            speed_value.text = practiceDayInfo.totalStepCounted.toString()
            speed_icon.setBackgroundResource(R.drawable.foot)
            speed_title.text = speed_title.context.getString(R.string.steps)
            time_value.text = TimeUtils.convertDutationToFormmated(practiceDayInfo.totalTimeSpent)
            practice_type_image.setBackgroundResource(R.drawable.road_icon)
        }
    }

    override fun getLayout(): Int {
        return R.layout.fragment_practice_detail
    }
}