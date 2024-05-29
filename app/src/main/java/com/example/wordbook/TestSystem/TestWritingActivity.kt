package com.example.wordbook

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.lifecycleScope
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.launch
import java.util.Collections.shuffle

class TestWritingActivity : AppCompatActivity() {

    private lateinit var words: MutableList<Word>
    private var correctAnswersCount = 0
    private var allAnswersCount = 0
    private var currentIndex = 0
    private lateinit var topicDao: TopicDao
    private lateinit var wordDao: WordDao
    private lateinit var roomHelper: RoomHelper

    private lateinit var topicName: TextView
    private lateinit var textWord: TextView
    private lateinit var textTranslation: TextView
    private lateinit var exampleFront: TextView
    private lateinit var exampleBack: TextView
    private lateinit var editText: EditText
    private lateinit var checkButton: Button
    private lateinit var continueButton: Button
    private lateinit var buttonFinishTest: Button
    private lateinit var cardFront: View
    private lateinit var cardBack: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test_writing)

        roomHelper = RoomHelper(applicationContext)
        wordDao = roomHelper.wordDao

        val wordsJson = intent.getStringExtra("wordsJson")
        words = Gson().fromJson(wordsJson, object : TypeToken<MutableList<Word>>() {}.type)
        words.shuffle()

        topicName = findViewById(R.id.topicName)
        topicName.text = intent.getStringExtra("topicName")

        textWord = findViewById(R.id.textWord)
        textTranslation = findViewById(R.id.textTranslation)
        exampleFront = findViewById(R.id.textExampleFront)
        exampleBack = findViewById(R.id.textExampleBack)
        editText = findViewById(R.id.editText)
        buttonFinishTest = findViewById(R.id.buttonFinishTest)
        checkButton = findViewById(R.id.checkButton)
        continueButton = findViewById(R.id.continueButton)
        cardFront = findViewById(R.id.cardFront)
        cardBack = findViewById(R.id.cardBack)

        continueButton.visibility = View.GONE

        updateCard()

        checkButton.setOnClickListener {
            lifecycleScope.launch {
                if (editText.text.isNotEmpty()) {
                    checkAnswer()
                }
            }
        }

        continueButton.setOnClickListener {
            nextWord()
        }

        buttonFinishTest.setOnClickListener {
            showExitTestDialog()
        }
    }

    private fun updateCard() {
        val word = words[currentIndex]
        textWord.text = word.nativeWord
        textTranslation.text = word.translation
        if (word.exampleNative.isEmpty()) {
            exampleFront.visibility = View.GONE
        } else {
            exampleFront.text = word.exampleNative
            exampleFront.visibility = View.VISIBLE
        }
        if (word.exampleTranslation.isEmpty()) {
            exampleBack.visibility = View.GONE
        } else {
            exampleBack.text = word.exampleTranslation
            exampleBack.visibility = View.VISIBLE
        }
        cardFront.visibility = View.VISIBLE
        cardBack.visibility = View.GONE
        editText.text.clear()
    }

    private suspend fun checkAnswer() {
        val word = words[currentIndex]

        val userAnswer = editText.text.toString().trim().replace("\\s+".toRegex(), "")
        val actualTranslation = word.translation.trim().replace("\\s+".toRegex(), "")

        if (userAnswer.equals(actualTranslation, ignoreCase = true)) {
            word.status++
            if (word.status > 6) {
                word.isLearned = true
                word.status = 0
            }
            word.isMistaken = false
            wordDao.updateWordById(word.id, word.nativeWord, word.translation, word.exampleNative, word.exampleTranslation, word.topicId, word.status, word.isLearned, word.isMistaken)
            correctAnswersCount++
            allAnswersCount++
            cardBack.setBackgroundColor(resources.getColor(android.R.color.holo_green_light))
        } else {
            word.status--
            if (word.status < 0) word.status = 0
            word.isMistaken = true
            wordDao.updateWordById(word.id, word.nativeWord, word.translation, word.exampleNative, word.exampleTranslation, word.topicId, word.status, word.isLearned, word.isMistaken)
            allAnswersCount++
            cardBack.setBackgroundColor(resources.getColor(android.R.color.holo_red_light))
        }

        val animOut = AnimationUtils.loadAnimation(this, R.anim.card_flip_left_out)
        val animIn = AnimationUtils.loadAnimation(this, R.anim.card_flip_right_in)

        cardFront.startAnimation(animOut)
        cardFront.postDelayed({
            cardFront.visibility = View.GONE
            cardBack.visibility = View.VISIBLE
            editText.isEnabled = false
            checkButton.visibility = View.GONE
            continueButton.visibility = View.VISIBLE
            cardBack.startAnimation(animIn)
        }, animOut.duration)
    }

    private fun nextWord() {
        if (currentIndex < words.size - 1) {
            currentIndex++
            val animOutLeft = AnimationUtils.loadAnimation(this, R.anim.slide_out_left)
            val animInRight = AnimationUtils.loadAnimation(this, R.anim.slide_in_right)

            cardBack.startAnimation(animOutLeft)
            cardBack.postDelayed({
                updateCard()
                editText.isEnabled = true
                checkButton.visibility = View.VISIBLE
                continueButton.visibility = View.GONE
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