package com.farouqannajaby.olsera.ui.home.insert

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.RadioButton
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import com.farouqannajaby.olsera.R
import com.farouqannajaby.olsera.database.Office
import com.farouqannajaby.olsera.databinding.ActivityAddUpdateOfficeBinding
import com.farouqannajaby.olsera.helper.ViewModelFactory
import com.farouqannajaby.olsera.ui.map.MapsActivity

class AddUpdateOfficeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddUpdateOfficeBinding

    companion object {
        const val EXTRA_OFFICE = "extra_office"
        const val EXTRA_LONGTITUDE = "extra_longtitude"
        const val EXTRA_LATITUDE = "extra_latitude"
        const val EXTRA_ADDRESS = "extra_address"
        const val EXTRA_CITY = "extra_city"
        const val EXTRA_POSTAL = "extra_postal"
        const val ALERT_DIALOG_CLOSE = 10
        const val ALERT_DIALOG_DELETE = 20
        const val TAG = "addupdate"
    }

    private var isEdit = false
    private var status: String = "0"
    private var office: Office? = null
    private var lat : String = ""
    private var long : String = ""
    private lateinit var officeAddUpdateViewModel: OfficeAddUpdateViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddUpdateOfficeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        officeAddUpdateViewModel = obtainViewModel(this@AddUpdateOfficeActivity)

        office = intent.getParcelableExtra(EXTRA_OFFICE)

        lat = intent.getStringExtra(EXTRA_LATITUDE).toString()
        long = intent.getStringExtra(EXTRA_LONGTITUDE).toString()

        binding.etAlamat.setText(intent.getStringExtra(EXTRA_ADDRESS).toString())
        binding.etCity.setText(intent.getStringExtra(EXTRA_CITY).toString())
        binding.etKode.setText(intent.getStringExtra(EXTRA_POSTAL).toString())

        showToast(long)
        showToast(lat)
        showToast(binding.etAlamat.text.toString())

        if (office!= null){
            isEdit = true
        }
        else{
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
            }
            else{
                actionBarTitle = getString(R.string.title_add_office)
                btnTitle = getString(R.string.title_add_office)
            }

            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar?.title = actionBarTitle

            binding.btnsave.text = btnTitle

            binding.btnsave.setOnClickListener {
                val title = binding.etNama.text.toString().trim()
                val city = binding.etCity.text.toString().trim()
                val alamat = binding.etAlamat.text.toString().trim()
                val kode = binding.etKode.text.toString().trim()


                when {
                    title.isEmpty() -> {
                        binding.etNama.error = getString(R.string.text_empty)
                    }
                    city.isEmpty() -> {
                        binding.etCity.error = getString(R.string.text_empty)
                    }
                    alamat.isEmpty() -> {
                        binding.etAlamat.error = getString(R.string.text_empty)
                    }
                    kode.isEmpty() -> {
                        binding.etKode.error = getString(R.string.text_empty)
                    }
                    lat.isEmpty() -> {
                        showToast(getString(R.string.empty_latitude))
                    }
                    long.isEmpty() -> {
                        showToast(getString(R.string.longtitude_empty))
                    }
                    else -> {
                        office.let { office ->
                            office?.title = title
                            office?.city = city
                            office?.alamat = alamat
                            office?.zipcode = kode
                            office?.latitude = lat
                            office?.longtitude = long
                            office?.status = status
                        }
                        if (isEdit) {
                            officeAddUpdateViewModel.update(office as Office)
                            showToast(getString(R.string.changed))
                        }else{
                            officeAddUpdateViewModel.insert(office as Office)
                            showToast(getString(R.string.added))
                        }
                        finish()
                    }
                }
            }
        }

        binding.btnMaps.setOnClickListener{
            toMaps()
        }
    }

    private fun toMaps(){
        val intent = Intent(this@AddUpdateOfficeActivity, MapsActivity::class.java)
        startActivity(intent)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        if (isEdit) {
            menuInflater.inflate(R.menu.menu_office, menu)
        }
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_delete -> showAlertDialog(ALERT_DIALOG_DELETE)
            android.R.id.home -> showAlertDialog(ALERT_DIALOG_CLOSE)
        }
        return super.onOptionsItemSelected(item)
    }

    private fun obtainViewModel(activity: AppCompatActivity): OfficeAddUpdateViewModel {
        val factory = ViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory)[OfficeAddUpdateViewModel::class.java]
    }

    private fun showAlertDialog(type: Int) {
        val isDialogClose = type == ALERT_DIALOG_CLOSE
        val dialogTitle: String
        val dialogMessage: String
        if (isDialogClose) {
            dialogTitle = getString(R.string.title_cancel)
            dialogMessage = getString(R.string.message_cancel)
        } else {
            dialogMessage = getString(R.string.message_delete)
            dialogTitle = getString(R.string.delete)
        }
        val alertDialogBuilder = AlertDialog.Builder(this)
        with(alertDialogBuilder) {
            setTitle(dialogTitle)
            setMessage(dialogMessage)
            setCancelable(false)
            setPositiveButton(getString(R.string.yes)) { _, _ ->
                if (!isDialogClose) {
                    officeAddUpdateViewModel.delete(office as Office)
                    showToast(getString(R.string.deleted))
                }
                finish()
            }
            setNegativeButton(getString(R.string.no)) { dialog, _ -> dialog.cancel() }
        }
        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }

    override fun onBackPressed() {
        showAlertDialog(ALERT_DIALOG_CLOSE)
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    fun onRadioButtonClicked(view: View) {
        if (view is RadioButton) {
            val checked = view.isChecked
            when (view.getId()) {
                R.id.radio_active ->
                    if (checked) {
                        status = "1"
                    }
                R.id.radio_inactive ->
                    if (checked) {
                        status = "0"
                    }
            }
        }
    }
}