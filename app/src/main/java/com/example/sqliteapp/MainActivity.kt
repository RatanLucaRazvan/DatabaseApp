package com.example.sqliteapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        loadFragment()
    }

    private fun loadFragment() {
        val fm: FragmentManager = this.supportFragmentManager
        val ft: FragmentTransaction = fm.beginTransaction()

        ft.add(R.id.fragmentHolder, AppFragment())
        ft.commit()
    }
}