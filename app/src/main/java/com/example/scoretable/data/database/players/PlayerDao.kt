package com.example.scoretable.data.database.players

import androidx.room.*
import com.example.scoretable.data.database.players.entites.GameScoreEntity
import com.example.scoretable.data.database.players.entites.PlayerEntity

@Dao
interface PlayerDao {
    @Query("SELECT * FROM ${PlayerEntity.TABLE_NAME}")
    fun getAllPlayers():List<PlayerEntity>

    @Insert
    fun insertPlayers(players:List<PlayerEntity>)

    @Insert()
    fun insertGameScores(games:List<GameScoreEntity>)

    @Update
    fun updateScore(score: GameScoreEntity)

    @Query("SELECT * FROM ${GameScoreEntity.TABLE_NAME} " +
            "Where ${GameScoreEntity.TABLE_NAME}.${GameScoreEntity.MAIN_PLAYER_COLUMN} == :id " +
            "Order by ${GameScoreEntity.ADD_PLAYER_COLUMN}")
    fun getAllScores(id:Int):List<GameScoreEntity>

}