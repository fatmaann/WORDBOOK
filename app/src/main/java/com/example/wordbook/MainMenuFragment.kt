package com.example.wordbook

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.fragment.app.viewModels


class MainMenuFragment : Fragment(), AddTopicFragment.OnTopicSavedListener {

    private lateinit var recyclerView: RecyclerView
    private val viewModel by viewModels<TopicViewModel>()
    private lateinit var adapter: TopicAdapter
//    private var topics: MutableList<Pair<String, Int>> = mutableListOf(
//        "Ошибки" to Color.WHITE,
//        "Выученные" to Color.WHITE
//    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_main_menu, container, false)

        recyclerView = view.findViewById(R.id.recycler_view)
        adapter = TopicAdapter(viewModel.topics)
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
    override fun onTopicSaved(topic: String, color: Int) {
        viewModel.topics.add(topic to color)
        adapter.notifyDataSetChanged()
    }
}