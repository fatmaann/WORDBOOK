package com.example.wordbook

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.recyclerview.widget.RecyclerView

class TestTopicAdapter(private val topics: List<TestTopicItem>) :
    RecyclerView.Adapter<TestTopicAdapter.TopicViewHolder>() {

    private var selectedPosition = -1

    inner class TopicViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val radioButton: RadioButton = itemView.findViewById(R.id.radioButtonTopic)

        init {
            radioButton.setOnClickListener {
                selectedPosition = adapterPosition
                notifyDataSetChanged()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TopicViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_test_topic, parent, false)
        return TopicViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: TopicViewHolder, position: Int) {
        holder.radioButton.text = topics[position].name
        holder.radioButton.isChecked = position == selectedPosition
    }

    fun setSelectedPosition(position: Int) {
        selectedPosition = position
        notifyDataSetChanged()
    }

    fun getSelectedTopic(): TestTopicItem? {
        return if (selectedPosition != -1) topics[selectedPosition] else null
    }

    override fun getItemCount() = topics.size
}
