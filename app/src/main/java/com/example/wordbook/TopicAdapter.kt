package com.example.wordbook

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView

class TopicAdapter(
    private val topics: MutableList<Topic>,
    private val onTopicClick: (Int) -> Unit
) : RecyclerView.Adapter<TopicAdapter.TopicViewHolder>() {

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

    fun updateTopics(newTopics: List<Topic>) {
        topics.clear()
        topics.addAll(newTopics)
        notifyDataSetChanged()
    }

    inner class TopicViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val topicTextView: TextView = itemView.findViewById(R.id.topicTextView)
        private val container: View = itemView.findViewById(R.id.container)
        private val context: Context = itemView.context

        fun bind(topic: Topic) {
            topicTextView.text = topic.name

            val background = GradientDrawable().apply {
                shape = GradientDrawable.RECTANGLE
                cornerRadius = 18f
                setColor(topic.color)
            }

            if (topic.color == Color.WHITE) {
                topicTextView.setTextColor(ContextCompat.getColor(context, android.R.color.black))
            } else {
                topicTextView.setTextColor(ContextCompat.getColor(context, android.R.color.white))
            }

            container.background = background

            itemView.setOnClickListener {
                onTopicClick(topic.id)
            }
        }
    }
}
