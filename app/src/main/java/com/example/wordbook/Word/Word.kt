package com.example.wordbook

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "words")
data class Word(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "nativeWord") val nativeWord: String,
    @ColumnInfo(name = "translation") val translation: String,
    @ColumnInfo(name = "exampleNative") val exampleNative: String,
    @ColumnInfo(name = "exampleTranslation") val exampleTranslation: String,
    @ColumnInfo(name = "topicId") val topicId: Int,
    @ColumnInfo(name = "status") val status: Int = 0,
    @ColumnInfo(name = "isLearned") var isLearned: Boolean = false,
    @ColumnInfo(name = "isMistaken") val isMistaken: Boolean = false
)
