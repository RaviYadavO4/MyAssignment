package com.example.myassignment.ui.splash


import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.myassignment.MainActivity
import com.example.myassignment.R
import com.example.myassignment.databinding.ActivitySplashBinding
import com.example.myassignment.persistence.Prefs
import com.example.myassignment.ui.auth.SigninActivity
import com.zplesac.connectionbuddy.ConnectionBuddy
import com.zplesac.connectionbuddy.interfaces.ConnectivityChangeListener
import com.zplesac.connectionbuddy.models.ConnectivityEvent
import org.koin.android.ext.android.inject

class SplashActivity : AppCompatActivity(R.layout.activity_splash), ConnectivityChangeListener {
    private lateinit var binding:ActivitySplashBinding

    private val prefs: Prefs by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ConnectionBuddy.getInstance()
            .configuration.networkEventsCache.clearLastNetworkState(this)

        if (prefs.getIsLogin()){
            startActivity(Intent(this,MainActivity::class.java))
        }else{
            startActivity(Intent(this,SigninActivity::class.java))
        }
    }

    override fun onStart() {
        super.onStart()
        ConnectionBuddy.getInstance().registerForConnectivityEvents(this, true, this)
    }

    override fun onStop() {
        super.onStop()
        ConnectionBuddy.getInstance().unregisterFromConnectivityEvents(this)
    }

    override fun onConnectionChange(event: ConnectivityEvent?) {
    }
}