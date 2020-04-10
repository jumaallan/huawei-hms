package com.androidstudy.huaweihms.views.activities

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.view.ViewCompat
import com.androidstudy.huaweihms.R
import com.androidstudy.huaweihms.utils.makeStatusBarTransparent
import com.androidstudy.huaweihms.utils.setMarginTop
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.huawei.hms.maps.CameraUpdateFactory
import com.huawei.hms.maps.HuaweiMap
import com.huawei.hms.maps.OnMapReadyCallback
import com.huawei.hms.maps.model.*
import kotlinx.android.synthetic.main.activity_map.*
import timber.log.Timber


class MapActivity : AppCompatActivity(), OnMapReadyCallback {

    private var huaweiMap: HuaweiMap? = null
    private var mMarker: Marker? = null
    private var mCircle: Circle? = null

    private val LAT_LNG = LatLng(31.2304, 121.4737)

    private val MAPVIEW_BUNDLE_KEY =
        "CV6vAyCd7futDo8W7C+mAlfmnLp4tgha60k6h9guOS8VeVMbW6x+V4CsthLL+Hs/uMCU8q/gaAgb29Kbdg0lBJUTrKPu"

//    26a53a8efd158708e26f3236e2616b72d0086ccd86a1c71aea1099a74302b43b

    private val RUNTIME_PERMISSIONS = arrayOf(
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.INTERNET
    )

    private val REQUEST_CODE = 100

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)

        makeStatusBarTransparent()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.content_container)) { _, insets ->
            findViewById<FloatingActionButton>(R.id.cardViewUserProfile).setMarginTop(insets.systemWindowInsetTop)
            insets.consumeSystemWindowInsets()
        }

        if (!hasPermissions(this, *RUNTIME_PERMISSIONS)) {
            ActivityCompat.requestPermissions(this, RUNTIME_PERMISSIONS, REQUEST_CODE)
        }

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
        Timber.d("onMapReady: ")
        huaweiMap = map
        huaweiMap!!.isMyLocationEnabled = true

        // move camera by CameraPosition param ,latlong and zoom params can set here
        val build = CameraPosition.Builder().target(LatLng(60.0, 60.0)).zoom(5f).build()

        val cameraUpdate = CameraUpdateFactory.newCameraPosition(build)
        huaweiMap!!.animateCamera(cameraUpdate)
        huaweiMap!!.setMaxZoomPreference(5F)
        huaweiMap!!.setMinZoomPreference(2F)

        // mark can be add by HuaweiMap
        mMarker = huaweiMap!!.addMarker(
            MarkerOptions().position(LAT_LNG)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_launcher_background))
                .clusterable(true)
        )

        mMarker!!.showInfoWindow()

        // circle can be add by HuaweiMap
        mCircle = huaweiMap!!.addCircle(
            CircleOptions().center(LatLng(60.0, 60.0)).radius(5000.0).fillColor(Color.GREEN)
        )
    }

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
