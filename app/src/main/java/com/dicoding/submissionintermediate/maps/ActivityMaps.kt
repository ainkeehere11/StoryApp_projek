package com.dicoding.submissionintermediate.maps

import android.content.Context
import android.content.pm.PackageManager
import android.content.res.Resources
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.dicoding.submissionintermediate.R

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.dicoding.submissionintermediate.databinding.ActivityMaps2Binding
import com.dicoding.submissionintermediate.viewmodel.AutentifikasiPref
import com.dicoding.submissionintermediate.viewmodel.FactoryViewModel
import com.dicoding.submissionintermediate.viewmodel.Story
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.Marker

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class ActivityMaps : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMaps2Binding
    private lateinit var pref: AutentifikasiPref
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private val mapsViewModel: ViewModelMaps by viewModels {
        FactoryViewModel(pref, this)
    }

    private var currentMarker = mutableListOf<Marker>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        pref = AutentifikasiPref.getInstance(dataStore)

        binding = ActivityMaps2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        mapsViewModel.listStories.observe(this) { stories ->
            if (::mMap.isInitialized) {
                addMarkers(stories)
            }
        }

        mapsViewModel.getToken().observe(this) { token ->
            mapsViewModel.listStory(token)
        }

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

        val indonesia = LatLng(-2.548926, 118.014863)
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(indonesia, 5f))

        getMyLocation()
        setMapStyle()
    }

    private fun addMarkers(stories: List<Story>) {
        currentMarker.forEach { it.remove() }
        currentMarker.clear()
        val boundsBuilder = LatLngBounds.Builder()

        stories.forEach { story ->
            val coordinate = LatLng(story.lat, story.lon)
            val option = MarkerOptions()
                .position(coordinate)
                .title(story.name)
                .snippet(story.description)
            val marker = mMap.addMarker(option)
            marker?.let {
                currentMarker.add(it)
                boundsBuilder.include(it.position)
            }
        }

        if (currentMarker.isNotEmpty()) {
            val bounds: LatLngBounds = boundsBuilder.build()
            mMap.setOnMapLoadedCallback {
                mMap.animateCamera(
                    CameraUpdateFactory.newLatLngBounds(
                        bounds,
                        resources.displayMetrics.widthPixels,
                        resources.displayMetrics.heightPixels,
                        100
                    )
                )
            }
        }
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
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            mMap.isMyLocationEnabled = true
        } else {
            requestPermissionLauncher.launch(android.Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    private fun setMapStyle() {
        try {
            val success =
                mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.maps_style))
            if (!success) {
                Log.e("MapsActivity", "Style parsing failed.")
            }
        } catch (exception: Resources.NotFoundException) {
            Log.e("MapsActivity", "Can't find style. Error: ", exception)
        }
    }
}