package com.farouqannajaby.olsera.ui.home.insert

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.RadioButton
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.farouqannajaby.olsera.R
import com.farouqannajaby.olsera.database.Office
import com.farouqannajaby.olsera.databinding.ActivityAddUpdateOfficeBinding
import com.farouqannajaby.olsera.helper.ViewModelFactory
import com.farouqannajaby.olsera.ui.home.HomeActivity
import com.farouqannajaby.olsera.ui.map.MapsActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*

class AddUpdateOfficeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddUpdateOfficeBinding

    companion object {
        const val EXTRA_OFFICE = "extra_office"
        const val EXTRA_LONGTITUDE = "extra_longtitude"
        const val EXTRA_LATITUDE = "extra_latitude"
        const val EXTRA_ADDRESS = "extra_address"
        const val EXTRA_CITY = "extra_city"
        const val EXTRA_TITLE = "extra_title"
        const val EXTRA_POSTAL = "extra_postal"
        const val ALERT_DIALOG_CLOSE = 10
        const val ALERT_DIALOG_DELETE = 20
        const val TAG = "addupdate"
        const val SIZE_MARKER = 40
    }

    private lateinit var mMap: GoogleMap
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

//        val mapFragment = supportFragmentManager
//            .findFragmentById(R.id.map) as SupportMapFragment
//        mapFragment.getMapAsync(this)

        officeAddUpdateViewModel = obtainViewModel(this@AddUpdateOfficeActivity)

        office =  intent.getParcelableExtra<Office>(EXTRA_OFFICE) as Office

//        lat = intent.getStringExtra(EXTRA_LATITUDE).toString()
//        long = intent.getStringExtra(EXTRA_LONGTITUDE).toString()
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
//                    lat = office.latitude.toString()
                    Log.i(TAG, "lat: ${office.latitude}")
//                    long = office.longtitude.toString()
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

                        officeAddUpdateViewModel.update(office as Office)
                        showToast(getString(R.string.changed))
                        val intent = Intent(this@AddUpdateOfficeActivity,
                            HomeActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                }
            }

        }else{
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

        binding.btnCancel.setOnClickListener{
            val intent = Intent(this@AddUpdateOfficeActivity, HomeActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.btnMaps.setOnClickListener{
            toMaps()
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

//    override fun onMapReady(googleMap: GoogleMap) {
//        mMap = googleMap
//
//        mMap.uiSettings.isZoomControlsEnabled = true
//        mMap.uiSettings.isIndoorLevelPickerEnabled = true
//        mMap.uiSettings.isCompassEnabled = true
//        mMap.uiSettings.isMapToolbarEnabled = true
//
//        mMap.setOnMapLongClickListener { latLng ->
//            mMap.addMarker(
//                MarkerOptions()
//                    .position(latLng)
//                    .title("Lokasi Anda")
//                    .snippet("Lat: ${latLng.latitude} Long: ${latLng.longitude}")
//                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA))
//
//            )
//            lat = latLng.latitude.toString()
//            long = latLng.longitude.toString()
//        }
//
//        googleMap.setOnMapClickListener { point ->
//            Toast.makeText(
//                this,
//                point.latitude.toString() + ", " + point.longitude,
//                Toast.LENGTH_SHORT
//            ).show()
//        }
//
//
//        mLoadMaps()
//
//    }

//    private fun mLoadMaps() {
//        val height: Int = SIZE_MARKER
//        val width: Int = SIZE_MARKER
//        val bitmapdraw = resources.getDrawable(R.drawable.img_location) as BitmapDrawable
//        val b = bitmapdraw.bitmap
//        val smallMarker = Bitmap.createScaledBitmap(
//            b,
//            bitmapdraw.bitmap.width * width / 100,
//            bitmapdraw.bitmap.height * height / 100,
//            false
//        )
//        mMap.addMarker(
//            MarkerOptions()
//                .position(LatLng(lat.toDouble(), long.toDouble()))
//                .icon(BitmapDescriptorFactory.fromBitmap(smallMarker))
//                .title("Lokasi Anda")
//        )
//
//        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(LatLng(lat.toDouble(), long.toDouble()), 10f))
//        val cameraPosition = CameraPosition.Builder()
//            .target(LatLng(lat.toDouble(), long.toDouble())) // Sets the center of the map to location user
//            .zoom(16f) // Sets the zoom
//            .bearing(0f) // Sets the orientation of the camera to east
//            .tilt(0f) // Sets the tilt of the camera to 30 degrees
//            .build() // Creates a CameraPosition from the builder
//        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
//        val circle = mMap.addCircle(
//            CircleOptions()
//                .center(LatLng(lat.toDouble(), long.toDouble()))
//                .radius(60.0)
//                .strokeWidth(3f)
//        )
//    }
}