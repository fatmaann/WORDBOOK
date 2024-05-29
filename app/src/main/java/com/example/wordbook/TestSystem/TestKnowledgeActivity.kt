package com.example.wordbook

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.wordbook.*
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.launch
import java.util.Collections.shuffle

class TestKnowledgeActivity : AppCompatActivity() {

    private lateinit var words: MutableList<Word>
    private var correctAnswersCount = 0
    private var allAnswersCount = 0
    private lateinit var topicDao: TopicDao
    private lateinit var wordDao: WordDao
    private lateinit var roomHelper: RoomHelper
    private var currentIndex = 0

    private lateinit var textWord: TextView
    private lateinit var textTranslation: TextView
    private lateinit var buttonShowTranslation: Button
    private lateinit var buttonKnow: Button
    private lateinit var buttonDontKnow: Button
    private lateinit var buttonFinishTest: Button
    private lateinit var cardFront: View
    private lateinit var cardBack: View
    private lateinit var buttonLayout: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test_knowledge)

        roomHelper = RoomHelper(applicationContext)
        wordDao = roomHelper.wordDao

        val wordsJson = intent.getStringExtra("wordsJson")
        words = Gson().fromJson(wordsJson, object : TypeToken<MutableList<Word>>() {}.type)
        words.shuffle()

        textWord = findViewById(R.id.textWord)
        textTranslation = findViewById(R.id.textTranslation)
        buttonShowTranslation = findViewById(R.id.buttonShowTranslation)
        buttonKnow = findViewById(R.id.buttonKnow)
        buttonDontKnow = findViewById(R.id.buttonDontKnow)
        buttonFinishTest = findViewById(R.id.buttonFinishTest)
        cardFront = findViewById(R.id.cardFront)
        cardBack = findViewById(R.id.cardBack)
        buttonLayout = findViewById(R.id.buttonLayout)

        updateCard()

        buttonShowTranslation.setOnClickListener {
            showTranslation()
        }

        buttonKnow.setOnClickListener {
            lifecycleScope.launch {
                nextWord(true)
            }
        }

        buttonDontKnow.setOnClickListener {
            lifecycleScope.launch {
                nextWord(false)
            }
        }


        buttonFinishTest.setOnClickListener {
            showExitTestDialog()
        }
    }

    private fun updateCard() {
        val word = words[currentIndex]
        textWord.text = word.nativeWord
        textTranslation.text = word.translation
        buttonLayout.visibility = View.GONE
        buttonShowTranslation.visibility = View.VISIBLE
        cardFront.visibility = View.VISIBLE
        cardBack.visibility = View.GONE
    }

    private fun showTranslation() {
        val animOut = AnimationUtils.loadAnimation(this, R.anim.card_flip_left_out)
        val animIn = AnimationUtils.loadAnimation(this, R.anim.card_flip_right_in)

        cardFront.startAnimation(animOut)
        cardFront.postDelayed({
            cardFront.visibility = View.GONE
            cardBack.visibility = View.VISIBLE
            buttonLayout.visibility = View.VISIBLE
            buttonShowTranslation.visibility = View.GONE
            cardBack.startAnimation(animIn)
        }, animOut.duration)
    }

    private suspend fun nextWord(isKnown: Boolean) {
        val word = words[currentIndex]

        if (isKnown) {
            word.status++
            if (word.status > 6) {
                word.isLearned = true
                word.status = 0
            }
            word.isMistaken = false
            wordDao.updateWordById(word.id, word.nativeWord, word.translation, word.exampleNative, word.exampleTranslation, word.topicId, word.status, word.isLearned, word.isMistaken)
            correctAnswersCount++
            allAnswersCount++
        } else {
            word.status--
            if (word.status < 0) word.status = 0
            word.isMistaken = true
            wordDao.updateWordById(word.id, word.nativeWord, word.translation, word.exampleNative, word.exampleTranslation, word.topicId, word.status, word.isLearned, word.isMistaken)
            allAnswersCount++
        }

        if (currentIndex < words.size - 1) {
            currentIndex++
            val animOutLeft = AnimationUtils.loadAnimation(this, R.anim.slide_out_left)
            val animInRight = AnimationUtils.loadAnimation(this, R.anim.slide_in_right)

            cardBack.startAnimation(animOutLeft)
            cardBack.postDelayed({
                updateCard()
                cardFront.startAnimation(animInRight)
            }, animOutLeft.duration)
        } else {
            val intent = Intent(this, MainActivity::class.java).apply {
                putExtra("correctAnswers", correctAnswersCount)
                putExtra("totalQuestions", allAnswersCount)
                flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
            }
            startActivity(intent)
            finish()
        }
    }

    private fun showExitTestDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Выход из теста")
        builder.setMessage("Вы уверены, что хотите завершить тест?")
        builder.setPositiveButton("Да") { dialog, _ ->
            val intent = Intent(this, MainActivity::class.java).apply {
                putExtra("correctAnswers", correctAnswersCount)
                putExtra("totalQuestions", allAnswersCount)
                flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
            }
            startActivity(intent)
            finish()
            dialog.dismiss()
        }
        builder.setNegativeButton("Нет") { dialog, _ ->
            dialog.dismiss()
        }
        val dialog = builder.create()
        dialog.show()
    }
}
