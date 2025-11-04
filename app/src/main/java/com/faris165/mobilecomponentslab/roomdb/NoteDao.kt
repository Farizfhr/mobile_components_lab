package com.faris165.mobilecomponentslab.roomdb


import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {
    @Query("SELECT * FROM Note ORDER BY id DESC")
    fun getAll(): Flow<List<Note>>

    @Insert
    suspend fun insert(n: Note)
}