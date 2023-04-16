package com.example.scoretable.di

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.scoretable.data.database.DataBase
import com.example.scoretable.data.database.players.PlayerDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RoomModule {

    @Provides
    @Singleton
    fun provideDataBase(@ApplicationContext context: Context): DataBase =
        Room.databaseBuilder(
            context = context.applicationContext,
            klass = DataBase::class.java,
            "ScoreDataBase"
        ).fallbackToDestructiveMigration().build()

    @Provides
    @Singleton
    fun provideScoreDao(dataBase: DataBase):PlayerDao = dataBase.playerDao()
}