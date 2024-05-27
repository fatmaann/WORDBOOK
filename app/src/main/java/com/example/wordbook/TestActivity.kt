package com.example.wordbook

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class TestActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)

        val wordsJson = intent.getStringExtra("wordsJson")
        val words: MutableList<Word> = Gson().fromJson(wordsJson, object : TypeToken<MutableList<Word>>() {}.type)

        println(words)
    }
}
