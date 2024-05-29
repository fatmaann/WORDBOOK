package com.example.wordbook

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    lateinit var bottomNavigationView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        clearBackStack()

        bottomNavigationView = findViewById(R.id.bottom_navigation)
        bottomNavigationView.setOnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.menu_item_home -> {
                    replaceFragment(MainMenuFragment(), R.id.menu_item_home)
                    true
                }
                R.id.menu_item_add -> {
                    replaceFragment(AddWordFragment(), R.id.menu_item_add)
                    true
                }
                R.id.menu_item_test -> {
                    replaceFragment(TestChooseFragment(), R.id.menu_item_test)
                    true
                }
                else -> false
            }
        }

        if (intent.hasExtra("correctAnswers") && intent.hasExtra("totalQuestions")) {
            val correctAnswers = intent.getIntExtra("correctAnswers", 0)
            val totalQuestions = intent.getIntExtra("totalQuestions", 0)
            showResultsFragment(correctAnswers, totalQuestions)
        } else {
            replaceFragment(MainMenuFragment(), R.id.menu_item_home)
        }
    }

    companion object {
        fun setAllButtonsGrey(bottomNavigationView: BottomNavigationView) {
            for (i in 0 until 4) {
                bottomNavigationView.menu.getItem(i).isChecked = false
            }
        }
    }

    private fun replaceFragment(fragment: Fragment, itemId: Int) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .addToBackStack(null)
            .commit()

        bottomNavigationView.menu.findItem(itemId).isChecked = true
    }

    private fun clearBackStack() {
        val fragmentManager = supportFragmentManager
        if (fragmentManager.backStackEntryCount > 0) {
            val firstFragment = fragmentManager.getBackStackEntryAt(0)
            fragmentManager.popBackStack(firstFragment.id, FragmentManager.POP_BACK_STACK_INCLUSIVE)
        }
    }

    private fun showResultsFragment(correctAnswers: Int, totalQuestions: Int) {
        val fragment = ResultsFragment.newInstance(correctAnswers, totalQuestions)
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .addToBackStack(null)
            .commit()
    }
}
