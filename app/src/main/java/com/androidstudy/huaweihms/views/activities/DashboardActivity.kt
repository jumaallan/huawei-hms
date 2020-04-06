package com.androidstudy.huaweihms.views.activities

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import com.androidstudy.huaweihms.R
import com.androidstudy.huaweihms.utils.makeStatusBarTransparent
import com.androidstudy.huaweihms.utils.setMarginTop
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.huawei.hms.maps.HuaweiMap
import com.huawei.hms.maps.OnMapReadyCallback
import kotlinx.android.synthetic.main.activity_dashboard.*


class DashboardActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var hMap: HuaweiMap

    private val MAPVIEW_BUNDLE_KEY =
        "CV6vAyCd7futDo8W7C+mAlfmnLp4tgha60k6h9guOS8VeVMbW6x+V4CsthLL+Hs/uMCU8q/gaAgb29Kbdg0lBJUTrKPu"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        makeStatusBarTransparent()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.content_container)) { _, insets ->
            findViewById<FloatingActionButton>(R.id.cardViewUserProfile).setMarginTop(insets.systemWindowInsetTop)
            insets.consumeSystemWindowInsets()
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
        Log.d("TAG", "onMapReady: ")
        hMap = map
    }
}
