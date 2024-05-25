package com.example.wordbook

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

class AddWordFragment : Fragment() {

    private lateinit var wordNative: EditText
    private lateinit var wordTranslation: EditText
    private lateinit var exampleNative: EditText
    private lateinit var exampleTranslation: EditText
    private lateinit var topicSpinner: Spinner
    private lateinit var saveButton: Button
    private lateinit var roomHelper: RoomHelper

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_add_word, container, false)

        wordNative = view.findViewById(R.id.word_native)
        wordTranslation = view.findViewById(R.id.word_translation)
        exampleNative = view.findViewById(R.id.example_native)
        exampleTranslation = view.findViewById(R.id.example_translation)
        topicSpinner = view.findViewById(R.id.topic_spinner)
        saveButton = view.findViewById(R.id.save_button)

        roomHelper = RoomHelper(requireContext())

        lifecycleScope.launch {
            val topics = roomHelper.topicDao.getAllTopics()
            val topicPairs = topics.map { it.name to it.id }
            val adapter = object : ArrayAdapter<Pair<String, Int>>(requireContext(), android.R.layout.simple_spinner_item, topicPairs) {
                override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                    val view = super.getView(position, convertView, parent)
                    (view as TextView).text = getItem(position)?.first
                    return view
                }

                override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
                    val view = super.getDropDownView(position, convertView, parent)
                    (view as TextView).text = getItem(position)?.first
                    return view
                }
            }
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            topicSpinner.adapter = adapter
        }

        saveButton.setOnClickListener {
            val selectedTopicPair = topicSpinner.selectedItem as Pair<String, Int>
            val nativeWord = wordNative.text.toString()
            val translation = wordTranslation.text.toString()
            val exampleNative = exampleNative.text.toString()
            val exampleTranslation = exampleTranslation.text.toString()

            if (nativeWord.length > 40 || translation.length > 40 || exampleNative.length > 40 || exampleTranslation.length > 40) {
                Toast.makeText(requireContext(), "Ни одно из полей не должно превышать 40 символов", Toast.LENGTH_SHORT).show()
            } else if (nativeWord.isEmpty() || translation.isEmpty()) {
                Toast.makeText(requireContext(), "Слова на иностранном и родном языках не могут быть пустыми", Toast.LENGTH_SHORT).show()
            } else {
                val word = Word(
                    nativeWord = nativeWord,
                    translation = translation,
                    exampleNative = exampleNative,
                    exampleTranslation = exampleTranslation,
                    topicId = selectedTopicPair.second,
                    status = 0
                )
                lifecycleScope.launch {
                    roomHelper.wordDao.insert(word)
                    clearFields()
                }
            }
        }

        return view
    }

    private fun clearFields() {
        wordNative.text.clear()
        wordTranslation.text.clear()
        exampleNative.text.clear()
        exampleTranslation.text.clear()
    }
}
