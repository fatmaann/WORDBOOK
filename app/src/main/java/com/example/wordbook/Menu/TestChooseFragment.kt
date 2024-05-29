package com.example.wordbook

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import kotlinx.coroutines.launch

class TestChooseFragment : Fragment() {

    private lateinit var topicDao: TopicDao
    private lateinit var wordDao: WordDao
    private lateinit var adapter: TestTopicAdapter
    private lateinit var roomHelper: RoomHelper

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        roomHelper = RoomHelper(requireContext())
        val view = inflater.inflate(R.layout.fragment_test_choose, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerViewTopics: RecyclerView = view.findViewById(R.id.recyclerViewTopics)
        recyclerViewTopics.layoutManager = LinearLayoutManager(context)

        lifecycleScope.launch {
            val topicsFromDb = roomHelper.topicDao.getAllTopics()
            val topics = mutableListOf(TestTopicItem(-1, "Все темы (невыученные)"))
            topics.addAll(topicsFromDb.map { TestTopicItem(it.id, it.name) })

            adapter = TestTopicAdapter(topics)
            recyclerViewTopics.adapter = adapter
            adapter.setSelectedPosition(0)
        }

        val radioGroupTestType: RadioGroup = view.findViewById(R.id.radioGroupTestType)
        val radioKnowWord: RadioButton = view.findViewById(R.id.radioKnowWord)
        val radioWriteWord: RadioButton = view.findViewById(R.id.radioWriteWord)

        radioKnowWord.isChecked = true

        val buttonStartTest: Button = view.findViewById(R.id.buttonStartTest)
        buttonStartTest.setOnClickListener {
            lifecycleScope.launch {
                val selectedTopic = adapter.getSelectedTopic()
                val words: MutableList<Word> = when (selectedTopic?.id) {
                    -1 -> roomHelper.wordDao.getAllUnlearnedWords()
                    1 -> roomHelper.wordDao.getMistakenWords()
                    2 -> roomHelper.wordDao.getLearnedWords()
                    else -> roomHelper.wordDao.getUnlearnedWordsByTopicId(selectedTopic?.id ?: 0)
                }

                if (words.isEmpty()) {
                    Toast.makeText(context, "Нет слов для тестирования", Toast.LENGTH_SHORT).show()
                } else {
                    if (radioKnowWord.isChecked) {
                        val intent = Intent(context, TestKnowledgeActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_NO_HISTORY
                        val wordsJson = Gson().toJson(words)
                        intent.putExtra("wordsJson", wordsJson)
                        startActivity(intent)
                    } else if (radioWriteWord.isChecked) {
                        val intent = Intent(context, TestWritingActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_NO_HISTORY
                        val wordsJson = Gson().toJson(words)
                        intent.putExtra("wordsJson", wordsJson)
                        startActivity(intent)
                    }
                }
            }
        }
    }
}
