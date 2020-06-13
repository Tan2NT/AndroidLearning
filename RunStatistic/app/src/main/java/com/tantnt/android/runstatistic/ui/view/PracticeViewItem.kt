package com.tantnt.android.runstatistic.ui.view

import com.tantnt.android.runstatistic.R
import com.tantnt.android.runstatistic.models.PRACTICE_TYPE
import com.tantnt.android.runstatistic.models.PracticeDayInfo
import com.tantnt.android.runstatistic.models.PracticeModel
import com.tantnt.android.runstatistic.utils.TimeUtils
import com.tantnt.android.runstatistic.utils.around2Place
import com.xwray.groupie.ExpandableGroup
import com.xwray.groupie.ExpandableItem
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item
import kotlinx.android.synthetic.main.fragment_practice_detail.*
import kotlinx.android.synthetic.main.header_title.*

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

/**
 * Header Item
 */
class HeaderItem(
    val title: String,
    val description : String
) : Item(), ExpandableItem {

    private lateinit var expandableGroup: ExpandableGroup

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.apply {
            header_title.text = title
            header_description.text = description
            header_arrow.setImageResource(getRotateIconResId())

            header_arrow.setOnClickListener {
                expandableGroup.onToggleExpanded()
                header_arrow.setImageResource(getRotateIconResId())
            }
        }
    }

    override fun getLayout(): Int {
        return R.layout.header_title
    }

    override fun setExpandableGroup(onToggleListener: ExpandableGroup) {
        expandableGroup = onToggleListener
    }

    private fun getRotateIconResId(): Int {
        if (expandableGroup.isExpanded)
            return R.drawable.ic_keyboard_arrow_up_black_24dp
        else
            return R.drawable.ic_keyboard_arrow_down_black_24dp
    }
}

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