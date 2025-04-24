package com.example.myassignment.ui.profile

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import coil.load
import com.example.myassignment.R
import com.example.myassignment.data.db.entity.User
import com.example.myassignment.databinding.FragmentProfileBinding
import com.example.myassignment.ui.splash.SplashActivity
import com.hadiyarajesh.flower.Resource
import org.koin.androidx.viewmodel.ext.android.viewModel


class ProfileFragment : Fragment(R.layout.fragment_profile) {
    private var _binding:FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModel<ProfileViewModel>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProfileBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.switchNotification.isChecked = viewModel.areNotificationsEnabled()
        setupObserver()
        initClick()
    }

    private fun initClick(){
        binding.switchTheme.setOnCheckedChangeListener { _, isChecked ->
            viewModel.toggleTheme(isChecked)
        }
        binding.switchNotification.setOnCheckedChangeListener { _, isChecked ->
            viewModel.enableNotifications(isChecked)
            Toast.makeText(requireContext(), "Notifications ${if (isChecked) "enabled" else "disabled"}", Toast.LENGTH_SHORT).show()
        }
        binding.cvLogout.setOnClickListener {
            viewModel.logout()
            startActivity(Intent(requireContext(),SplashActivity::class.java))
            requireActivity().finish()
        }
    }

    private fun setupObserver() {
        viewModel.user.observe(viewLifecycleOwner, userObserver)
        viewModel.showLoader.observe(viewLifecycleOwner, Observer {
            binding.progressCircular.visibility = if (it) View.VISIBLE else View.GONE
        })

        viewModel.isDarkMode.observe(viewLifecycleOwner) { isDark ->
            binding.switchTheme.isChecked = isDark
        }
    }

    private val userObserver: Observer<Resource<User>> = Observer { resource ->
        when (resource.status) {
            Resource.Status.SUCCESS -> {
                viewModel.showLoader.value = false
                resource.data?.let { user ->
                    Log.d("User","---$user")
                    binding.lblName.text = user.displayName
                    binding.lblEmail.text = user.email

                    binding.imgProfile.load(user.photoUrl) {
                        crossfade(true)
                        placeholder(R.mipmap.ic_launcher)
                        error(R.mipmap.ic_launcher)
                    }

                }
            }

            Resource.Status.LOADING -> {
                Log.d("Phones","fetching categories")
            }

            Resource.Status.ERROR -> {
                viewModel.showLoader.value = false
                Log.d("Phones","error in fetching categories: ${resource.message}")
            }
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}