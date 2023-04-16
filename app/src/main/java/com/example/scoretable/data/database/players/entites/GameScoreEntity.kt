package com.example.scoretable.data.database.players.entites

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = GameScoreEntity.TABLE_NAME,
    primaryKeys = [GameScoreEntity.MAIN_PLAYER_COLUMN, GameScoreEntity.ADD_PLAYER_COLUMN],
    foreignKeys = [
        ForeignKey(
            entity = PlayerEntity::class,
            parentColumns = arrayOf(PlayerEntity.ID_COLUMN),
            childColumns = arrayOf(GameScoreEntity.MAIN_PLAYER_COLUMN),
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = PlayerEntity::class,
            parentColumns = arrayOf(PlayerEntity.ID_COLUMN),
            childColumns = arrayOf(GameScoreEntity.ADD_PLAYER_COLUMN),
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class GameScoreEntity(
    @ColumnInfo(name = MAIN_PLAYER_COLUMN)
    val MainPlayerId: Int,
    @ColumnInfo(name = ADD_PLAYER_COLUMN)
    val AdditionalPlayerId: Int,
    @ColumnInfo(name = SCORE_COLUMN)
    val Score: Int?,
) {
    companion object {
        const val TABLE_NAME = "GameScore"

        const val MAIN_PLAYER_COLUMN = "MainPlayerId"
        const val ADD_PLAYER_COLUMN = "AdditionalPlayerId"
        const val SCORE_COLUMN = "Score"
    }
}