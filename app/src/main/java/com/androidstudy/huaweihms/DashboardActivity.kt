package com.androidstudy.huaweihms

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.huawei.hms.maps.HuaweiMap
import com.huawei.hms.maps.OnMapReadyCallback
import kotlinx.android.synthetic.main.activity_dashboard.*


class DashboardActivity : AppCompatActivity(), OnMapReadyCallback {

    private var hMap: HuaweiMap? = null

    private val MAPVIEW_BUNDLE_KEY =
        "CV6u79aF4HBYRV3ScFR+vPzssI0yD/cPIIhUzLLwp/6BMA+fohl4PRownMUXa9FT7KHNR/Ba6+syfUitY1ZLWzg65qmQ"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        makeStatusBarTransparent()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.content_container)) { _, insets ->
            findViewById<FloatingActionButton>(R.id.fab2).setMarginTop(insets.systemWindowInsetTop)
            insets.consumeSystemWindowInsets()
        }

        var mapViewBundle: Bundle? = null
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAPVIEW_BUNDLE_KEY)
        }
        mapView.onCreate(mapViewBundle)
        mapView.getMapAsync(this)
    }

    override fun onMapReady(map: HuaweiMap?) {
        hMap = map
    }

    override fun onStart() {
        super.onStart()
        mapView.onStart()
    }

    override fun onStop() {
        super.onStop()
        mapView.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView.onDestroy()
    }

    override fun onPause() {
        mapView.onPause()
        super.onPause()
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }
}
