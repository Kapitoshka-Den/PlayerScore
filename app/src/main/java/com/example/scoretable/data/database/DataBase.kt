package com.example.scoretable.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.scoretable.data.database.players.PlayerDao
import com.example.scoretable.data.database.players.entites.GameScoreEntity
import com.example.scoretable.data.database.players.entites.PlayerEntity

@Database(entities = [GameScoreEntity::class,PlayerEntity::class], version = 1)
abstract class DataBase: RoomDatabase() {

    abstract fun playerDao():PlayerDao
}