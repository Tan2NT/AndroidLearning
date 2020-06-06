package com.tantnt.android.runstatistic.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.tantnt.android.runstatistic.R
import com.tantnt.android.runstatistic.utils.*

class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_home, container, false)
        homeViewModel.text.observe(viewLifecycleOwner, Observer {
            //textView.text = it
        })

        val k = 12.12345676
        Log.i(LOG_TAG, "test around 2 place k = " + k.around2Place().toString())
        Log.i(LOG_TAG, "test around 3 place k = " + k.around3Place().toString())
        Log.i(LOG_TAG, "kalo burned per minute = " + KcalCaclator.burnedByWalkingPerMinute(70.0, 7.0, 1.70 ))
        Log.i(LOG_TAG, "time duration string format = " + TimeUtils.convertDutationToFormmated(104731).toString())

        return root
    }
}
