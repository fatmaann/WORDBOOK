package com.example.wordbook

import androidx.room.*
import com.example.wordbook.Word

@Dao
interface WordDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(word: Word)

    @Query("DELETE FROM words WHERE id = :wordId")
    suspend fun deleteWordById(wordId: Int)

    @Query("UPDATE words SET nativeWord = :nativeWord, translation = :translation, exampleNative = :exampleNative, exampleTranslation = :exampleTranslation, topicId = :topicId, status = :status, isLearned = :isLearned, isMistaken = :isMistaken WHERE id = :wordId")
    suspend fun updateWordById(wordId: Int, nativeWord: String, translation: String, exampleNative: String, exampleTranslation: String, topicId: Int, status: Int, isLearned: Boolean, isMistaken: Boolean)

    @Query("SELECT * FROM words WHERE topicId = :topicId")
    suspend fun getWordsForTopic(topicId: Int): MutableList<Word>

    @Query("SELECT COUNT(*) FROM words WHERE topicId = :topicId")
    suspend fun getWordCountForTopic(topicId: Int): Int

    @Query("SELECT * FROM words WHERE isLearned = 1")
    suspend fun getLearnedWords(): MutableList<Word>

    @Query("SELECT * FROM words WHERE isMistaken = 1")
    suspend fun getMistakenWords(): MutableList<Word>

    @Query("DELETE FROM words WHERE topicId = :topicId")
    suspend fun deleteWordsForTopic(topicId: Int)

    @Query("SELECT * FROM words WHERE id = :wordId")
    suspend fun getWordById(wordId: Int): Word?

    @Query("SELECT * FROM words WHERE topicId = :topicId AND isLearned = 1")
    suspend fun getLearnedWordsByTopicId(topicId: Int): MutableList<Word>

    @Query("SELECT * FROM words WHERE topicId = :topicId AND isLearned = 0")
    suspend fun getUnlearnedWordsByTopicId(topicId: Int): MutableList<Word>
}


