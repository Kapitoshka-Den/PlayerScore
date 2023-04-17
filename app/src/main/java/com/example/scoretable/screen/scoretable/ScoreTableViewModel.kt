package com.example.scoretable.screen.scoretable

import android.util.Log
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.scoretable.data.database.players.PlayerDao
import com.example.scoretable.data.database.players.entites.GameScoreEntity
import com.example.scoretable.data.database.players.entites.PlayerEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.yield
import javax.inject.Inject

@HiltViewModel
class ScoreTableViewModel @Inject constructor(private val playerDao: PlayerDao) : ViewModel() {

    private val _playerState = MutableStateFlow(ScoreTableState.ScoreState().playersList)
    val playerState = _playerState.asStateFlow()

    private val _scoreState = MutableStateFlow(ScoreTableState.ScoreState().scoreList)
    val scoreState = _scoreState.asStateFlow()

    private val _loadingState = MutableStateFlow(ScoreTableState.LoadingState().isLoad)
    val loadingState = _loadingState.asStateFlow()


    fun changeScore(gameScore: GameScoreEntity, newScore: String) {
        viewModelScope.launch(Dispatchers.Default) {
            if (newScore.isEmpty())
                playerDao.updateScore(gameScore.copy(Score = null))
            else
                playerDao.updateScore(gameScore.copy(Score = newScore.toInt()))
            _scoreState.update {
                _playerState.value.associate { item -> item.Id to playerDao.getAllScores(item.Id) }
            }

        }
    }

    fun loadData() {
        viewModelScope.launch(Dispatchers.Default) {
            if (playerDao.getAllPlayers().isEmpty()) {
                val playerList = buildList {
                    for (i in 1..7) {
                        this.add(PlayerEntity(Name = "Player $i"))
                    }
                }
                playerDao.insertPlayers(playerList)
                playerDao.insertGameScores(buildList {
                    val playersList = playerDao.getAllPlayers()
                    playersList.forEach { mainPlayer ->
                        playersList.forEach { additionalPlayer ->
                            this.add(
                                GameScoreEntity(
                                    MainPlayerId = mainPlayer.Id,
                                    AdditionalPlayerId = additionalPlayer.Id,
                                    Score = null
                                )
                            )
                        }
                    }
                })
            }


            _playerState.update { playerDao.getAllPlayers().toMutableList() }
            _loadingState.update { false }
            _scoreState.update {
                _playerState.value.associate { item -> item.Id to playerDao.getAllScores(item.Id) }
            }
            delay(3000)
        }


    }
}