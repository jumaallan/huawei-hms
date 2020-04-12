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
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.view.ViewCompat
import com.androidstudy.huaweihms.BuildConfig
import com.androidstudy.huaweihms.R
import com.androidstudy.huaweihms.utils.makeStatusBarTransparent
import com.androidstudy.huaweihms.utils.setMarginTop
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
import com.huawei.hms.maps.HuaweiMap.OnMarkerDragListener
import com.huawei.hms.maps.OnMapReadyCallback
import com.huawei.hms.maps.model.*
import kotlinx.android.synthetic.main.activity_map.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

class MapActivity : AppCompatActivity(), OnMapReadyCallback {

    private var huaweiMap: HuaweiMap? = null
    private var mMarker: Marker? = null
    private var mCircle: Circle? = null

    private var LAT_LONG = LatLng(37.0144, 1.1018)

    private lateinit var analytics: HiAnalyticsInstance

    // the callback of the request
    var mLocationCallback: LocationCallback? = null
    var mLocationRequest: LocationRequest? = null
    private var fusedLocationProviderClient: FusedLocationProviderClient? = null
    private var settingsClient: SettingsClient? = null

    private val MAPVIEW_BUNDLE_KEY = BuildConfig.MAPVIEW_BUNDLE_KEY

    private val RUNTIME_PERMISSIONS = arrayOf(
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.INTERNET
    )

    private val REQUEST_CODE = 100

    private val mapViewModel: MapViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)

        // Make Map Full Screen
        makeStatusBarTransparent()

        // Anchor the profile card on top right side
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.content_container)) { _, insets ->
            findViewById<FloatingActionButton>(R.id.cardViewUserProfile).setMarginTop(insets.systemWindowInsetTop)
            insets.consumeSystemWindowInsets()
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

        // get location updates here
        requestLocationUpdatesWithCallback()

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

                            LAT_LONG = LatLng(location.latitude, location.longitude)
                        }
                    }
                }
            }

            override fun onLocationAvailability(locationAvailability: LocationAvailability?) {
                if (locationAvailability != null) {
                    val flag = locationAvailability.isLocationAvailable
                    Timber.i(
                        "onLocationAvailability isLocationAvailable:$flag"
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

//        lifecycleScope.launch {
//            mapViewModel.getLocationDescriptions().observe(this) {
//                setUpViews(it)
//            }
//        }
    }

    private fun setUpViews(it: Any) {

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
        huaweiMap!!.isBuildingsEnabled = true
        huaweiMap!!.isTrafficEnabled = true
//        huaweiMap!!.setOnCameraMoveListener(this);

        huaweiMap!!.setOnInfoWindowClickListener { marker ->
            Toast.makeText(
                applicationContext,
                "onInfoWindowClick:" + marker.title,
                Toast.LENGTH_SHORT
            ).show()
        }

//         move camera by CameraPosition param ,latlong and zoom params can set here
//         we can use the location from the location kit
        val build = CameraPosition.Builder().target(LAT_LONG).zoom(15f).build()

//         setup the zoom preferences on the map
        val cameraUpdate = CameraUpdateFactory.newCameraPosition(build)
        huaweiMap!!.animateCamera(cameraUpdate)
        huaweiMap!!.setMaxZoomPreference(20F)
        huaweiMap!!.setMinZoomPreference(10f)

        huaweiMap!!.animateCamera(cameraUpdate)

        // marker can be add by HuaweiMap
        mMarker = huaweiMap!!.addMarker(
            MarkerOptions().position(LAT_LONG)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_map_marker))
                .clusterable(true)
        )

        val isInfoWindowShown = mMarker!!.isInfoWindowShown
        if (isInfoWindowShown) {
            mMarker!!.hideInfoWindow()
        } else {
            mMarker!!.showInfoWindow()
        }

        mMarker!!.isDraggable = true
        huaweiMap!!.setOnMarkerDragListener(object : OnMarkerDragListener {
            override fun onMarkerDragStart(marker: Marker?) {
                Timber.i("onMarkerDragStart: ")
            }

            override fun onMarkerDrag(marker: Marker?) {
                Timber.i("onMarkerDrag: ")
            }

            override fun onMarkerDragEnd(marker: Marker?) {
                Timber.i("onMarkerDragEnd: ")
                mMarker = huaweiMap!!.addMarker(
                    MarkerOptions().position(LAT_LONG)
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_map_marker))
                        .clusterable(true)
                )
            }
        })

        huaweiMap!!.setOnMarkerClickListener { marker ->
            Toast.makeText(
                applicationContext,
                "onMarkerClick:" + marker.title,
                Toast.LENGTH_SHORT
            ).show()
            false
        }
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

