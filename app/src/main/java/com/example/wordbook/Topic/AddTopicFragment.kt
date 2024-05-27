package com.example.wordbook

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment

class AddTopicFragment : Fragment() {

    private lateinit var editText: EditText
    private lateinit var saveButton: Button
    private lateinit var colorButton: Button
    private var listener: OnTopicSavedListener? = null
    private var selectedColor: Int = Color.WHITE

    interface OnTopicSavedListener {
        fun onTopicSaved(topic: Topic)
    }

    fun setListener(listener: OnTopicSavedListener) {
        this.listener = listener
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_add_topic, container, false)

        editText = view.findViewById(R.id.editText)
        saveButton = view.findViewById(R.id.saveButton)
        colorButton = view.findViewById(R.id.colorButton)

        colorButton.setOnClickListener {
            val popup = ColorPickerPopup(requireContext(), object : ColorPickerPopup.OnColorSelectedListener {
                override fun onColorSelected(color: Int) {
                    selectedColor = color
                    colorButton.setBackgroundColor(color)
                }
            })
            popup.show(it)
        }

        saveButton.setOnClickListener {
            val topicName = editText.text.toString()
            if (topicName.length > 30) {
                Toast.makeText(
                    requireContext(),
                    "Название темы не может превышать 30 символов",
                    Toast.LENGTH_SHORT
                ).show()
            } else if (topicName.isEmpty()) {
                Toast.makeText(
                    requireContext(),
                    "Название темы не может быть пустым",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                val topic = Topic(name = topicName, color = selectedColor)
                listener?.onTopicSaved(topic)
                requireActivity().supportFragmentManager.popBackStack()
            }
        }

        return view
    }
}