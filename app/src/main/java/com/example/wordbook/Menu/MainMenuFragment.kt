package com.example.wordbook

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EdgeEffect
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.wordbook.*
import kotlinx.coroutines.launch

class MainMenuFragment : Fragment(), AddTopicFragment.OnTopicSavedListener {

    private lateinit var recyclerView: RecyclerView
    private lateinit var roomHelper: RoomHelper
    private lateinit var adapter: TopicAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        roomHelper = RoomHelper(requireContext())
        return inflater.inflate(R.layout.fragment_main_menu, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.recycler_view)
        recyclerView.edgeEffectFactory = object : RecyclerView.EdgeEffectFactory() {
            override fun createEdgeEffect(view: RecyclerView, direction: Int): EdgeEffect {
                return object : EdgeEffect(view.context) {
                    override fun onPull(deltaDistance: Float, displacement: Float) {
                        // Do nothing
                    }

                    override fun onRelease() {
                        // Do nothing
                    }

                    override fun onAbsorb(velocity: Int) {
                        // Do nothing
                    }

                    override fun onPull(deltaDistance: Float) {
                        // Do nothing
                    }
                }
            }
        }
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        loadTopics()

        val addButton: Button = view.findViewById(R.id.add_button)
        addButton.setOnClickListener {
            val activity = context as MainActivity
            MainActivity.setAllButtonsGrey(activity.bottomNavigationView)

            val addTopicFragment = AddTopicFragment()
            addTopicFragment.setListener(this)
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, addTopicFragment)
                .addToBackStack(null)
                .commit()
        }
    }

    private fun loadTopics() {
        lifecycleScope.launch {
            val topics = roomHelper.topicDao.getAllTopics().toMutableList()
            adapter = TopicAdapter(topics) { topicId ->
                openWordListFragment(topicId)
            }
            recyclerView.adapter = adapter
        }
    }

    private fun openWordListFragment(topicId: Int) {
        val fragment = WordListFragment().apply {
            arguments = Bundle().apply {
                putInt("TOPIC_ID", topicId)
            }
        }
        parentFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .addToBackStack(null)
            .commit()
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onTopicSaved(topic: Topic) {
        lifecycleScope.launch {
            roomHelper.topicDao.insert(topic)
            loadTopics()
        }
    }
}
