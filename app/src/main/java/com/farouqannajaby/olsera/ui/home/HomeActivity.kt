package com.farouqannajaby.olsera.ui.home

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.farouqannajaby.olsera.R
import com.farouqannajaby.olsera.databinding.ActivityHomeBinding
import com.farouqannajaby.olsera.databinding.ActivityMainBinding
import com.farouqannajaby.olsera.helper.ViewModelFactory
import com.farouqannajaby.olsera.ui.home.adapter.OfficeAdapter
import com.farouqannajaby.olsera.ui.home.adapter.SectionPagerAdapter
import com.farouqannajaby.olsera.ui.home.insert.AddUpdateOfficeActivity
import com.farouqannajaby.olsera.ui.home.insert.OfficeAddUpdateViewModel
import com.google.android.material.tabs.TabLayoutMediator

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding

    companion object {
        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.tab_text_1,
            R.string.tab_text_2,
            R.string.tab_text_3
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val sectionsPagerAdapter = SectionPagerAdapter(this)

        binding.viewPager.adapter = sectionsPagerAdapter
        TabLayoutMediator(binding.tabs, binding.viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()

        binding.btnAdd.setOnClickListener{
            toAddEdit()
        }


    }

    private fun toAddEdit(){
        val intent = Intent(this@HomeActivity, AddUpdateOfficeActivity::class.java)
        startActivity(intent)
    }

}