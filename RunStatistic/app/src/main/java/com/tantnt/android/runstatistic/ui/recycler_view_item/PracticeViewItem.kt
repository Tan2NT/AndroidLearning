package com.tantnt.android.runstatistic.ui.recycler_view_item

import com.tantnt.android.runstatistic.R
import com.tantnt.android.runstatistic.models.PRACTICE_TYPE
import com.tantnt.android.runstatistic.models.PracticeModel
import com.tantnt.android.runstatistic.utils.TimeUtils
import com.tantnt.android.runstatistic.utils.around2Place
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item
import kotlinx.android.synthetic.main.fragment_practice_detail.*

/**
 * content class ViewHolder to preparing data to display into the recyclerView
 */

/**
 * View Item detail for each practice
 */
class PracticeViewItem (
    val practiceModel: PracticeModel
) : Item() {
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.apply {
            day_time_text.text = TimeUtils.convertTimeToStringFormat(practiceModel.startTime)
            distance_value.text = practiceModel.distance.around2Place().toString()
            calo_value.text = practiceModel.calo.around2Place().toString()
            speed_value.text = practiceModel.speed.around2Place().toString()
            time_value.text = TimeUtils.convertDutationToFormmated(practiceModel.duration)
            var resId = R.drawable.walking_selected_icon
            when(practiceModel.practiceType) {
                PRACTICE_TYPE.RUNNING -> resId = R.drawable.running_selected_icon
                PRACTICE_TYPE.CYCLING -> resId = R.drawable.cycling_selected_icon
                PRACTICE_TYPE.WALKING -> resId = R.drawable.walking_selected_icon
            }
            practice_type_image.setBackgroundResource(resId)
        }
    }

    override fun getLayout(): Int {
        return R.layout.fragment_practice_detail
    }

    override fun getSpanSize(spanCount: Int, position: Int): Int {
        return spanCount
    }
}

/**
 * get list PracticeModel from list PracticeViewItem
 */
fun List<PracticeViewItem>.asListPracticeModel(): List<PracticeModel> {
    return this.map {
        PracticeModel(
            startTime = it.practiceModel.startTime,
            practiceType = it.practiceModel.practiceType,
            duration = it.practiceModel.duration,
            distance = it.practiceModel.distance,
            calo = it.practiceModel.calo,
            speed = it.practiceModel.speed,
            status = it.practiceModel.status,
            path = it.practiceModel.path)
    }
}
