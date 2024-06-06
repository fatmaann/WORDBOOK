package com.example.wordbook

import android.annotation.SuppressLint
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
    private lateinit var learnedCheckBox: CheckBox
    private lateinit var saveButton: Button
    private lateinit var roomHelper: RoomHelper

    @SuppressLint("ResourceType")
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
        learnedCheckBox = view.findViewById(R.id.checkbox_learned)
        saveButton = view.findViewById(R.id.save_button)

        roomHelper = RoomHelper(requireContext())

        lifecycleScope.launch {
            val topics = roomHelper.topicDao.getAllTopics()
            val filteredTopics = topics.filter { it.id != 1 && it.id != 2 }
            val topicPairs = filteredTopics.map { it.name to it.id }
            val adapter = object : ArrayAdapter<Pair<String, Int>>(
                requireContext(),
                android.R.layout.simple_spinner_item,
                topicPairs
            ) {
                override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
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
            adapter.setDropDownViewResource(R.drawable.spinner_window_drawable)
            topicSpinner.adapter = adapter
        }

        saveButton.setOnClickListener {
            if (topicSpinner.adapter == null || topicSpinner.adapter.isEmpty) {
                Toast.makeText(
                    requireContext(),
                    "Пожалуйста, создайте новую тему",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                val selectedTopicPair = topicSpinner.selectedItem as Pair<String, Int>
                val nativeWord = wordNative.text.toString()
                val translation = wordTranslation.text.toString()
                val exampleNative = exampleNative.text.toString()
                val exampleTranslation = exampleTranslation.text.toString()
                val isLearned = learnedCheckBox.isChecked

                if (nativeWord.length > 40 || translation.length > 40 || exampleNative.length > 40 || exampleTranslation.length > 40) {
                    Toast.makeText(
                        requireContext(),
                        "Ни одно из полей не должно превышать 40 символов",
                        Toast.LENGTH_SHORT
                    ).show()
                } else if (nativeWord.isEmpty() || translation.isEmpty()) {
                    Toast.makeText(
                        requireContext(),
                        "Слова на иностранном и родном языках не могут быть пустыми",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    val word = Word(
                        nativeWord = nativeWord,
                        translation = translation,
                        exampleNative = exampleNative,
                        exampleTranslation = exampleTranslation,
                        topicId = selectedTopicPair.second,
                        isLearned = isLearned
                    )
                    lifecycleScope.launch {
                        roomHelper.wordDao.insert(word)
                        clearFields()
                        val activity = context as MainActivity
                        MainActivity.setAllButtonsGrey(activity.bottomNavigationView)
                        openWordListFragment(selectedTopicPair.second)
                    }
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

    private fun openWordListFragment(topicId: Int) {
        val fragment = WordListFragment().apply {
            arguments = Bundle().apply {
                putInt("TOPIC_ID", topicId)
            }
        }
        requireActivity().supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .addToBackStack(null)
            .commit()
    }
}
