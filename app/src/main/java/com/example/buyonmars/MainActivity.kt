package com.example.buyonmars

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.mars_list_options, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.all_view_option -> {
                Log.i("sky", "onOptionsItemSelected: all_view_option")
                true
            }
            R.id.buy_view_option -> {
                Log.i("sky", "onOptionsItemSelected: buy_view_option")
                true
            }
            R.id.rent_view_option -> {
                Log.i("sky", "onOptionsItemSelected: rent_view_option")
                true
            }
            R.id.favorites_view_option -> {
                Log.i("sky", "onOptionsItemSelected: favorites_view_option")
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}

