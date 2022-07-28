package com.farouqannajaby.olsera.ui.home.adapter

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.farouqannajaby.olsera.ui.home.AllStatusFragment

class SectionPagerAdapter(activity: AppCompatActivity) : FragmentStateAdapter(activity) {
    override fun getItemCount(): Int {
        return 3
    }

    override fun createFragment(position: Int): Fragment {
        val fragment = AllStatusFragment()
        fragment.arguments = Bundle().apply {
            putInt(AllStatusFragment.ARG_SECTION_NUMBER, position + 1)
        }
        return fragment
    }

}