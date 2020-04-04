package com.example.aboutme

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        done_button.setOnClickListener {
            addNickname(it)
            hideKeyboard(it)
        }

        nickname_text.setOnClickListener {
            updateNicknameText(it)
        }
    }

    private fun updateNicknameText(view: View){
        nickname_edit.visibility = View.VISIBLE
        nickname_edit.requestFocus()
        showKeyboard(nickname_edit)
        showKeyboard(nickname_edit)

        done_button.visibility = View.VISIBLE
        nickname_text.visibility = View.GONE

    }

    private fun addNickname(view : View){
        nickname_text.text = nickname_edit.text
        nickname_text.visibility = View.VISIBLE
        done_button.visibility = View.GONE
        nickname_edit.visibility = View.GONE
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
