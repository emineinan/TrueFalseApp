package com.example.truefalseapp

import com.example.truefalseapp.data.MAX_NO_OF_QUESTIONS
import com.example.truefalseapp.data.SCORE_INCREASE
import com.example.truefalseapp.ui.theme.game.GameViewModel
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class GameViewModelTest {
    private val gameViewModel = GameViewModel()

    @Test
    fun gameViewModel_Initialization_FirstQuestionLoaded() {
        val gameUiState = gameViewModel.uiState.value

        assertFalse(gameUiState.currentQuestion.isEmpty())
        assertFalse(gameUiState.correctAnswer.isEmpty())
        assertTrue(gameUiState.currentQuestionCount == 1)
        assertTrue(gameUiState.score == 0)
        assertTrue(gameUiState.isAnswerWrong == null)
        assertFalse(gameUiState.isGameOver)
    }


    @Test
    fun gameViewModel_AnsweredCorrectly_ScoreUpdated() {
        var gameUiState = gameViewModel.uiState.value

        gameViewModel.updateUserAnswer(gameUiState.correctAnswer)
        gameViewModel.checkUserAnswer()

        gameUiState = gameViewModel.uiState.value

        assertEquals(false, gameUiState.isAnswerWrong)
        assertEquals(SCORE_INCREASE, gameUiState.score)
    }


    @Test
    fun gameViewModel_AnsweredIncorrectly_ScoreNotUpdated() {
        val incorrectAnswer = "and"

        gameViewModel.updateUserAnswer(incorrectAnswer)
        gameViewModel.checkUserAnswer()

        val gameUiState = gameViewModel.uiState.value

        assertEquals(true, gameUiState.isAnswerWrong)
        assertEquals(0, gameUiState.score)
    }


    @Test
    fun gameViewModel_QuestionSolved_QuestionCountIncreased() {
        var gameUiState = gameViewModel.uiState.value

        gameViewModel.updateUserAnswer(gameUiState.correctAnswer)
        gameViewModel.checkUserAnswer()

        gameUiState = gameViewModel.uiState.value
        val lastQuestionCount = gameUiState.currentQuestionCount

        gameViewModel.goToNextQuestion()
        gameUiState = gameViewModel.uiState.value

        assertEquals(lastQuestionCount + 1, gameUiState.currentQuestionCount)
    }


    @Test
    fun gameViewModel_AllQuestionSolved_UiStateUpdatedCorrectly() {
        var expectedScore = 0
        var gameUiState = gameViewModel.uiState.value

        repeat(MAX_NO_OF_QUESTIONS) {
            expectedScore += SCORE_INCREASE

            gameViewModel.updateUserAnswer(gameUiState.correctAnswer)
            gameViewModel.checkUserAnswer()
            gameViewModel.goToNextQuestion()

            gameUiState = gameViewModel.uiState.value

            assertEquals(expectedScore, gameUiState.score)
        }

        assertEquals(MAX_NO_OF_QUESTIONS, gameUiState.currentQuestionCount)
        assertTrue(gameUiState.isGameOver)
    }
}