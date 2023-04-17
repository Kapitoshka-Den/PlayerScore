package com.example.scoretable.screen.scoretable

import android.util.Log
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.focusTarget
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun ScoreTableScreen(viewModel: ScoreTableViewModel = hiltViewModel()) {

    val isLoading = viewModel.loadingState.collectAsState().value
    val playerList = viewModel.playerState.collectAsState().value
    val scoreList = viewModel.scoreState.collectAsState().value
    val focusManager = LocalFocusManager.current

    LaunchedEffect(key1 = Unit) {
        viewModel.loadData()
    }
    if (isLoading) {
        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            CircularProgressIndicator(
                modifier = Modifier.size(size = 64.dp),
                color = Color.Blue,
                strokeWidth = 6.dp
            )
        }

    } else {
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .horizontalScroll(
                    rememberScrollState()
                )
        ) {
            Row() {
                Text(
                    text = "",
                    modifier = Modifier
                        .border(color = Color.Black, width = 3.dp)
                        .wrapContentHeight(align = CenterVertically)
                        .width(100.dp)
                        .height(50.dp),
                )
                Text(
                    text = "", modifier = Modifier
                        .border(color = Color.Black, width = 3.dp)
                        .width(50.dp)
                        .height(50.dp)
                        .wrapContentHeight(align = CenterVertically)
                )
                for (i in 1..playerList.size) {
                    Text(
                        text = i.toString(),
                        modifier = Modifier
                            .border(color = Color.Black, width = 3.dp)
                            .width(50.dp)
                            .height(50.dp)
                            .wrapContentHeight(align = CenterVertically),
                        textAlign = TextAlign.Center,
                    )
                }
                Text(
                    text = "Сумма очков",
                    modifier = Modifier
                        .border(color = Color.Black, width = 3.dp)
                        .width(150.dp)
                        .height(50.dp)
                        .wrapContentHeight(align = CenterVertically),
                    textAlign = TextAlign.Center,
                )
                Text(
                    text = "Место",
                    modifier = Modifier
                        .border(color = Color.Black, width = 3.dp)
                        .width(100.dp)
                        .height(50.dp)
                        .wrapContentHeight(align = CenterVertically),
                    textAlign = TextAlign.Center,
                )
            }
            playerList.forEachIndexed { index, playerEntity ->
                Row() {
                    Text(
                        text = playerEntity.Name, modifier = Modifier
                            .border(color = Color.Black, width = 3.dp)
                            .width(100.dp)
                            .height(50.dp)
                            .wrapContentHeight(align = CenterVertically),
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = (index + 1).toString(), modifier = Modifier
                            .border(color = Color.Black, width = 3.dp)
                            .width(50.dp)
                            .height(50.dp)
                            .wrapContentHeight(align = CenterVertically),
                        textAlign = TextAlign.Center
                    )
                    scoreList.get(playerEntity.Id)?.forEach { scores ->
                        val score = remember {
                            mutableStateOf(if (scores.Score == null) "" else scores.Score.toString())
                        }

                        TextField(
                            value = score.value,
                            onValueChange = {
                                viewModel.changeScore(scores, it)
                                score.value = it
                            },
                            colors = TextFieldDefaults.textFieldColors(
                                backgroundColor = if (scores.MainPlayerId == scores.AdditionalPlayerId) Color.Black else Color.Transparent
                            ),
                            keyboardOptions = KeyboardOptions.Default.copy(
                                keyboardType = KeyboardType.Number,
                                imeAction = ImeAction.Next
                            ),
                            keyboardActions = KeyboardActions(
                                onNext = { focusManager.moveFocus(FocusDirection.Right) }),
                            readOnly = (scores.MainPlayerId == scores.AdditionalPlayerId),
                            modifier = Modifier
                                .border(color = Color.Black, width = 3.dp)
                                .width(50.dp)
                                .height(50.dp),
                        )
                    }

                    Text(
                        text = scoreList[playerEntity.Id]!!.sumOf { item -> item.Score ?: 0 }.toString(),
                        modifier = Modifier
                            .border(color = Color.Black, width = 3.dp)
                            .width(150.dp)
                            .height(50.dp)
                            .wrapContentHeight(align = CenterVertically),
                        textAlign = TextAlign.Center,
                        color = if(scoreList[playerEntity.Id]
                                ?.filter { item -> item.Score == null }?.size!! >= 2) Color.White else Color.Black
                    )
                    Log.e(playerEntity.Id.toString(),(scoreList[playerEntity.Id]
                            ?.filter { item -> item.Score == null }?.size.toString()))
                    Text(
                        text = "", modifier = Modifier
                            .border(color = Color.Black, width = 3.dp)
                            .width(100.dp)
                            .height(50.dp)
                            .wrapContentHeight(align = CenterVertically),
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}