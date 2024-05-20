package com.example.wordbook

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment

class AddTopicFragment : Fragment() {

    private lateinit var editText: EditText
    private lateinit var saveButton: Button
    private lateinit var colorButton: Button
    private var listener: OnTopicSavedListener? = null
    private var selectedColor: Int = Color.WHITE

    interface OnTopicSavedListener {
        fun onTopicSaved(topic: String, color: Int)
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
            val dialog = ColorPickerDialog(requireContext(), object : ColorPickerDialog.OnColorSelectedListener {
                override fun onColorSelected(color: Int) {
                    selectedColor = color
                    colorButton.setBackgroundColor(color)
                }
            })
            dialog.show()
        }

        saveButton.setOnClickListener {
            val topic = editText.text.toString()
            listener?.onTopicSaved(topic, selectedColor)
            requireActivity().supportFragmentManager.popBackStack()
        }

        return view
    }
}