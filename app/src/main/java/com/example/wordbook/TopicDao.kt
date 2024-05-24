package com.example.wordbook

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface TopicDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(topic: Topic)

    @Query("SELECT * FROM topics")
    suspend fun getAllTopics(): MutableList<Topic>
}