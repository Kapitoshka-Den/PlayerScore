package com.example.scoretable.screen.scoretable

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.scoretable.data.database.players.PlayerDao
import com.example.scoretable.data.database.players.entites.PlayerEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ScoreTableViewModel @Inject constructor(private val playerDao: PlayerDao) : ViewModel() {

    fun testDao() {
        viewModelScope.launch (Dispatchers.Default){
            playerDao.insertPlayers(
                listOf(
                    PlayerEntity(Name = "Player1")
                )
            )
            val test = playerDao.getAllPlayers()
            Log.e("",test.count().toString())
        }
    }
}