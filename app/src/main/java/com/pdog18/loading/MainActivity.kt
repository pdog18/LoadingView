package com.pdog18.loading

import android.app.Activity
import android.os.Bundle
import android.os.Handler
import kotlinx.android.synthetic.main.activity_main.*
import com.pdog.loading.LoadingView

class MainActivity : Activity(), LoadingView {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        startLoading()
        Handler().postDelayed(Runnable {
            loadingFinish()
        }, 5000L)

        button.setOnClickListener {
            startLoading()
            Handler().postDelayed(Runnable {
                loadingFinish()
            }, 5000L)
        }
    }
}
