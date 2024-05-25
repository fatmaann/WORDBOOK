package com.example.wordbook

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface WordDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(word: Word)

    @Query("SELECT * FROM words WHERE topicId = :topicId")
    suspend fun getWordsForTopic(topicId: Int): MutableList<Word>

    @Query("SELECT COUNT(*) FROM words WHERE topicId = :topicId")
    suspend fun getWordCountForTopic(topicId: Int): Int
}

