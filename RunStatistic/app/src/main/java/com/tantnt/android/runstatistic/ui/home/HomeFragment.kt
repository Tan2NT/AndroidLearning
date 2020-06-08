package com.tantnt.android.runstatistic.ui.home

import android.Manifest
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.tantnt.android.runstatistic.R
import com.tantnt.android.runstatistic.network.service.TAG
import com.tantnt.android.runstatistic.utils.*

private const val REQUEST_WRITE_EXTERNAL_STORAGE_PERMISSIONS_REQUEST_CODE = 10

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

        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        if(writeExternalStorePermissionApproved() == false)
            requestWriteExternalStoragePermission()
    }

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
