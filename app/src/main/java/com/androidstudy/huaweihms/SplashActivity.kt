package com.androidstudy.huaweihms

import android.animation.Animator
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_splash.*
import timber.log.Timber

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        makeStatusBarTransparent()

        animation_view.addAnimatorListener(object :
            Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator) {
                Timber.d("Animation started")
            }

            override fun onAnimationEnd(animation: Animator) {
                Timber.d("Animation ended")
                try {
                    val intent = Intent(applicationContext, WelcomeActivity::class.java)
                    startActivity(intent)
                    finish()
                } catch (ex: Exception) {
                    ex.toString()
                }
            }

            override fun onAnimationCancel(animation: Animator) {
                Timber.d("Animation cancelled")
            }

            override fun onAnimationRepeat(animation: Animator) {
                Timber.d("Animation repeated")
            }
        })
    }
}
