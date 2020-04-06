package com.androidstudy.huaweihms

import com.facebook.stetho.Stetho

class HuaweiDebug : Huawei() {
    override fun onCreate() {

        super.onCreate()

        if (BuildConfig.DEBUG) {
            Stetho.initializeWithDefaults(this)
        }
    }
}