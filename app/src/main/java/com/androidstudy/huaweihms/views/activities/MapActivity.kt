package com.androidstudy.huaweihms.views.activities

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.IntentSender.SendIntentException
import android.content.pm.PackageManager
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.view.ViewCompat
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import com.androidstudy.huaweihms.BuildConfig
import com.androidstudy.huaweihms.R
import com.androidstudy.huaweihms.data.remote.LocationDataRequest
import com.androidstudy.huaweihms.di.injectFeature
import com.androidstudy.huaweihms.utils.makeStatusBarTransparent
import com.androidstudy.huaweihms.utils.setMarginTop
import com.androidstudy.huaweihms.views.adapter.NearbyRecyclerViewAdapter
import com.androidstudy.huaweihms.views.viewmodel.MapViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.huawei.hms.analytics.HiAnalytics
import com.huawei.hms.analytics.HiAnalyticsInstance
import com.huawei.hms.analytics.HiAnalyticsTools
import com.huawei.hms.common.ApiException
import com.huawei.hms.common.ResolvableApiException
import com.huawei.hms.location.*
import com.huawei.hms.maps.CameraUpdateFactory
import com.huawei.hms.maps.HuaweiMap
import com.huawei.hms.maps.OnMapReadyCallback
import com.huawei.hms.maps.model.LatLng
import com.huawei.hms.maps.model.Marker
import com.huawei.hms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.activity_map.*
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

class MapActivity : AppCompatActivity(), OnMapReadyCallback {

    // huawei maps && huawei marker
    private var huaweiMap: HuaweiMap? = null
    private var huaweiMarker: Marker? = null

    // setting up analytics
    private lateinit var analytics: HiAnalyticsInstance

    // the callback of the request
    private var mLocationCallback: LocationCallback? = null
    private var mLocationRequest: LocationRequest? = null
    private var fusedLocationProviderClient: FusedLocationProviderClient? = null
    private var settingsClient: SettingsClient? = null

    // retrieve map key from secrets
    private val MAPVIEW_BUNDLE_KEY = BuildConfig.MAPVIEW_BUNDLE_KEY

    // permissions
    private val RUNTIME_PERMISSIONS = arrayOf(
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.INTERNET
    )

    private val REQUEST_CODE = 100

