package com.example.aboutme

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.databinding.DataBindingUtil
import com.example.aboutme.databinding.ActivityMainBinding
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding

    private val myName: MyName = MyName("HOANG NGOC TAN")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        binding.myName = myName

        binding.doneButton.setOnClickListener {
            addNickname(it)
            hideKeyboard(it)
        }

        binding.nicknameText.setOnClickListener {
            updateNicknameText(it)
        }
    }

    private fun updateNicknameText(view: View){
        binding.nicknameEdit.visibility = View.VISIBLE
        binding.nicknameEdit.requestFocus()
        showKeyboard(binding.nicknameEdit)
        showKeyboard(binding.nicknameEdit)

        binding.doneButton.visibility = View.VISIBLE
        binding.nicknameText.visibility = View.GONE

    }

    private fun addNickname(view : View){
        binding.apply {
            myName?.nickName = binding.nicknameEdit.text.toString()
            invalidateAll()
        }

        binding.nicknameText.visibility = View.VISIBLE
        binding.doneButton.visibility = View.GONE
        binding.nicknameEdit.visibility = View.GONE
    }

    private fun showKeyboard(view : View){
        // Show the keyboard.
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(view, 0)
    }

    private fun hideKeyboard(view : View){
        // Hide the keyboard.
        val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }
}
