package com.example.wordbook

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.wordbook.R

class ResultsFragment : Fragment() {

    companion object {
        fun newInstance(correctAnswers: Int, totalQuestions: Int): ResultsFragment {
            val fragment = ResultsFragment()
            val args = Bundle()
            args.putInt("correctAnswers", correctAnswers)
            args.putInt("totalQuestions", totalQuestions)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_results, container, false)
        val correctAnswers = arguments?.getInt("correctAnswers") ?: 0
        val totalQuestions = arguments?.getInt("totalQuestions") ?: 0
        val percentage = if (totalQuestions != 0) (correctAnswers.toFloat() / totalQuestions * 100).toInt() else 0


        view.findViewById<TextView>(R.id.textCorrectAnswers).text = "Верные ответы: $correctAnswers"
        view.findViewById<TextView>(R.id.textTotalQuestions).text = "Вопросы: $totalQuestions"
        view.findViewById<TextView>(R.id.textPercentage).text = "Процент верных: $percentage%"

        return view
    }
}
