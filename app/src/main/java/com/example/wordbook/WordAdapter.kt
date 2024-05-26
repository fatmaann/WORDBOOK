package com.example.wordbook

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class WordAdapter(private var words: List<Word>, private val onItemClick: (Word) -> Unit) : RecyclerView.Adapter<WordAdapter.WordViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WordViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_word, parent, false)
        return WordViewHolder(view)
    }

    override fun onBindViewHolder(holder: WordViewHolder, position: Int) {
        holder.bind(words[position], onItemClick)
    }

    override fun getItemCount(): Int {
        return words.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateWords(newWords: List<Word>) {
        words = newWords
        notifyDataSetChanged()
    }

    class WordViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val nativeTextView: TextView = itemView.findViewById(R.id.native_text)
        private val translationTextView: TextView = itemView.findViewById(R.id.translation_text)
        private val exampleNativeTextView: TextView = itemView.findViewById(R.id.example_native_text)
        private val exampleTranslationTextView: TextView = itemView.findViewById(R.id.example_translation_text)

        fun bind(word: Word, onItemClick: (Word) -> Unit) {
            nativeTextView.text = word.nativeWord
            translationTextView.text = word.translation
            exampleNativeTextView.text = word.exampleNative
            exampleTranslationTextView.text = word.exampleTranslation

            itemView.setOnClickListener {
                onItemClick(word)
            }
        }
    }
}
