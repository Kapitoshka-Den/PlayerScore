package com.example.scoretable.screen.scoretable

import android.util.Log
import android.widget.Toast
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.scoretable.ui.theme.Bronze
import com.example.scoretable.ui.theme.Gold
import com.example.scoretable.ui.theme.Silver

@Composable
fun ScoreTableScreen(viewModel: ScoreTableViewModel = hiltViewModel()) {

    val isLoading = viewModel.loadingState.collectAsState().value
    val playerList = viewModel.playerState.collectAsState().value
    val scoreList = viewModel.scoreState.collectAsState().value
    val placesList = viewModel.placesState.collectAsState().value
    val isNullScoresMoreOne = viewModel.isNullScoresMoreOne.collectAsState().value

    val focusManager = LocalFocusManager.current
    val context = LocalContext.current

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
                        if (scores.MainPlayerId == scores.AdditionalPlayerId)
                            Text(
                                text = "",
                                Modifier
                                    .background(Color.Black)
                                    .width(50.dp)
                                    .height(50.dp)
                            )
                        else
                            TextField(
                                value = score.value,
                                onValueChange = {
                                    if (it.isNotEmpty() && it.toInt() >= 6) {
                                        Toast.makeText(
                                            context,
                                            "Введите значение от 0 до 5",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    } else {
                                        viewModel.changeScore(scores, it)
                                        score.value = it
                                    }
                                },
                                keyboardOptions = KeyboardOptions.Default.copy(
                                    keyboardType = KeyboardType.Number,
                                    imeAction = ImeAction.Next
                                ),
                                keyboardActions = KeyboardActions(
                                    onNext = { focusManager.moveFocus(FocusDirection.Right) }),
                                modifier = Modifier
                                    .border(color = Color.Black, width = 3.dp)
                                    .width(50.dp)
                                    .height(50.dp),
                            )
                    }

                    Text(
                        text = scoreList[playerEntity.Id]!!.sumOf { item -> item.Score ?: 0 }
                            .toString(),
                        modifier = Modifier
                            .border(color = Color.Black, width = 3.dp)
                            .width(150.dp)
                            .height(50.dp)
                            .wrapContentHeight(align = CenterVertically),
                        textAlign = TextAlign.Center,
                        color = if (scoreList[playerEntity.Id]
                                ?.filter { item -> item.Score == null }?.size!! >= 2
                        ) Color.White else Color.Black
                    )
                    Box(
                        modifier = Modifier
                            .border(color = Color.Black, width = 3.dp)
                            .background(
                                if (isNullScoresMoreOne) Color.White
                                else
                                    when (placesList.firstOrNull { item -> item.Id == playerEntity.Id }?.Place) {
                                        1 -> Gold
                                        2 -> Silver
                                        3 -> Bronze
                                        else -> Color.White
                                    }
                            )
                    ) {
                        Text(
                            text = placesList.firstOrNull { item -> item.Id == playerEntity.Id }?.Place.toString(),
                            modifier = Modifier
                                .width(100.dp)
                                .height(50.dp)
                                .wrapContentHeight(align = CenterVertically),
                            textAlign = TextAlign.Center,
                            color = if (isNullScoresMoreOne) Color.White else Color.Black
                        )
                    }
                }
            }
        }
    }
}