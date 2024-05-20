package com.example.wordbook

import android.app.Dialog
import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.View
import android.widget.GridLayout

class ColorPickerDialog(context: Context, private val listener: OnColorSelectedListener) : Dialog(context) {

    interface OnColorSelectedListener {
        fun onColorSelected(color: Int)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.color_picker_dialog)

        val colorGrid: GridLayout = findViewById(R.id.colorGrid)

        val colorViews = listOf(
            findViewById<View>(R.id.colorRed),
            findViewById<View>(R.id.colorGreen),
            findViewById<View>(R.id.colorBlue)
        )

        val colors = listOf(
            0xFFDF433A.toInt(), // Red
            0xFF3EE04C.toInt(), // Green
            0xFF476EE1.toInt()  // Blue
        )

        colorViews.forEachIndexed { index, view ->
            val drawable = view.background as GradientDrawable
            drawable.setColor(colors[index])
            view.setOnClickListener {
                listener.onColorSelected(colors[index])
                dismiss()
            }
        }
    }
}
