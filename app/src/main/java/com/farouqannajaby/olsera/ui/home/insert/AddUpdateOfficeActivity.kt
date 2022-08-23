package com.farouqannajaby.olsera.ui.home.insert

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.RadioButton
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModelProvider
import com.farouqannajaby.olsera.R
import com.farouqannajaby.olsera.database.Office
import com.farouqannajaby.olsera.databinding.ActivityAddUpdateOfficeBinding
import com.farouqannajaby.olsera.helper.ViewModelFactory
import com.farouqannajaby.olsera.ui.home.HomeActivity
import com.farouqannajaby.olsera.ui.map.MapsActivity
import com.farouqannajaby.olsera.utils.Constanta.ALERT_DIALOG_CLOSE
import com.farouqannajaby.olsera.utils.Constanta.ALERT_DIALOG_DELETE
import com.farouqannajaby.olsera.utils.Constanta.EXTRA_ADDRESS
import com.farouqannajaby.olsera.utils.Constanta.EXTRA_CITY
import com.farouqannajaby.olsera.utils.Constanta.EXTRA_LATITUDE
import com.farouqannajaby.olsera.utils.Constanta.EXTRA_LONGTITUDE
import com.farouqannajaby.olsera.utils.Constanta.EXTRA_POSTAL
import com.farouqannajaby.olsera.utils.Constanta.EXTRA_TITLE
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import java.io.IOException
import java.util.*

class AddUpdateOfficeActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var binding: ActivityAddUpdateOfficeBinding

    companion object {
        const val EXTRA_OFFICE = "extra_office"
    }

    private var currentLocation : Location? = null
    private var fusedLocationProviderClient: FusedLocationProviderClient? = null
    private val REQUEST_CODE = 101

    private var isEdit = false
    private var status: String = "0"
    private var office: Office? = null
    private var lat : String = ""
    private var long : String = ""
    private var city : String? = null
    private var alamat: String? = null
    private var codePos: String? = null
    private var title: String? = null
    private lateinit var officeAddUpdateViewModel: OfficeAddUpdateViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddUpdateOfficeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this@AddUpdateOfficeActivity)

        fetchLocation()

        officeAddUpdateViewModel = obtainViewModel(this@AddUpdateOfficeActivity)

        office =  intent.getParcelableExtra(EXTRA_OFFICE)

        lat = intent.getStringExtra(EXTRA_LATITUDE) ?: "0"
        long = intent.getStringExtra(EXTRA_LONGTITUDE) ?: "0"
        city = intent.getStringExtra(EXTRA_CITY) ?: ""
        title = intent.getStringExtra(EXTRA_TITLE) ?: ""
        alamat = intent.getStringExtra(EXTRA_ADDRESS) ?: ""
        codePos = intent.getStringExtra(EXTRA_POSTAL) ?: ""

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

                        officeAddUpdateViewModel.update(office as Office)
                        showToast(getString(R.string.changed))
                        val intent = Intent(this@AddUpdateOfficeActivity,
                            HomeActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                }
            }

        }
        else{
            actionBarTitle = getString(R.string.title_add_office)
            btnTitle = getString(R.string.title_add_office)

            binding.etNama.setText(title)
            binding.etAlamat.setText(alamat)
            binding.etKode.setText(codePos)
            binding.etCity.setText(city)

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

                        officeAddUpdateViewModel.insert(office as Office)
                        showToast(getString(R.string.added))

                        val intent = Intent(this@AddUpdateOfficeActivity,
                            HomeActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                }
            }
        }

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = actionBarTitle

        binding.btnsave.text = btnTitle

        binding.btnCancel.setOnClickListener{
            val intent = Intent(this@AddUpdateOfficeActivity, HomeActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.btnMaps.setOnClickListener{
            toMaps()
        }
    }

    private fun fetchLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_COARSE_LOCATION)
            != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), REQUEST_CODE)
            return
        }

        val task = fusedLocationProviderClient!!.lastLocation
        task.addOnSuccessListener { location ->
            if (location != null){
                currentLocation = location
                val supportMapFragment = (supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?)
                supportMapFragment!!.getMapAsync(this@AddUpdateOfficeActivity)
            }
        }
    }

    private fun toMaps(){
        val intent = Intent(this@AddUpdateOfficeActivity, MapsActivity::class.java)
        startActivity(intent)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
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

    override fun onMapReady(googleMap: GoogleMap) {
        val latLng = LatLng(currentLocation!!.latitude, currentLocation!!.longitude)
        val markerOptions = MarkerOptions().position(latLng).title("Your Location")
        googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng))
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,15f))
        googleMap.addMarker(markerOptions)

        binding.etCity.setText(getCity(currentLocation!!.latitude, currentLocation!!.longitude))
        binding.etAlamat.setText(getAddressName(currentLocation!!.latitude, currentLocation!!.longitude))
        binding.etKode.setText(getPostalCode(currentLocation!!.latitude, currentLocation!!.longitude))

        googleMap.setOnMapLongClickListener { latLng ->
            googleMap.addMarker(
                MarkerOptions()
                    .position(latLng)
                    .title("Lokasi Anda")
                    .snippet("Lat: ${latLng.latitude} Long: ${latLng.longitude}")
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.img_location))
                    .flat(true)

            )
            lat = latLng.latitude.toString()
            long = latLng.longitude.toString()

            binding.etCity.setText(getCity(lat.toDouble(),long.toDouble()))
            binding.etAlamat.setText(getAddressName(lat.toDouble(),long.toDouble()))
            binding.etKode.setText(getPostalCode(lat.toDouble(),long.toDouble()))
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when(requestCode){
            REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    fetchLocation()
                }
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    private fun getAddressName(lat: Double, lon: Double): String? {
        var addressName: String? = null
        val geocoder = Geocoder(this, Locale.getDefault())
        try {
            val list = geocoder.getFromLocation(lat, lon, 1)
            if (list != null && list.size != 0) {
                addressName = list[0].getAddressLine(0)
                Log.d("cekName", "getAddressName: $addressName")
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return addressName
    }

    private fun getCity(lat: Double, lon: Double): String? {
        var city: String? = null
        val geocoder = Geocoder(this, Locale.getDefault())
        try {
            val list = geocoder.getFromLocation(lat, lon, 1)
            if (list != null && list.size != 0) {
                city = list[0].subAdminArea
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return city
    }

    private fun getPostalCode(lat: Double, lon: Double): String? {
        var postal: String? = null
        val geocoder = Geocoder(this, Locale.getDefault())
        try {
            val list = geocoder.getFromLocation(lat, lon, 1)
            if (list != null && list.size != 0) {
                postal = list[0].postalCode
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return postal
    }

}