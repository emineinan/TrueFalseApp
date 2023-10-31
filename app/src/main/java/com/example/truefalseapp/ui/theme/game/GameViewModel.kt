package com.example.truefalseapp.ui.theme.game

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.truefalseapp.data.MAX_NO_OF_QUESTIONS
import com.example.truefalseapp.data.Question
import com.example.truefalseapp.data.SCORE_INCREASE
import com.example.truefalseapp.data.allQuestions
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class GameViewModel : ViewModel() {
    private val _uiSate = MutableStateFlow(GameUiState())
    val uiState: StateFlow<GameUiState> = _uiSate.asStateFlow()

    private var usedQuestions: MutableList<String> = mutableListOf()
    private lateinit var currentQuestion: Question

    private var userAnswer by mutableStateOf("")

    init {
        resetGame()
    }

    fun resetGame() {
        usedQuestions.clear()
        val question = getQuestion()
        _uiSate.value = GameUiState(
            currentQuestion = question.question,
            correctAnswer = question.answer
        )
    }

    private fun getQuestion(): Question {
        currentQuestion = allQuestions.random()

        if (usedQuestions.contains(currentQuestion.question)) {
            getQuestion()
        } else {
            usedQuestions.add(currentQuestion.question)
        }
        return currentQuestion
    }

    fun updateUserAnswer(answer: String) {
        userAnswer = answer
    }

    fun checkUserAnswer() {
        if (userAnswer == uiState.value.correctAnswer) {
            val updatedScore = _uiSate.value.score.plus(SCORE_INCREASE)
            _uiSate.update { it.copy(isAnswerWrong = false) }
            updateGameScore(updatedScore)
        } else {
            _uiSate.update { it.copy(isAnswerWrong = true) }
        }
    }

    private fun updateGameScore(updatedScore: Int) {
        if (usedQuestions.size == MAX_NO_OF_QUESTIONS) {
            _uiSate.update {
                it.copy(isGameOver = true, score = updatedScore)
            }
        } else {
            _uiSate.update {
                it.copy(score = updatedScore)
            }
        }
    }

    fun goToNextQuestion() {
        if (usedQuestions.size == MAX_NO_OF_QUESTIONS) {
            _uiSate.update { it.copy(isGameOver = true) }
        } else {
            val question = getQuestion()
            _uiSate.update {
                it.copy(
                    currentQuestion = question.question,
                    correctAnswer = question.answer,
                    currentQuestionCount = it.currentQuestionCount.inc()
                )
            }
        }
    }
}