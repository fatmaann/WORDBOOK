package com.example.wordbook

import androidx.room.*

@Dao
interface TopicDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(topic: Topic)

    @Query("SELECT * FROM topics")
    suspend fun getAllTopics(): MutableList<Topic>

    @Query("SELECT * FROM topics WHERE id = :topicId")
    suspend fun getTopicNameById(topicId: Int): Topic?

    @Update
    suspend fun update(topic: Topic)

    @Query("DELETE FROM topics WHERE id = :topicId")
    suspend fun deleteTopic(topicId: Int)
}