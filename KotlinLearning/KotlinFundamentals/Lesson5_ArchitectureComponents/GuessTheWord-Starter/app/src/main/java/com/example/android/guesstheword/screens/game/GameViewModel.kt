package com.example.android.guesstheword.screens.game

import android.os.CountDownTimer
import android.text.format.DateUtils
import android.util.Log
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.example.android.guesstheword.R
import kotlinx.android.synthetic.main.game_fragment.*
import java.util.*
import kotlin.random.Random

class GameViewModel : ViewModel() {

    // The current word
    private val _word = MutableLiveData<String>()
    val word : LiveData<String>
        get() = _word

    val currentHintWord = Transformations.map(word) {
        val lenght  = word.value.toString().length
        val ranPos = Random.nextInt(lenght - 1)
        val stringTemplate = "Current word has %d letter \n The letter at position %d is %s"
        String.format(stringTemplate, word.value.toString().length,  ranPos, word.value.toString().get(ranPos))
    }

    // The current score
    private val _score = MutableLiveData<Int>()
    val score: LiveData<Int>
        get() = _score

    // The game finish
    private val _isGameFinished = MutableLiveData<Boolean>()
    val evenGameFinish: LiveData<Boolean>
        get() = _isGameFinished

    // The list of words - the front of the list is the next word to guess
    lateinit var wordList: MutableList<String>
	
    // Countdown time
    private val _currentTime = MutableLiveData<Long>()
    val currentTime: LiveData<Long>
        get() = _currentTime

    // The String version of the current time
    val currentTimeString = Transformations.map(currentTime) {
        DateUtils.formatElapsedTime(it)
    }

    private lateinit var timer : CountDownTimer

    companion object {
        // time when the game is over
        private const val DONE = 0L

        // Countdown time interval
        private const val ONE_SECOND = 1000L

        // Total time for the game
        private const val COUNTDOWN_TIME = 60000L
    }

    init {
        _word.value = ""
        _score.value = 0
        _isGameFinished.value = false

        //create a timer which triggers the end of the game when it finishes
        timer = object : CountDownTimer(COUNTDOWN_TIME, ONE_SECOND) {

            override fun onTick(milisecondUntilFinished: Long) {
                _currentTime.value = milisecondUntilFinished/ ONE_SECOND
            }

            override fun onFinish() {
                _currentTime.value = DONE
                onGameFinished()
            }
        }

        timer.start()

        resetList()
        nextWord()
    }

    override fun onCleared() {
        super.onCleared()

        // Cancel the timer
        timer.cancel()
    }

    /**
     * Resets the list of words and randomizes the order
     */
    private fun resetList() {
        wordList = mutableListOf(
                "queen",
                "hospital",
                "basketball",
                "cat",
                "change",
                "snail",
                "soup",
                "calendar",
                "sad",
                "desk",
                "guitar",
                "home",
                "railway",
                "zebra",
                "jelly",
                "car",
                "crow",
                "trade",
                "bag",
                "roll",
                "bubble"
        )
        wordList.shuffle()
    }

    /**
     * Moves to the next word in the list
     */
    private fun nextWord() {
        if (!wordList.isEmpty()) {
            //Select and remove a word from the list
            _word.value = wordList.removeAt(0)
        }else{
            resetList()
        }
    }

    /** Methods for buttons presses **/

    fun onSkip() {
        _score.value = (score.value)?.minus(1)
        nextWord()
    }

    fun onCorrect() {
        _score.value = (score.value)?.plus(1)
        nextWord()
    }

    /* Method for the game completed event */
    fun onGameFinished() {
        _isGameFinished.value = true
    }

    fun onGameFinishCompleted() {
        _isGameFinished.value = false
    }
}