package com.example.wordbook

import android.graphics.Color
import androidx.lifecycle.ViewModel

class TopicViewModel : ViewModel() {
    val topics: MutableList<Pair<String, Int>> = mutableListOf(
        "Ошибки" to Color.WHITE,
        "Выученные" to Color.WHITE
    )
}