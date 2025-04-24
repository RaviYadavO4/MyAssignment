package com.example.myassignment.ui.auth

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.example.myassignment.MainActivity
import com.example.myassignment.R
import com.example.myassignment.databinding.ActivitySigninBinding
import com.example.myassignment.persistence.Prefs
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.tasks.Task
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class SigninActivity : AppCompatActivity(R.layout.activity_signin) {
    private lateinit var binding:ActivitySigninBinding

    private val authViewModel: AuthViewModel by viewModel()
    private val prefs: Prefs by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySigninBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupObserver()

        binding.googleSignInButton.setOnClickListener {
            authViewModel.showLoader.value = true
            val signInIntent = authViewModel.getGoogleSignInClient().signInIntent
            googleSignInResult.launch(signInIntent)
        }
        
    }

    private fun setupObserver(){
        authViewModel.showLoader.observe(this, Observer {

            binding.progressCircular.visibility = if (it) View.VISIBLE else View.GONE
        })
    }

    private val googleSignInResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            if (task.isSuccessful) {
                authViewModel.showLoader.value = false
                val account = task.result
                account?.idToken?.let { idToken ->
                    authViewModel.signInWithGoogle(idToken).observe(this) { user ->
                        if (user != null) {
                            prefs.setIsLogin(data = true)
                            startActivity(Intent(this@SigninActivity,MainActivity::class.java))
                            finish()
                        } else {

                        }
                    }
                }
            } else {
                authViewModel.showLoader.value = false
                Toast.makeText(this, "Google Sign-in failed", Toast.LENGTH_SHORT).show()
            }
        }
}