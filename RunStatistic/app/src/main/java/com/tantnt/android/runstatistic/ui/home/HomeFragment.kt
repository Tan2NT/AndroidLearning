package com.tantnt.android.runstatistic.ui.home

import android.Manifest
import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.tantnt.android.runstatistic.R
import com.tantnt.android.runstatistic.models.DAILY_TARGET
import com.tantnt.android.runstatistic.models.PracticeDayInfo
import com.tantnt.android.runstatistic.network.service.TAG
import com.tantnt.android.runstatistic.utils.*
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_select_target_dialog.view.*
import org.threeten.bp.LocalDateTime
import org.threeten.bp.LocalDate

private const val REQUEST_WRITE_EXTERNAL_STORAGE_PERMISSIONS_REQUEST_CODE = 10

class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel

    private var mStepCounted : Int? = 0

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

        Log.i(LOG_TAG, "LocalDate: ${org.threeten.bp.LocalDate.now()} - LocalDateTime: ${LocalDateTime.now()}")

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
                var stepCounted = 0
                var totalDistance = 0.0
                var totalTimeSpent = 0L
                var totalCaloBurned = 0.0

                for(practice in it) {
                    stepCounted += practice.getStepsCounted()
                    totalDistance += practice.distance
                    totalTimeSpent += practice.duration
                    totalCaloBurned += practice.calo
                    Log.i(LOG_TAG, "practice todays : ${practice.toString()}")
                }
                mStepCounted = stepCounted

                // update UI
                setProgressBarStatus(stepCounted)
                total_calo_today_text.text = totalCaloBurned.around2Place().toString()
                total_distance_today_text.text = totalDistance.around2Place().toString()
                total_time_today_text.text = TimeUtils.convertDutationToFormmated(totalTimeSpent).toString()
            }
        })

        /**
         * handle display the best practice and history
         */
        homeViewModel.latest30Practice.observe(viewLifecycleOwner, Observer {
            it?.let {
                /**
                 * get the best practice base on now much Calo is spent
                 */

                // group practice by day
                var groupByDay  = it.groupBy { it -> it.startTime.toLocalDate() }

                // find the best practice day
                var bestPracticeDay =
                    PracticeDayInfo(LocalDate.now(), 0.0, 0L, 0, 0.0)
                groupByDay.forEach {
                    var totalDistance = 0.0
                    var totalTimeSpent = 0L
                    var totalStepCounted = 0
                    var totalCaloBurned = 0.0
                    it.value.forEach { practice ->
                        totalStepCounted += practice.getStepsCounted()
                        totalDistance += practice.distance
                        totalTimeSpent += practice.duration
                        totalCaloBurned += practice.calo
                        Log.i(LOG_TAG, "practice todays : ${practice.toString()}")
                    }
                    if(totalCaloBurned > bestPracticeDay.totalCaloBurned) {
                        bestPracticeDay = PracticeDayInfo(
                            it.key,
                            totalDistance.around2Place(),
                            totalTimeSpent,
                            totalStepCounted,
                            totalCaloBurned.around2Place())
                    }
                }

                // update the best practice day value
                if(!groupByDay.isEmpty()) {
                    bpr_day_text.text = bestPracticeDay.date.toString()
                    bpr_step_text.text = bestPracticeDay.totalStepCounted.toString()
                    bpr_distance_text.text = bestPracticeDay.totalDistance.toString()
                    bpr_energy_text.text = bestPracticeDay.totalCaloBurned.toString()
                }

            }
        })
    }

    fun openSelectDailyTargetDialog() {
        // Inflate the dialog with custom view
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.fragment_select_target_dialog, null)
        val builder = AlertDialog.Builder(requireContext())
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

    fun setProgressBarStatus(stepCounted: Int) {
        val target = SharedPreferenceUtil.getDailyTargetStep(requireContext())
        val completedPercent = (stepCounted * 100) / target
        Log.i(LOG_TAG, "setProgressBarStatu $stepCounted - $target - $completedPercent")
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
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
    }

    // TODO: Step 1.0, Review Permissions: Method requests permissions.
    private fun requestWriteExternalStoragePermission() {
        Log.d(TAG, "requestWriteExternalStoragePermissions ---")
        requestPermissions(
            arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
            REQUEST_WRITE_EXTERNAL_STORAGE_PERMISSIONS_REQUEST_CODE
        )
    }
}
