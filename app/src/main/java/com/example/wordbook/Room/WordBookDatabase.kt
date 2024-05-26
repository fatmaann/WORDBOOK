package com.example.wordbook

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.wordbook.Topic
import com.example.wordbook.TopicDao
import com.example.wordbook.Word
import com.example.wordbook.WordDao


@Database(entities = [Topic::class, Word::class], version = 1, exportSchema = false)
abstract class WordBookDatabase : RoomDatabase() {

    abstract fun topicDao(): TopicDao
    abstract fun wordDao(): WordDao

}
