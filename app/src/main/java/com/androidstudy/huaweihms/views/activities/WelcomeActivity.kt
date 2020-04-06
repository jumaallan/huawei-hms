package com.androidstudy.huaweihms.views.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.androidstudy.huaweihms.R
import com.androidstudy.huaweihms.utils.makeStatusBarTransparent

class WelcomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)

        makeStatusBarTransparent()

    }
}
