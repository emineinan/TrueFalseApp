package com.example.truefalseapp.ui.theme.game

data class GameUiState(
    val currentQuestion: String = "",
    val correctAnswer: String = "",
    val currentQuestionCount: Int = 1,
    val score: Int = 0,
    val isAnswerWrong: Boolean? = null,
    val isGameOver: Boolean = false
)