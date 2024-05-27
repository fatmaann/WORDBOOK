package com.example.wordbook

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.ToggleButton
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.wordbook.*
import kotlinx.coroutines.launch

class WordListFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: WordAdapter
    private lateinit var roomHelper: RoomHelper

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_word_list, container, false)
        roomHelper = RoomHelper(requireContext())
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.recycler_view)
        adapter = WordAdapter(emptyList()) { word ->
            openEditWordFragment(word)
        }
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter

        val topicId = arguments?.getInt("TOPIC_ID") ?: -1
        if (topicId != -1) {
            lifecycleScope.launch {
                val topic = roomHelper.topicDao.getTopicNameById(topicId)
                val topicName = topic?.name ?: "Название темы"
                view.findViewById<TextView>(R.id.topicTitle).text = topicName

                val words = when (topicId) {
                    1 -> roomHelper.wordDao.getMistakenWords()
                    2 -> roomHelper.wordDao.getLearnedWords()
                    else -> emptyList()
                }

                if (topicId == 1 || topicId == 2) {
                    if (words.isNotEmpty()) {
                        adapter.updateWords(words)
                        recyclerView.visibility = View.VISIBLE
                    } else {
                        view.findViewById<TextView>(R.id.noWordsText).visibility = View.VISIBLE
                    }
                } else {
                    var isCheckedLearned = false
                    updateWordList(topicId, isCheckedLearned)
                    val radioGroup: RadioGroup = view.findViewById(R.id.radioGroup)
                    radioGroup.visibility = View.VISIBLE
                    radioGroup.setOnCheckedChangeListener { group, checkedId ->
                        isCheckedLearned = checkedId == R.id.radioButtonLearned
                        updateWordList(topicId, isCheckedLearned)
                    }
                }

                val editTopicIcon = view.findViewById<View>(R.id.edit_topic_icon)
                editTopicIcon.setOnClickListener {
                    openEditTopicFragment(topicId)
                }
            }
        }
    }

    private fun openEditWordFragment(word: Word) {
        val fragment = EditWordFragment().apply {
            arguments = Bundle().apply {
                putInt("WORD_ID", word.id)
            }
        }
        parentFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .addToBackStack(null)
            .commit()
    }

    private fun openEditTopicFragment(topicId: Int) {
        val fragment = EditTopicFragment().apply {
            arguments = Bundle().apply {
                putInt("TOPIC_ID", topicId)
            }
        }
        parentFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .addToBackStack(null)
            .commit()
    }

    private fun updateWordList(topicId: Int, isLearned: Boolean) {
        lifecycleScope.launch {
            val words = if (isLearned) {
                roomHelper.wordDao.getLearnedWordsByTopicId(topicId)
            } else {
                roomHelper.wordDao.getUnlearnedWordsByTopicId(topicId)
            }

            if (words.isNotEmpty()) {
                adapter.updateWords(words)
                recyclerView.visibility = View.VISIBLE
                view?.findViewById<TextView>(R.id.noWordsText)?.visibility = View.GONE
            } else {
                recyclerView.visibility = View.GONE
                view?.findViewById<TextView>(R.id.noWordsText)?.visibility = View.VISIBLE
            }
        }
    }
}
