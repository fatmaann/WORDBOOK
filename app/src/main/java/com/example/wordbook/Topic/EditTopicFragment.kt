package com.example.wordbook

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

class EditTopicFragment : Fragment() {

    private lateinit var editTopicName: EditText
    private lateinit var selectColorButton: Button
    private lateinit var saveButton: Button
    private lateinit var backButton: Button
    private lateinit var deleteButton: Button
    private lateinit var roomHelper: RoomHelper
    private var topicId: Int = -1
    private var selectedColor: Int = Color.WHITE

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_edit_topic, container, false)

        editTopicName = view.findViewById(R.id.edit_topic_name)
        selectColorButton = view.findViewById(R.id.select_color_button)
        saveButton = view.findViewById(R.id.save_button)
        backButton = view.findViewById(R.id.back_button)
        deleteButton = view.findViewById(R.id.delete_button)

        roomHelper = RoomHelper(requireContext())

        topicId = arguments?.getInt("TOPIC_ID") ?: -1

        lifecycleScope.launch {
            val topic = roomHelper.topicDao.getTopicNameById(topicId)
            if (topic != null) {
                editTopicName.setText(topic.name)
                selectedColor = topic.color
                updateColorButton()

                if (topicId == 1 || topicId == 2) {
                    editTopicName.isEnabled = false
                    deleteButton.visibility = View.GONE
                }
            }
        }

        selectColorButton.setOnClickListener {
            val dialog = ColorPickerDialog(
                requireContext(),
                object : ColorPickerDialog.OnColorSelectedListener {
                    override fun onColorSelected(color: Int) {
                        selectedColor = color
                        updateColorButton()
                    }
                })
            dialog.show()
        }

        saveButton.setOnClickListener {
            val newName = editTopicName.text.toString()
            lifecycleScope.launch {
                val topic = Topic(id = topicId, name = newName, color = selectedColor)
                roomHelper.topicDao.update(topic)
                parentFragmentManager.popBackStack()
            }
        }

        backButton.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        deleteButton.setOnClickListener {
            showDeleteConfirmationDialog()
        }

        return view
    }

    private fun updateColorButton() {
        selectColorButton.setBackgroundColor(selectedColor)
        selectColorButton.setTextColor(if (selectedColor == Color.WHITE) Color.BLACK else Color.WHITE)
    }

    private fun showDeleteConfirmationDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle("Удаление темы")
            .setMessage("Вы уверены, что хотите удалить эту тему?")
            .setPositiveButton("Да") { dialog, which ->
                lifecycleScope.launch {
                    roomHelper.wordDao.deleteWordsForTopic(topicId)
                    roomHelper.topicDao.deleteTopic(topicId)
                    Toast.makeText(requireContext(), "Тема удалена", Toast.LENGTH_SHORT).show()
                    parentFragmentManager.popBackStack()
                    parentFragmentManager.popBackStack()
                    parentFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, MainMenuFragment())
                        .addToBackStack(null)
                        .commit()
                }
            }
            .setNegativeButton("Нет", null)
            .show()
    }
}