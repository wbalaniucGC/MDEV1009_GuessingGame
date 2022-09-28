package com.mdev.guessinggame

import androidx.lifecycle.ViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.LiveData

class GameViewModel : ViewModel() {
    private val words = listOf("Android", "Activity", "Fragment") // List of available words
    private val secretWord = words.random().uppercase()  // Randomly picked word the user has to guess

    private val _secretWordDisplay = MutableLiveData<String>()  // How the word is displayed
    val secretWordDisplay : LiveData<String> get() = _secretWordDisplay

    private var correctGuesses = "" // Correct letters

    private val _incorrectGuesses = MutableLiveData<String>()   // Incorrect letters
    val incorrectGuesses : LiveData<String> get() = _incorrectGuesses

    private val _livesLeft = MutableLiveData<Int>(8) // Number of lives left
    val livesLeft : LiveData<Int> get() = _livesLeft

    private val _gameOver = MutableLiveData<Boolean>(false)
    val gameOver : LiveData<Boolean> get() = _gameOver

    init {
        _secretWordDisplay.value = deriveSecretWordDisplay()
    }

    private fun deriveSecretWordDisplay() : String {
        var display = ""
        secretWord.forEach {
            display += checkLetter(it.toString())
        }
        return display
    }

    private fun checkLetter(str: String) = when (correctGuesses.contains(str)) {
        true -> str
        false -> "_"
    }

    fun makeGuess(guess:String) {
        if(guess.length == 1) {
            if(secretWord.contains(guess)) {
                correctGuesses += guess
                _secretWordDisplay.value = deriveSecretWordDisplay()
            } else {
                _incorrectGuesses.value += "$guess "
                // livesLeft--
                _livesLeft.value = livesLeft.value?.minus(1)
            }

            if(isWon() || isLost()) _gameOver.value = true
        }
    }

    private fun isWon() = secretWord.equals(secretWordDisplay.value, true)

    private fun isLost() = livesLeft.value?:0 <= 0

    fun wonLostMessage() : String {
        var message = ""
        if(isWon()) message = "You Won!"
        else if (isLost()) message = "You Lost!"

        message += " The word was $secretWord."
        return message
    }

    fun finishGame() {
        _gameOver.value = true
    }
}