package com.tantnt.android.runstatistic.ui.home

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.facebook.ads.AdSize
import com.facebook.ads.AdView
import com.tantnt.android.runstatistic.R
import com.tantnt.android.runstatistic.models.DAILY_TARGET
import com.tantnt.android.runstatistic.models.PracticeDayInfo
import com.tantnt.android.runstatistic.models.asListPracticeItem
import com.tantnt.android.runstatistic.models.getPracticeDayInfo
import com.tantnt.android.runstatistic.network.service.TAG
import com.tantnt.android.runstatistic.ui.view.HeaderItem
import com.tantnt.android.runstatistic.ui.view.PracticeViewItem
import com.tantnt.android.runstatistic.ui.view.asListPracticeModel
import com.tantnt.android.runstatistic.utils.*
import com.xwray.groupie.ExpandableGroup
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Section
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_select_target_dialog.view.*
import org.threeten.bp.LocalDate


private const val REQUEST_WRITE_EXTERNAL_STORAGE_PERMISSIONS_REQUEST_CODE = 10
private const val ADS_BANNER_PLACEMENT_ID = "261115174996633_261116634996487"

class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel

    private var mStepCounted : Int? = 0

    // Facebook ads
    private var bannerAdView: AdView? = null

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        homeViewModel =
                ViewModelProviders.of(this, HomeViewModelFactory(requireActivity().application) )
                    .get(HomeViewModel::class.java)

        val root = inflater.inflate(R.layout.fragment_home, container, false)

        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        if(writeExternalStorePermissionApproved() == false)
            requestWriteExternalStoragePermission()

        // Register buttons listener
        target_layout.setOnClickListener {
            openSelectDailyTargetDialog()
        }

        // History button click
        btn_history.setOnClickListener {
            it.findNavController()
                .navigate(R.id.action_navigation_home_to_historyFragment)
        }

        // hand touch effect
        btn_history.setOnTouchListener { v, event ->
            when(event.action) {
                MotionEvent.ACTION_DOWN -> {
                    v.background.setColorFilter(Color.GRAY, PorterDuff.Mode.SRC_ATOP)
                    v.invalidate()
                }
                MotionEvent.ACTION_UP -> {
                    v.background.clearColorFilter()
                    v.invalidate()
                }
            }
            false
        }

        // set initialize Target text
        target_step.text = SharedPreferenceUtil.getDailyTargetStep(requireContext()).toString()

        // observer
        /**
         * handle display today practices info
         */
        homeViewModel.todayPractices.observe(viewLifecycleOwner, Observer {
            it?.let {

                /**
                 * Get total Steps/Distance/time spent/calo burned in today
                 */

                if(it.size > 0){
                    val practiceDayInfo = it.getPracticeDayInfo()
                    mStepCounted = practiceDayInfo.totalStepCounted

                    // update UI
                    updateTodayInfo(practiceDayInfo)
                }
            }
        })


        // handle display the best practice and history
        homeViewModel.latest7DaysPractice.observe(viewLifecycleOwner, Observer {
            it?.let {

               if(it.size > 0) {
                   // display the latest practices info recyclerView
                   initRecycleViewAndBestPracticeDay(it.asListPracticeItem())
               }
            }
        })

        // Instantiate an AdView object.
        // NOTE: The placement ID from the Facebook Monetization Manager identifies your App.
        // To get test ads, add IMG_16_9_APP_INSTALL# to your placement id. Remove this when your app is ready to serve real ads.
        bannerAdView = AdView(context, "$ADS_BANNER_PLACEMENT_ID", AdSize.BANNER_HEIGHT_90)

        // Request an ad
        bannerAdView?.let {
            // Add the ad view to activity layout
            banner_container.addView(bannerAdView)
            it.loadAd()
        }
    }

    override fun onDestroy() {
        bannerAdView?.destroy()
        super.onDestroy()
    }

    @SuppressLint("StringFormatMatches")
    private fun initRecycleViewAndBestPracticeDay(viewItems: List<PracticeViewItem>) {
        var groupAdapter = GroupAdapter<GroupieViewHolder>().apply {
            spanCount = 1
        }

        // group practice by day
        var groupByDay  = viewItems.groupBy { it -> it.practiceModel.startTime.toLocalDate() }

        // add all practice, group by date
        // find the best practice day
        var bestPracticeDay =
            PracticeDayInfo(LocalDate.now(), 0.0, 0L, 0, 0.0)
        groupByDay.forEach {

            val currentDayInfo = it.value.asListPracticeModel().getPracticeDayInfo()
            val headerTitle = it.key.toString()
            var description =
                getString(R.string.practice_day_description_has_number_activities, it.value.size, currentDayInfo.totalDistance.toFloat())
            if(it.value.size == 1)
                description = getString(R.string.practice_day_description_has_only_1_activity, currentDayInfo.totalDistance.toFloat())

            ExpandableGroup(
                HeaderItem(
                    headerTitle,
                    description
                ), true).apply {
                add(Section(it.value))
                groupAdapter.add(this)
            }

            // find the best practice day
            if(currentDayInfo.totalCaloBurned >= bestPracticeDay.totalCaloBurned) {
                bestPracticeDay = currentDayInfo
            }
        }

        recyclerView_practices.apply {
            layoutManager = LinearLayoutManager(this@HomeFragment.context)
            adapter = groupAdapter
        }

        // update the best practice day value
        if(!groupByDay.isEmpty()) {
            updateBestPracticeDayInfo(bestPracticeDay)
        }

    }

    fun openSelectDailyTargetDialog() {
        // Inflate the dialog with custom view
        val dialogView = LayoutInflater.from(context).inflate(R.layout.fragment_select_target_dialog, null)
        val builder = AlertDialog.Builder(context)
            .setView(dialogView)

        var target = SharedPreferenceUtil.getDailyTargetStep(requireContext())
        dialogView.textView_adjust_target.setText(target.toString())

        // Show dialog
        val alertDialog = builder.show()

        dialogView.btn_select_target_save.setOnClickListener {
            // save the target into ShareReference key
            onTargetChanged(target)
            alertDialog.dismiss()
        }

        dialogView.daily_target_group.setOnCheckedChangeListener { group, checkedId ->
            when(checkedId) {
                R.id.radio_under_average    -> target = DAILY_TARGET.UNDER_MEDIUM
                R.id.radio_average          -> target = DAILY_TARGET.MEDIUM
                R.id.radio_high_active      -> target = DAILY_TARGET.HIGH_ACTIVE
                R.id.radio_weight_loss      -> target = DAILY_TARGET.WEIGHT_LOSS
                R.id.radio_muscle_gain      -> target = DAILY_TARGET.MUSCLE_GAIN
                R.id.radio_adjusted         -> {
                    try {
                        dialogView.textView_adjust_target.requestFocus()
                    } catch (e: Exception) {
                        Log.e(LOG_TAG, "parse adjusted target failed ${e.toString()}")
                    }
                }
            }
            onTargetChanged(target)
        }

        dialogView.btn_select_target_cancel.setOnClickListener {
            alertDialog.dismiss()
        }

        dialogView.textView_adjust_target.addTextChangedListener {
            try {
                target = it.toString().toInt()
            } catch (e: Exception) {
                Log.e(LOG_TAG, "failed to get target from adjusted text ${e.toString()}")
            }
        }
    }

    /**
     * Update the today practice info
     */
    fun updateTodayInfo(info: PracticeDayInfo) {
        setProgressBarStatus(info.totalStepCounted)
        total_calo_today_text.text = info.totalCaloBurned.around2Place().toString()
        total_distance_today_text.text = info.totalDistance.around2Place().toString()
        total_time_today_text.text = TimeUtils.convertDutationToFormmated(info.totalTimeSpent).toString()
    }

    /**
     * Update the best practice day info
     */
    fun updateBestPracticeDayInfo(info: PracticeDayInfo) {
        bpr_day_text.text = info.date.toString()
        bpr_step_text.text = info.totalStepCounted.toString()
        bpr_distance_text.text = info.totalDistance.around2Place().toString()
        bpr_energy_text.text = info.totalCaloBurned.around2Place().toString()
    }

    /**
     * How much steps counted / targeting step
     */

    fun setProgressBarStatus(stepCounted: Int) {
        val target = SharedPreferenceUtil.getDailyTargetStep(requireContext())
        val completedPercent = (stepCounted * 100) / target
        current_step_text.text = stepCounted.toString()
        progressBar.progress = completedPercent
    }

    /**
     * save new target whenever target is changed
     */
    fun onTargetChanged(newTarget: Int) {
        SharedPreferenceUtil.saveDailyTargetStepPref(requireContext(), newTarget)
        target_step.text = newTarget.toString()
        setProgressBarStatus(mStepCounted!!)
    }

    /**
     * update progress bar status: number of step perform / target step
     */

    // Review Permissions: Method checks if permissions approved.
    private fun writeExternalStorePermissionApproved(): Boolean {
        return PermissionUtils.checkPermission(
            activity?.applicationContext!!,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE) &&
                PermissionUtils.checkPermission(
                    activity?.applicationContext!!,
                    android.Manifest.permission.READ_EXTERNAL_STORAGE)

    }

    // TODO: Step 1.0, Review Permissions: Method requests permissions.
    private fun requestWriteExternalStoragePermission() {
        Log.d(TAG, "requestWriteExternalStoragePermissions ---")
        requestPermissions(
            arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE),
            REQUEST_WRITE_EXTERNAL_STORAGE_PERMISSIONS_REQUEST_CODE
        )
    }
}
