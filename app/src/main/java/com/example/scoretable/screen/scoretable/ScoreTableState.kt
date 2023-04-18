package com.example.scoretable.screen.scoretable

import com.example.scoretable.data.database.players.entites.GameScoreEntity
import com.example.scoretable.data.database.players.entites.PlayerEntity
import com.example.scoretable.domain.models.PlayerPlace

sealed class ScoreTableState {
    data class ScoreState(
        val scoreList: Map<Int,List<GameScoreEntity>> = mapOf(),
        val playersList: MutableList<PlayerEntity> = mutableListOf(),
        val playerPlaces: MutableList<PlayerPlace> = mutableListOf()
    )

    data class LoadingState(val isLoad: Boolean = true)
}