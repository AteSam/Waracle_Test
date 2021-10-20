package com.example.waracleandroidtest.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.waracleandroidtest.R

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(android.R.id.content, CakeListFragment.newInstance())
                .commit()
        }
    }
}