package com.farouqannajaby.olsera.ui.map

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.farouqannajaby.olsera.R
import com.farouqannajaby.olsera.databinding.ActivityMapsBinding
import com.farouqannajaby.olsera.ui.home.insert.AddUpdateOfficeActivity
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.MarkerOptions
import java.io.IOException
import java.util.*


class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private var lat : String = ""
    private var long : String = ""
    private var address : String = ""
    private val TAG = "MapsActivity"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        title = resources.getString(R.string.title_map)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.uiSettings.isIndoorLevelPickerEnabled = true
        mMap.uiSettings.isCompassEnabled = true
        mMap.uiSettings.isMapToolbarEnabled = true

        mMap.setOnMapLongClickListener { latLng ->
            mMap.addMarker(
                MarkerOptions()
                    .position(latLng)
                    .title("Lokasi Anda")
                    .snippet("Lat: ${latLng.latitude} Long: ${latLng.longitude}")
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA))

            )
            lat = latLng.latitude.toString()
            long = latLng.longitude.toString()
        }

        googleMap.setOnMapClickListener { point ->
            Toast.makeText(
                this,
                point.latitude.toString() + ", " + point.longitude,
                Toast.LENGTH_SHORT
            ).show()
        }

        getMyLocation()
    }

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                getMyLocation()
            }
        }

    private fun getMyLocation() {
        if (ContextCompat.checkSelfPermission(
                this.applicationContext,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            mMap.isMyLocationEnabled = true
        } else {
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    private fun getAddressName(lat: Double, lon: Double): String? {
        var addressName: String? = null
        val geocoder = Geocoder(this@MapsActivity, Locale.getDefault())
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
        val geocoder = Geocoder(this@MapsActivity, Locale.getDefault())
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
        val geocoder = Geocoder(this@MapsActivity, Locale.getDefault())
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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
//            val intent = Intent(this@MapsActivity, AddUpdateOfficeActivity::class.java)
//            intent.putExtra(AddUpdateOfficeActivity.EXTRA_LATITUDE, lat)
//            intent.putExtra(AddUpdateOfficeActivity.EXTRA_LONGTITUDE, long)
//            intent.putExtra(AddUpdateOfficeActivity.EXTRA_ADDRESS,
//                getAddressName(lat.toDouble(),long.toDouble()))
//            intent.putExtra(AddUpdateOfficeActivity.EXTRA_CITY,
//                getCity(lat.toDouble(),long.toDouble()))
//            intent.putExtra(AddUpdateOfficeActivity.EXTRA_POSTAL,
//                getPostalCode(lat.toDouble(),long.toDouble()))
//            startActivity(intent)
        }
        return super.onOptionsItemSelected(item)
    }

}