package com.example.truefalseapp.ui.theme.game

import android.app.Activity
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.truefalseapp.R

@Composable
fun GameScreen(modifier: Modifier = Modifier, gameViewModel: GameViewModel = viewModel()) {
    val gameUiState by gameViewModel.uiState.collectAsState()
    val options = listOf("True", "False")
    var selectedOption by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            modifier = modifier.padding(bottom = 16.dp),
            text = stringResource(R.string.true_or_false),
            style = typography.titleLarge,
        )

        Card(
            modifier = modifier
                .padding(16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 5.dp)
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.padding(16.dp)
            ) {
                QuestionCount(modifier, gameUiState, typography)
                Question(modifier, gameUiState, typography)

                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxSize(),
                ) {
                    options.forEach { text ->
                        Text(
                            text = text,
                            style = typography.titleSmall,
                            color = Color.White,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                                .clip(shape = RoundedCornerShape(size = 8.dp))
                                .clickable {
                                    selectedOption = text
                                    gameViewModel.updateUserAnswer(selectedOption)
                                    gameViewModel.checkUserAnswer()
                                }
                                .background(
                                    if (text == selectedOption && gameUiState.isAnswerWrong == true) {
                                        Color.Red
                                    } else if (text == selectedOption && gameUiState.isAnswerWrong == false) {
                                        Color.Green
                                    } else {
                                        Color.LightGray
                                    }
                                )
                                .padding(vertical = 12.dp, horizontal = 16.dp)
                        )
                    }
                }
            }
        }

        OutlinedButton(
            modifier = Modifier
                .padding(top = 8.dp)
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            onClick = {
                gameViewModel.goToNextQuestion()
                selectedOption = ""
            }
        ) {
            Text(
                text = stringResource(R.string.next),
                fontSize = 16.sp
            )
        }

        GameStatus(score = gameUiState.score, modifier = Modifier.padding(20.dp))

        if (gameUiState.isGameOver) {
            FinalScoreDialog(
                score = gameUiState.score,
                onPlayAgain = { gameViewModel.resetGame() }
            )
        }
    }
}

@Composable
private fun Question(
    modifier: Modifier,
    gameUiState: GameUiState,
    typography: Typography
) {
    Text(
        modifier = modifier.padding(bottom = 8.dp),
        text = gameUiState.currentQuestion,
        style = typography.titleMedium
    )
}

@Composable
private fun QuestionCount(
    modifier: Modifier,
    gameUiState: GameUiState,
    typography: Typography
) {
    Text(
        modifier = modifier.padding(bottom = 8.dp),
        text = stringResource(
            R.string.question_count,
            gameUiState.currentQuestionCount
        ),
        textAlign = TextAlign.Center,
        style = typography.titleMedium
    )
}

@Composable
fun GameStatus(score: Int, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier.padding(top = 64.dp)
    ) {
        Text(
            modifier = modifier.padding(2.dp),
            text = stringResource(R.string.score, score),
            style = typography.titleLarge
        )
    }
}

@Composable
private fun FinalScoreDialog(
    score: Int,
    onPlayAgain: () -> Unit,
    modifier: Modifier = Modifier
) {
    val activity = (LocalContext.current as Activity)

    AlertDialog(
        onDismissRequest = {},
        title = { Text(text = stringResource(R.string.congratulations)) },
        text = { Text(text = stringResource(R.string.your_score_is, score)) },
        modifier = modifier,
        dismissButton = {
            TextButton(
                onClick = {
                    activity.finish()
                }
            ) {
                Text(text = stringResource(R.string.exit))
            }
        },
        confirmButton = {
            TextButton(onClick = onPlayAgain) {
                Text(text = stringResource(R.string.play_again))
            }
        }
    )
}
