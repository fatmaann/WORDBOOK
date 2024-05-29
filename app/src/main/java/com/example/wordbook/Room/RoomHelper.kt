package com.example.wordbook

import android.content.Context
import android.graphics.Color
import androidx.room.Room
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RoomHelper(context: Context) {
    private val db: WordBookDatabase = Room.databaseBuilder(
        context.applicationContext,
        WordBookDatabase::class.java, "word_book_database"
    ).build()

    val topicDao: TopicDao = db.topicDao()
    val wordDao: WordDao = db.wordDao()

    init {
        CoroutineScope(Dispatchers.IO).launch {
            insertInitialData()
        }
    }

    private suspend fun insertInitialData() {
        withContext(Dispatchers.IO) {
            val existingTopics = topicDao.getAllTopics()
            if (existingTopics.isEmpty()) {
                topicDao.insert(Topic(name = "Ошибки", color = 0xFFEB2828.toInt()))
//                Log.i("insert", "I am here 1 ")
                topicDao.insert(Topic(name = "Выученные", color = 0xFFDC880A.toInt()))
//                Log.i("insert", "I am here 2")
                topicDao.insert(Topic(name = "Новая тема", color = 0xFFE5CF06.toInt()))
            }
        }
    }
}
