package com.tantnt.android.runstatistic.ui.profile

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInResult
import com.google.firebase.auth.FirebaseAuth
import com.tantnt.android.runstatistic.R
import com.tantnt.android.runstatistic.models.UserLevel
import com.tantnt.android.runstatistic.utils.LOG_TAG
import com.tantnt.android.runstatistic.utils.SharedPreferenceUtil
import kotlinx.android.synthetic.main.fragment_profile.*

class ProfileFragment : Fragment() {

    private lateinit var profileViewModel: ProfileViewModel

    private lateinit var auth: FirebaseAuth

    companion object {
        const val SIGN_IN_REQUEST_CODE = 101
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        profileViewModel =
                ViewModelProviders.of(this).get(ProfileViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_profile, container, false)

        auth = FirebaseAuth.getInstance()

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btn_auth_google.setOnClickListener {
            launchSignInFlow()
        }

        observerAuthenticatedState()

        setupProfileLevel()

    }

    private fun setupProfileLevel() {
       context?.let {
           val stepCounted = SharedPreferenceUtil.getTotalStep(it)
           val level = UserLevel.getUserLevel(stepCounted)
           val stepToNextLevel = UserLevel.getStepToNextLevel(stepCounted)
            current_level_text.text = getString(R.string.current_level, level)
           if(stepToNextLevel > 0)
               step_to_next_level_text.text = getString(R.string.step_to_next_level, stepToNextLevel, level + 1)
           else
               step_to_next_level_text.text = getString(R.string.supper_man)
           val currentStepPercent = stepCounted * 100 / UserLevel.getMaxStep()
           progressBar.progress = currentStepPercent.toInt()

       }
    }

    private fun observerAuthenticatedState() {
        profileViewModel.authenticationState.observe(viewLifecycleOwner, Observer { authenticationState ->
            when(authenticationState) {
                ProfileViewModel.AuthenticationState.AUTHENTICATED -> {
                    Log.i(LOG_TAG, "observer AUthentication state ${FirebaseAuth.getInstance().currentUser.toString()}")
                    profile_name.text = FirebaseAuth.getInstance().currentUser?.displayName
                    btn_auth_google.text = getString(R.string.logout)
                    btn_auth_google.setOnClickListener {
                        AuthUI.getInstance().signOut(requireContext())
                    }
                }
                else -> {
                    profile_name.text = ""
                    btn_auth_google.text = getString(R.string.login)
                    btn_auth_google.setOnClickListener {
                        launchSignInFlow()
                    }
                }
            }

        })
    }

    private fun launchSignInFlow() {
        Log.i(LOG_TAG, "launch Sign in flow")
        // Give users the option to sign in / register with their email
        // If users choose to register with their email. the will need to create a password as well
        val providers = arrayListOf(
            AuthUI.IdpConfig.EmailBuilder().build(), AuthUI.IdpConfig.GoogleBuilder().build()
        )

        // Create and launch sign-in intent.
        // Listen tp tje response of this activity with the // SIGN_IN_RESULT_CODE code
        startActivityForResult(
            AuthUI.getInstance().createSignInIntentBuilder().setAvailableProviders(
                    providers
                    ).build(), SIGN_IN_REQUEST_CODE
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == SIGN_IN_REQUEST_CODE) {
            val response = IdpResponse.fromResultIntent(data)
            val signInResult: GoogleSignInResult? = Auth.GoogleSignInApi.getSignInResultFromIntent(data)
            Log.i(LOG_TAG, "sing in result $signInResult")
            signInResult?.let {
                if(it.isSuccess) {
                    val account: GoogleSignInAccount = it.signInAccount!!
                    val url : String = account.photoUrl.toString()
                    Log.i(LOG_TAG, "photo url: $url")
                }
            }
            Log.i(LOG_TAG, "sign in result: $response")
            if (resultCode == Activity.RESULT_OK) {
                // User successfully signed in.
                Log.i(LOG_TAG, "Successfully signed in user ${FirebaseAuth.getInstance().currentUser?.displayName}!")
            } else {
                Log.i(LOG_TAG, "Sign in unsuccessful ${response?.error?.errorCode}")
            }
        }
    }
}
