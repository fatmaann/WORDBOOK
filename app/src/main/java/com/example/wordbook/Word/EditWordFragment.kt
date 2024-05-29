package com.example.wordbook

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

class EditWordFragment : Fragment() {

    private lateinit var editNativeWord: EditText
    private lateinit var editTranslation: EditText
    private lateinit var editExampleNative: EditText
    private lateinit var editExampleTranslation: EditText
    private lateinit var selectTopicSpinner: Spinner
    private lateinit var learnedCheckBox: CheckBox
    private lateinit var saveButton: Button
    private lateinit var deleteButton: Button
    private lateinit var backButton: Button
    private lateinit var roomHelper: RoomHelper
    private var wordStatus: Int = 0
    private var wordIsLearned: Boolean = false
    private var wordIsMistaken: Boolean = false
    private var wordId: Int = -1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_edit_word, container, false)
        roomHelper = RoomHelper(requireContext())
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        editNativeWord = view.findViewById(R.id.edit_native_word)
        editTranslation = view.findViewById(R.id.edit_translation)
        editExampleNative = view.findViewById(R.id.edit_example_native)
        editExampleTranslation = view.findViewById(R.id.edit_example_translation)
        selectTopicSpinner = view.findViewById(R.id.select_topic_spinner)
        learnedCheckBox = view.findViewById(R.id.checkbox_learned)
        saveButton = view.findViewById(R.id.save_button)
        deleteButton = view.findViewById(R.id.delete_button)
        backButton = view.findViewById(R.id.back_button)

        wordId = arguments?.getInt("WORD_ID") ?: -1

        if (wordId != -1) {
            lifecycleScope.launch {
                val word = roomHelper.wordDao.getWordById(wordId)
                if (word != null) {
                    wordStatus = word.status
                    wordIsLearned = word.isLearned
                    wordIsMistaken = word.isMistaken
                    editNativeWord.setText(word.nativeWord)
                    editTranslation.setText(word.translation)
                    editExampleNative.setText(word.exampleNative)
                    editExampleTranslation.setText(word.exampleTranslation)
                    learnedCheckBox.isChecked = word.isLearned

                    val topics = roomHelper.topicDao.getAllTopics()
                    val filteredTopics = topics.filter { it.id != 1 && it.id != 2 }
                    val topicPairs = filteredTopics.map { it.name to it.id }
                    val adapter = object : ArrayAdapter<Pair<String, Int>>(
                        requireContext(),
                        android.R.layout.simple_spinner_item,
                        topicPairs
                    ) {
                        override fun getView(
                            position: Int,
                            convertView: View?,
                            parent: ViewGroup
                        ): View {
                            val view = super.getView(position, convertView, parent)
                            (view as TextView).text = getItem(position)?.first
                            return view
                        }

                        override fun getDropDownView(
                            position: Int,
                            convertView: View?,
                            parent: ViewGroup
                        ): View {
                            val view = super.getDropDownView(position, convertView, parent)
                            (view as TextView).text = getItem(position)?.first
                            return view
                        }
                    }
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    selectTopicSpinner.adapter = adapter

                    selectTopicSpinner.setSelection(filteredTopics.indexOfFirst { it.id == word.topicId })
                }
            }
        }

        saveButton.setOnClickListener {
            val nativeWord = editNativeWord.text.toString()
            val translation = editTranslation.text.toString()
            val exampleNative = editExampleNative.text.toString()
            val exampleTranslation = editExampleTranslation.text.toString()

            if (selectTopicSpinner.adapter == null || selectTopicSpinner.adapter.isEmpty) {
                Toast.makeText(
                    requireContext(),
                    "Пожалуйста, создайте новую тему",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                val selectedTopicPair = selectTopicSpinner.selectedItem as Pair<String, Int>
                val topicId = selectedTopicPair.second
                val status = wordStatus
                val isLearned = learnedCheckBox.isChecked
                val isMistaken = wordIsMistaken

                if (nativeWord.isNotBlank() && translation.isNotBlank()) {
                    lifecycleScope.launch {
                        roomHelper.wordDao.updateWordById(
                            wordId,
                            nativeWord,
                            translation,
                            exampleNative,
                            exampleTranslation,
                            topicId,
                            status,
                            isLearned,
                            isMistaken
                        )
                        parentFragmentManager.popBackStack()
                    }
                } else {
                    Toast.makeText(
                        requireContext(),
                        "Пожалуйста, заполните обязательные поля",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }

        deleteButton.setOnClickListener {
            lifecycleScope.launch {
                roomHelper.wordDao.deleteWordById(wordId)
                parentFragmentManager.popBackStack()
            }
        }

        backButton.setOnClickListener {
            parentFragmentManager.popBackStack()
        }
    }
}
