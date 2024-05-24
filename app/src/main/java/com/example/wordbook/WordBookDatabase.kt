package com.example.wordbook

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


@Database(entities = [Topic::class, Word::class], version = 1, exportSchema = false)
abstract class WordBookDatabase : RoomDatabase() {

    abstract fun topicDao(): TopicDao
    abstract fun wordDao(): WordDao

}
