package com.example.scoretable.data.database.players.entites

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = PlayerEntity.TABLE_NAME)
data class PlayerEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = ID_COLUMN)
    val Id: Int = 0,
    @ColumnInfo(name = NAME_COLUMN)
    val Name: String
){
    companion object{
        const val TABLE_NAME="PlayerEntity"

        const val ID_COLUMN = "Id"
        const val NAME_COLUMN = "Name"
    }
}