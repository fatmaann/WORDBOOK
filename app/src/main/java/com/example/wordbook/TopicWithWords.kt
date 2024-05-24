package com.example.wordbook

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Relation

@Entity(tableName = "topicWithWords")
data class TopicWithWords(
    @Embedded val topic: Topic,
    @Relation(
        parentColumn = "id",
        entityColumn = "topicId"
    )
    val words: List<Word>
)