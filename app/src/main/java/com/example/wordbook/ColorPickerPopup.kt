package com.example.wordbook

import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.util.Log
import android.widget.PopupWindow

class ColorPickerPopup(context: Context, private val listener: OnColorSelectedListener) {

    interface OnColorSelectedListener {
        fun onColorSelected(color: Int)
    }

    private val popupWindow: PopupWindow

    init {
        val view = LayoutInflater.from(context).inflate(R.layout.color_picker_popup, null)
        popupWindow = PopupWindow(
            view,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            true
        )

        val colorViews = listOf(
            view.findViewById<View>(R.id.colorRed),
            view.findViewById<View>(R.id.colorGreen),
            view.findViewById<View>(R.id.colorBlue),
            view.findViewById<View>(R.id.colorRed1),
            view.findViewById<View>(R.id.colorGreen1),
            view.findViewById<View>(R.id.colorBlue1)
        )

        val colors = listOf(
            0xFFDF433A.toInt(), // Red
            0xFF3EE04C.toInt(), // Green
            0xFF476EE1.toInt(),  // Blue
            0xFFFFFFFF.toInt(),
            0xFFFFFFFF.toInt(),
            0xFFFFFFFF.toInt()
        )

        colorViews.forEachIndexed { index, view ->
            val drawable = view.background as GradientDrawable
            drawable.setColor(colors[index])
            view.setOnClickListener {
                listener.onColorSelected(colors[index])
                popupWindow.dismiss()
            }
        }
    }

    fun show(anchor: View) {
        popupWindow.showAsDropDown(anchor)

        val location = IntArray(2)
        anchor.getLocationOnScreen(location)
        popupWindow.update(
            location[0] + anchor.width + 22,
            location[1] + 9,
            popupWindow.width,
            popupWindow.height
        )
    }
}
