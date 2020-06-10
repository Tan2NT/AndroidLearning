package com.tantnt.android.runstatistic.ui.home

import androidx.recyclerview.widget.RecyclerView
import com.tantnt.android.runstatistic.R
import com.tantnt.android.runstatistic.models.PRACTICE_TYPE
import com.tantnt.android.runstatistic.models.PracticeModel
import com.tantnt.android.runstatistic.utils.TimeUtils
import com.tantnt.android.runstatistic.utils.around2Place
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item
import kotlinx.android.synthetic.main.fragment_detail.*
import kotlinx.android.synthetic.main.header_title.*

class PracticeItem (
    val practiceModel: PracticeModel
) : Item() {
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.apply {
            day_time_text.text = practiceModel.startTime.toString()
            distance_value.text = practiceModel.distance.around2Place().toString()
            calo_value.text = practiceModel.calo.around2Place().toString()
            speed_value.text = practiceModel.speed.around2Place().toString()
            time_value.text = TimeUtils.convertDutationToFormmated(practiceModel.duration)
            var resId = R.drawable.walking_selected_icon
            when(practiceModel.practiceType) {
                PRACTICE_TYPE.RUNNING -> resId = R.drawable.running_selected_icon
                PRACTICE_TYPE.CYCLING -> resId = R.drawable.cycling_selected_icon
            }
            practice_type_image.setImageResource(resId)
        }
    }

    override fun getLayout(): Int {
        return R.layout.fragment_detail
    }
}

class HeaderItem(
    val title: String
) : Item() {
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.apply {
            header_title.text = title
        }
    }

    override fun getLayout(): Int {
        return R.layout.header_title
    }

}