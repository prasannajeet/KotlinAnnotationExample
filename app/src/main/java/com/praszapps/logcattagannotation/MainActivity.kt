package com.praszapps.logcattagannotation

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log

@LogcatTag
class MainActivity : AppCompatActivity() {

    val TAG by lazy {
        MainActivityTagBuilder.getTag()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Log.d(TAG, "Hello")
    }
}
