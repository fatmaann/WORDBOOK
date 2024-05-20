package com.example.wordbook

import android.annotation.SuppressLint
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import android.content.Context
import android.graphics.Color
import androidx.core.content.ContextCompat

class TopicAdapter(private val topics: MutableList<Pair<String, Int>>) : RecyclerView.Adapter<TopicAdapter.TopicViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TopicViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_topic, parent, false)
        return TopicViewHolder(view)
    }

    override fun onBindViewHolder(holder: TopicViewHolder, position: Int) {
        holder.bind(topics[position])
    }

    override fun getItemCount(): Int {
        return topics.size
    }

    inner class TopicViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val topicTextView: TextView = itemView.findViewById(R.id.topicTextView)
        private val container: View = itemView.findViewById(R.id.container)
        private val context: Context = itemView.context

        fun bind(topic: Pair<String, Int>) {
            topicTextView.text = topic.first

            val background = GradientDrawable().apply {
                shape = GradientDrawable.RECTANGLE
                cornerRadius = 18f
                setColor(topic.second)
            }

            if (topic.second == Color.WHITE) {
                topicTextView.setTextColor(ContextCompat.getColor(context, android.R.color.black))
            } else {
                topicTextView.setTextColor(ContextCompat.getColor(context, android.R.color.white))
            }

            container.background = background
        }
    }
}