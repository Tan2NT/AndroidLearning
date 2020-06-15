package com.tantnt.android.runstatistic.ui.history

import android.annotation.SuppressLint
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.tantnt.android.runstatistic.R
//import com.tantnt.android.runstatistic.data.database.getDatabase
import com.tantnt.android.runstatistic.models.*
import com.tantnt.android.runstatistic.ui.recycler_view_item.HeaderItem
import com.tantnt.android.runstatistic.ui.recycler_view_item.PracticeViewItem
import com.tantnt.android.runstatistic.ui.recycler_view_item.asListPracticeModel
import com.tantnt.android.runstatistic.utils.around2Place
import com.xwray.groupie.ExpandableGroup
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Section
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.fragment_history.*
import javax.inject.Inject

class HistoryFragment : Fragment() {

    enum class GROUP_TYPE(value : Int) {
        DAY (1),
        MONTH(2)
    }

    @Inject
    internal lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var viewModel: HistoryViewModel

    private var mGroupType = GROUP_TYPE.MONTH

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidSupportInjection.inject(this)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_history, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel =
        ViewModelProviders.of(this, viewModelFactory).get(HistoryViewModel::class.java)

        viewModel.allPractices.observe(viewLifecycleOwner, Observer {
            when(mGroupType) {
                GROUP_TYPE.DAY -> initRecycleViewGroupByDay((it as List<PracticeModel>).asListPracticeItem())
                GROUP_TYPE.MONTH -> initRecycleViewGroupByMonth((it as List<PracticeModel>))
            }
        })
    }

    @SuppressLint("StringFormatMatches")
    private fun initRecycleViewGroupByDay(viewItems: List<PracticeViewItem>) {
        var groupAdapter = GroupAdapter<GroupieViewHolder>().apply {
            spanCount = 1
        }

        // group practice by day
        var groupByDay  = viewItems.groupBy { it -> it.practiceModel.startTime.toLocalDate() }
        // add all practice, group by date
        groupByDay.forEach {

            val currentDayInfo = it.value.asListPracticeModel().getPracticeDayInfo()
            var description =
                getString(R.string.practice_day_description_has_number_activities, it.value.size, currentDayInfo.totalDistance.toFloat())
            if(it.value.size == 1)
                description = getString(R.string.practice_day_description_has_only_1_activity, currentDayInfo.totalDistance.toFloat())

            ExpandableGroup(
                HeaderItem(
                    it.key.toString(),
                    description
                ), true).apply {
                add(Section(it.value))
                groupAdapter.add(this)
            }
        }

        history_recyclerView.apply {
            layoutManager = LinearLayoutManager(this@HistoryFragment.context)
            adapter = groupAdapter
        }
    }

    @SuppressLint("StringFormatMatches")
    private fun initRecycleViewGroupByMonth(viewItems: List<PracticeModel>) {
        var groupAdapter = GroupAdapter<GroupieViewHolder>().apply {
            spanCount = 1
        }

        // group practice by month
        var groupByMonth  = viewItems.groupBy { it -> it.startTime.month }

        // Walk through each Month then add each practice day into the recyclerView
        groupByMonth.forEach { MonthItem ->
            var currentMonthInfo: MutableList<PracticeDayInfo> = mutableListOf()
            var totalDistanceInMonth = 0.0
            var totalPractice = 0
            var year: Int = 0

            val listDayPracticeModel = MonthItem.value
            var groupByDay  = listDayPracticeModel.groupBy { it -> it.startTime.toLocalDate() }

            // Walk through each Day, then sum the info of all practice within this day
            groupByDay.forEach {
                val practiceDayInfo = it.value.getPracticeDayInfo()
                currentMonthInfo.add(practiceDayInfo)
                totalPractice += it.value.size
                totalDistanceInMonth += practiceDayInfo.totalDistance
                year = it.value.get(0).startTime.year
            }

            // get header info
            val title = MonthItem.key.toString() +  ", " + year.toString()
            var description =
                getString(R.string.practice_day_description_has_number_activities, totalPractice, totalDistanceInMonth.around2Place().toFloat())
            if(totalPractice == 1)
                description = getString(R.string.practice_day_description_has_only_1_activity,  totalDistanceInMonth.around2Place().toFloat())

            // Create expandable group by Month (Header) & list practice day of this month (PracticeDayViewItem) into the group Adapter
            ExpandableGroup(
                HeaderItem(
                    title,
                    description
                ), true).apply {
                add(Section(currentMonthInfo.toList().asPracticeDayViewItem()))
                groupAdapter.add(this)
            }
        }

        history_recyclerView.apply {
            layoutManager = LinearLayoutManager(this@HistoryFragment.context)
            adapter = groupAdapter
        }

    }

}
