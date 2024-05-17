package com.example.wordbook

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainMenuFragment : Fragment(), AddTopicFragment.OnTopicSavedListener {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: TopicAdapter
    private var topics: MutableList<String> = mutableListOf("Ошибки", "Выученные")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_main_menu, container, false)

        recyclerView = view.findViewById(R.id.recycler_view)
        adapter = TopicAdapter(topics)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter

        val addButton: Button = view.findViewById(R.id.add_button)
        addButton.setOnClickListener {
            val addTopicFragment = AddTopicFragment()
            addTopicFragment.setListener(this)
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, addTopicFragment)
                .addToBackStack(null)
                .commit()
        }

        return view
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onTopicSaved(topic: String) {
        topics.add(topic)
        adapter.notifyDataSetChanged()
    }
}