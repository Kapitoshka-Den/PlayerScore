package com.example.scoretable.screen.scoretable

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.scoretable.data.database.players.PlayerDao
import com.example.scoretable.data.database.players.entites.GameScoreEntity
import com.example.scoretable.data.database.players.entites.PlayerEntity
import com.example.scoretable.domain.models.PlayerPlace
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ScoreTableViewModel @Inject constructor(private val playerDao: PlayerDao) : ViewModel() {

    private val _playerState = MutableStateFlow(ScoreTableState.ScoreState().playersList)
    val playerState = _playerState.asStateFlow()

    private val _scoreState = MutableStateFlow(ScoreTableState.ScoreState().scoreList)
    val scoreState = _scoreState.asStateFlow()

    private val _loadingState = MutableStateFlow(ScoreTableState.LoadingState().isLoad)
    val loadingState = _loadingState.asStateFlow()

    private val _placesState = MutableStateFlow(ScoreTableState.ScoreState().playerPlaces)
    val placesState = _placesState.asStateFlow()

    private val _isNullScoresMoreOne = MutableStateFlow(false)
    val isNullScoresMoreOne = _isNullScoresMoreOne.asStateFlow()

    fun changeScore(gameScore: GameScoreEntity, newScore: String) {
        viewModelScope.launch(Dispatchers.Default) {
            if (newScore.isEmpty())
                playerDao.updateScore(gameScore.copy(Score = null))
            else
                playerDao.updateScore(gameScore.copy(Score = newScore.toInt()))
            _scoreState.update {
                _playerState.value.associate { item -> item.Id to playerDao.getAllScores(item.Id) }
            }
            formPlaces()
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
            _scoreState.update {
                _playerState.value.associate { item -> item.Id to playerDao.getAllScores(item.Id) }
            }
            formPlaces()
            _loadingState.update { false }
        }
    }

    private fun formPlaces() {
        val sortedScores = _scoreState.value.map { item ->
            PlayerPlace(
                Id = item.key,
                Score = item.value.sumOf { items -> items.Score ?: 0 })
        }.sortedByDescending { item -> item.Score }.toMutableList()
        val sortedScoresWithPlaces = sortedScores.map { item -> item.copy(Place = (sortedScores.indexOf(item) + 1)) }.toMutableList()
        _placesState.update {
            sortedScoresWithPlaces
        }
        _isNullScoresMoreOne.update { _scoreState.value.values.filter { item -> item.filter { score -> score.Score != null }.size >= 6 }.size < 7 }
    }
}

