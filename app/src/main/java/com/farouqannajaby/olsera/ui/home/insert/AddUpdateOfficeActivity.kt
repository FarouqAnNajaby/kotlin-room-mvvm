package com.farouqannajaby.olsera.ui.home.insert

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import com.farouqannajaby.olsera.R
import com.farouqannajaby.olsera.databinding.ActivityAddUpdateOfficeBinding
import com.farouqannajaby.olsera.ui.map.MapsActivity

class AddUpdateOfficeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddUpdateOfficeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddUpdateOfficeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        title = resources.getString(R.string.title_add_office)

        binding.btnMaps.setOnClickListener{
            toMaps()
        }
    }

    private fun toMaps(){
        val intent = Intent(this@AddUpdateOfficeActivity, MapsActivity::class.java)
        startActivity(intent)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }
}