package com.example.scoretable.screen.scoretable

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun ScoreTableScreen(viewModel: ScoreTableViewModel = hiltViewModel()) {

    LaunchedEffect(key1 = Unit){
        viewModel.testDao()
    }
    Text(text = "test")
}