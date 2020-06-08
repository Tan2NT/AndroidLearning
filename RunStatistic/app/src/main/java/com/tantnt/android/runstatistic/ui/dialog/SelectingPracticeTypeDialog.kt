package com.tantnt.android.runstatistic.ui.dialog

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment

import com.tantnt.android.runstatistic.R
import com.tantnt.android.runstatistic.models.PRACTICE_TYPE
import kotlinx.android.synthetic.main.select_practice_type_dialog.*
import java.util.*

// TODO: Rename parameter arguments, choose names that match

/**
 * A simple [Fragment] subclass.
 * Use the [SelectingPracticeTypeDialog.newInstance] factory method to
 * create an instance of this fragment.
 */

class SelectingPracticeTypeDialog() : DialogFragment() {

    var selectedPracticeType = PRACTICE_TYPE.WALKING

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.select_practice_type_dialog, container, false)
    }

    fun updatePracticeIcons() {
        when (selectedPracticeType) {
            PRACTICE_TYPE.WALKING -> {
                walking_icon.setBackgroundResource(R.drawable.walking_selected_icon)
                walking_text.setTextColor(Color.GREEN)

                running_icon.setBackgroundResource(R.drawable.running_icon)
                running_text.setTextColor(Color.BLACK)
                cycling_icon.setBackgroundResource(R.drawable.cycling_icon)
                cycling_text.setTextColor(Color.BLACK)
            }

            PRACTICE_TYPE.RUNNING -> {
                running_icon.setBackgroundResource(R.drawable.running_selected_icon)
                running_text.setTextColor(Color.GREEN)

                walking_icon.setBackgroundResource(R.drawable.walking_icon)
                walking_text.setTextColor(Color.BLACK)

                cycling_icon.setBackgroundResource(R.drawable.cycling_icon)
                cycling_text.setTextColor(Color.BLACK)
            }

            PRACTICE_TYPE.CYCLING -> {
                cycling_icon.setBackgroundResource(R.drawable.cycling_selected_icon)
                cycling_text.setTextColor(Color.GREEN)

                walking_icon.setBackgroundResource(R.drawable.walking_icon)
                walking_text.setTextColor(Color.BLACK)

                running_icon.setBackgroundResource(R.drawable.running_icon)
                running_text.setTextColor(Color.BLACK)

            }
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        // select Walking as default practice type
        walking_icon.setBackgroundResource(R.drawable.walking_selected_icon)
        walking_text.setTextColor(Color.GREEN)

        // handle icons tapped
        walking_icon.setOnClickListener {
            selectedPracticeType = PRACTICE_TYPE.WALKING
            updatePracticeIcons()
        }
        running_icon.setOnClickListener {
            selectedPracticeType = PRACTICE_TYPE.RUNNING
            updatePracticeIcons()
        }
        cycling_icon.setOnClickListener {
            selectedPracticeType = PRACTICE_TYPE.CYCLING
            updatePracticeIcons()
        }

        // send the practice type back to PracticeFragment
        confirm_button.setOnClickListener {
            val intent = Intent()
            intent.putExtra("practice_type", selectedPracticeType.value)
            targetFragment?.onActivityResult(targetRequestCode, Activity.RESULT_OK, intent)
            this.dismiss()
        }
    }

}
