package com.farouqannajaby.olsera.ui.home.insert

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.lifecycle.ViewModelProvider
import com.farouqannajaby.olsera.R
import com.farouqannajaby.olsera.database.Office
import com.farouqannajaby.olsera.databinding.ActivityAddUpdateOfficeBinding
import com.farouqannajaby.olsera.helper.ViewModelFactory
import com.farouqannajaby.olsera.ui.map.MapsActivity

class AddUpdateOfficeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddUpdateOfficeBinding

    companion object {
        const val EXTRA_NOTE = "extra_office"
        const val ALERT_DIALOG_CLOSE = 10
        const val ALERT_DIALOG_DELETE = 20
    }

    private var isEdit = false
    private var office: Office? = null
    private lateinit var officeAddUpdateViewModel: OfficeAddUpdateViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddUpdateOfficeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        officeAddUpdateViewModel = obtainViewModel(this@AddUpdateOfficeActivity)

        office = intent.getParcelableExtra(EXTRA_NOTE)

        if (office!= null){
            isEdit = true
        }else{
            office = Office()
        }

        var actionBarTitle: String
        var btnTitle: String

        if (isEdit){
            actionBarTitle = getString(R.string.title_edit_office)
            btnTitle = getString(R.string.title_edit_office)
            if (office != null){
                office?.let { office ->
                    binding.etNama.setText(office.title)
                    binding.etAlamat.setText(office.alamat)
                    binding.etKode.setText(office.zipcode)
                    binding.etCity.setText(office.city)
                }
            }else{
                actionBarTitle = getString(R.string.title_add_office)
                btnTitle = getString(R.string.title_add_office)
            }

            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar?.title = actionBarTitle
            binding.btnsave.text = btnTitle
        }

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

    private fun obtainViewModel(activity: AppCompatActivity): OfficeAddUpdateViewModel {
        val factory = ViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory)[OfficeAddUpdateViewModel::class.java]
    }

}