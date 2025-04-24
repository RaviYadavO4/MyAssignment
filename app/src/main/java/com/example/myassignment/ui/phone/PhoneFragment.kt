package com.example.myassignment.ui.phone


import android.Manifest
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import com.example.myassignment.R
import com.example.myassignment.data.db.entity.Phone
import com.example.myassignment.databinding.FragmentPhoneBinding
import com.example.myassignment.databinding.ViewHolderItemPhoneBinding
import com.example.myassignment.utils.GenericAdapter
import com.hadiyarajesh.flower.Resource
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.single.PermissionListener
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel


class PhoneFragment : Fragment() {
    private var _binding:FragmentPhoneBinding?= null
    private val binding get() = _binding!!

    private val viewModel by viewModel<PhoneViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPhoneBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requestNotificationPermission()
        setupObserver()


    }

    private fun setupObserver() {
        viewModel.phones.observe(viewLifecycleOwner, categoriesObserver)
        viewModel.showLoader.observe(viewLifecycleOwner, Observer {

            binding.progressCircular.visibility = if (it) View.VISIBLE else View.GONE
        })
    }

    private val categoriesObserver: Observer<Resource<List<Phone>>> = Observer { resource ->

        when (resource.status) {

            Resource.Status.SUCCESS -> {
                viewModel.showLoader.value = false
                resource.data?.let { phones ->
                    Log.d("phones","---$phones")
                    getPhoneAdapter(phones)
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

    private fun getPhoneAdapter(list: List<Phone>) {
        val adapter = GenericAdapter(
            list,
            { inflater, parent, attachToParent ->
                ViewHolderItemPhoneBinding.inflate(inflater, parent, attachToParent)
            },
            { binding, itemList, position ->
                binding.lblName.text = itemList.name
                binding.imgDelete.setOnClickListener {
                    showDeleteConfirmationDialog {
                        viewModel.fetchFcmToken()
                        viewModel.fcmToken.observe(this) { token ->
                            token?.let {
                                Log.d("FCM", "Token: $it")
                                viewModel.deletePhone(phone = itemList, fcmToken = it)
                            }
                        }
                    }


                }


            }
        )
        binding.rvPhone.adapter = adapter
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun requestNotificationPermission(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            Dexter.withContext(requireContext())
                .withPermission(Manifest.permission.POST_NOTIFICATIONS)
                .withListener(object : PermissionListener {
                    override fun onPermissionGranted(response: PermissionGrantedResponse?) {
                        // Permission granted – notifications will work
                    }

                    override fun onPermissionDenied(response: PermissionDeniedResponse?) {
                        requestNotificationPermission()
                    }

                    override fun onPermissionRationaleShouldBeShown(
                        permission: com.karumi.dexter.listener.PermissionRequest?,
                        token: PermissionToken?
                    ) {
                        token?.continuePermissionRequest()
                    }
                }).check()
        }

    }

    private fun showDeleteConfirmationDialog(onConfirm: () -> Unit) {
        AlertDialog.Builder(requireContext())
            .setTitle("Delete Item")
            .setMessage("Are you sure you want to delete this item?")
            .setPositiveButton("Delete") { _, _ ->
                onConfirm()
            }
            .setNegativeButton("Cancel", null)
            .show()
    }
}