    // maps view model
    private val mapViewModel: MapViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)

        injectFeature()

        // Make Map Full Screen
        makeStatusBarTransparent()

        // Anchor the profile card on top right side
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.content_container)) { _, insets ->
            findViewById<FloatingActionButton>(R.id.cardViewUserProfile).setMarginTop(insets.systemWindowInsetTop)
            insets.consumeSystemWindowInsets()
        }

        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        recyclerView.layoutManager = layoutManager
        val snapHelper = PagerSnapHelper()
        recyclerView.onFlingListener = null
        snapHelper.attachToRecyclerView(recyclerView)

        val nearbyAdapter = NearbyRecyclerViewAdapter { modules, position -> }
        recyclerView.adapter = nearbyAdapter

        indicator.attachToRecyclerView(recyclerView, snapHelper)
        nearbyAdapter.registerAdapterDataObserver(indicator.adapterDataObserver)

        // observe the nearby places here - if we have something, lets show them
        mapViewModel.locationDescriptions.observe(this) {
            if (it.isEmpty()) {
                linearLayout.visibility = View.GONE
            } else {
                linearLayout.visibility = View.VISIBLE
                nearbyAdapter.submitList(it)
            }
        }

        // Analytics Kit
        HiAnalyticsTools.enableLog()
        analytics = HiAnalytics.getInstance(this)
        analytics.setAnalyticsEnabled(true)
        analytics.regHmsSvcEvent()

        // Check for permissions
        if (!hasPermissions(this, *RUNTIME_PERMISSIONS)) {
            ActivityCompat.requestPermissions(this, RUNTIME_PERMISSIONS, REQUEST_CODE)
        }

        // Setting up location kit
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        settingsClient = LocationServices.getSettingsClient(this)
        mLocationRequest = LocationRequest()
        mLocationRequest!!.interval = 10000
        mLocationRequest!!.priority = LocationRequest.PRIORITY_HIGH_ACCURACY

        // location updates callback
        mLocationCallback = object : LocationCallback() {
            @SuppressLint("BinaryOperationInTimber")
            override fun onLocationResult(locationResult: LocationResult?) {
                if (locationResult != null) {
                    val locations: List<Location> = locationResult.locations
                    if (locations.isNotEmpty()) {
                        for (location in locations) {
                            Timber.i(
                                "onLocationResult location[Longitude,Latitude,Accuracy]:" + location.longitude
                                    .toString() + "," + location.latitude
                                    .toString() + "," + location.accuracy
                            )

                            // we should call the huawei maps here and update the marker and location too
                            if (huaweiMap == null) {
                                return
                            }

                            huaweiMap!!.moveCamera(
                                CameraUpdateFactory.newLatLngZoom(
                                    LatLng(
                                        location.latitude,
                                        location.longitude
                                    ), 16f
                                )
                            )

                            huaweiMap!!.setOnMapClickListener { latLng ->

                                if (huaweiMarker != null) {
                                    huaweiMarker!!.remove()
                                }

                                huaweiMarker = huaweiMap!!.addMarker(
                                    MarkerOptions().position(
                                        latLng
                                    )
                                )

                                Toast.makeText(
                                    applicationContext,
                                    "onMapClick :$latLng",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }

                            // marker can be add by HuaweiMap
                            huaweiMarker = huaweiMap!!.addMarker(
                                MarkerOptions().position(
                                    LatLng(
                                        location.latitude,
                                        location.longitude
                                    )
                                )
                            )

                            val isInfoWindowShown = huaweiMarker!!.isInfoWindowShown
                            if (isInfoWindowShown) {
                                huaweiMarker!!.hideInfoWindow()
                            } else {
                                huaweiMarker!!.showInfoWindow()
                            }

                            huaweiMap!!.setOnMarkerClickListener { marker ->

                                // try to load nearby places
                                lifecycleScope.launch {
                                    mapViewModel.getReverseGeoCode(
                                        com.androidstudy.huaweihms.data.remote.LocationRequest(
                                            LocationDataRequest(
                                                marker.position.latitude,
                                                marker.position.longitude
                                            ),
                                            "en",
                                            "KE",
                                            true
                                        )
                                    )
                                }

                                Toast.makeText(
                                    applicationContext,
                                    "Fetching nearby places. Please wait...",
                                    Toast.LENGTH_SHORT
                                ).show()
                                false
                            }
                        }
                    }
                }
            }

            override fun onLocationAvailability(locationAvailability: LocationAvailability?) {
                if (locationAvailability != null) {
                    val flag = locationAvailability.isLocationAvailable
                    Timber.i(
                        "onLocationAvailability isLocationAvailable : $flag"
                    )
                }
            }
        }

        // Setting up map
        var mapViewBundle: Bundle? = null
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAPVIEW_BUNDLE_KEY)
        }
        map.onCreate(mapViewBundle)
        map.getMapAsync(this)

    }

    override fun onStart() {
        super.onStart()
        map!!.onStart()
    }

    override fun onStop() {
        super.onStop()
        map!!.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
        map!!.onDestroy()
        analytics.unRegHmsSvcEvent()
        removeLocationUpdatesWithCallback()
    }

    override fun onPause() {
        map!!.onPause()
        super.onPause()
    }

    override fun onResume() {
        super.onResume()
        map!!.onResume()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        map!!.onLowMemory()
    }

    override fun onMapReady(map: HuaweiMap) {

        huaweiMap = map
        huaweiMap!!.isMyLocationEnabled = true
        huaweiMap!!.uiSettings.isMyLocationButtonEnabled = true
        huaweiMap!!.uiSettings.setAllGesturesEnabled(true)
        huaweiMap!!.isBuildingsEnabled = true
        huaweiMap!!.isTrafficEnabled = true

        huaweiMap!!.setMinZoomPreference(6.0f)
        huaweiMap!!.setMaxZoomPreference(14.0f)

        // get location updates here
        requestLocationUpdatesWithCallback()

    }

    private fun requestLocationUpdatesWithCallback() {
        try {
            val builder = LocationSettingsRequest.Builder()
            builder.addLocationRequest(mLocationRequest)
            val locationSettingsRequest = builder.build()
            // check devices settings before request location updates.
            settingsClient!!.checkLocationSettings(locationSettingsRequest)
                .addOnSuccessListener {
                    Timber.i("check location settings success")
                    fusedLocationProviderClient!!.requestLocationUpdates(
                        mLocationRequest,
                        mLocationCallback,
                        Looper.getMainLooper()
                    )
                        .addOnSuccessListener {
                            Timber.i("requestLocationUpdatesWithCallback onSuccess")
                        }
                        .addOnFailureListener { e ->
                            Timber.e("requestLocationUpdatesWithCallback onFailure: %s", e.message)
                        }
                }
                .addOnFailureListener { e ->
                    Timber.e("checkLocationSetting onFailure: %s", e.message)
                    when ((e as ApiException).statusCode) {
                        LocationSettingsStatusCodes.RESOLUTION_REQUIRED -> try {
                            val rae = e as ResolvableApiException
                            rae.startResolutionForResult(
                                this,
                                0
                            )
                        } catch (sie: SendIntentException) {
                            Timber.e("PendingIntent unable to execute request.")
                        }
                    }
                }
        } catch (e: java.lang.Exception) {
            Timber.e("requestLocationUpdatesWithCallback exception: %s", e.message)
        }
    }

    private fun removeLocationUpdatesWithCallback() {
        try {
            fusedLocationProviderClient!!.removeLocationUpdates(mLocationCallback)
                .addOnSuccessListener {
                    Timber.d(
                        "removeLocationUpdatesWithCallback onSuccess"
                    )
                }
                .addOnFailureListener { e ->
                    Timber.e(
                        "removeLocationUpdatesWithCallback onFailure: %s", e.message
                    )
                }
        } catch (e: Exception) {
            Timber.e(
                "removeLocationUpdatesWithCallback exception: %s", e.message
            )
        }
    }

    // check for permissions
    private fun hasPermissions(
        context: Context,
        vararg permissions: String
    ): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && permissions != null) {
            for (permission in permissions) {
                if (ActivityCompat.checkSelfPermission(
                        context,
                        permission
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    return false
                }
            }
        }
        return true
    }
}

