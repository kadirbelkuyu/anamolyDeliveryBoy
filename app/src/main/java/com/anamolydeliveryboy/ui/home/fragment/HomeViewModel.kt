package com.anamolydeliveryboy.ui.home.fragment

import android.Manifest
import android.app.Activity
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.*
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import com.anamolydeliveryboy.repository.ProjectRepository
import com.anamolydeliveryboy.response.CommonResponse

class HomeViewModel : ViewModel() {
    val projectRepository = ProjectRepository()

    val permissionLiveData = MutableLiveData<Boolean>()
    val locationSettingUpdateLiveData = MutableLiveData<LocationSettingsResult>()
    val locationUnableLiveData = MutableLiveData<Boolean>()

    fun makeGetHomeData(params: HashMap<String, String>): LiveData<CommonResponse?> {
        return projectRepository.getHomeData(params)
    }

    fun makePickupOrder(params: HashMap<String, String>): LiveData<CommonResponse?> {
        return projectRepository.pickupOrder(params)
    }

    fun makeCompleteOrder(params: HashMap<String, String>): LiveData<CommonResponse?> {
        return projectRepository.completeOrder(params)
    }

    fun checkPermission(context: Context) {
        Dexter.withActivity(context as Activity)
            .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
            .withListener(object : PermissionListener {
                override fun onPermissionGranted(response: PermissionGrantedResponse?) {
                    //mMap.isMyLocationEnabled = true
                    permissionLiveData.value = true
                }

                override fun onPermissionRationaleShouldBeShown(
                    permission: PermissionRequest?,
                    token: PermissionToken?
                ) {
                    token?.continuePermissionRequest()
                }

                override fun onPermissionDenied(response: PermissionDeniedResponse?) {
                    permissionLiveData.value = false
                }
            }).check()
    }

    fun checkLocationSettingsRequest(context: Context) {
        val googleApiClient = GoogleApiClient.Builder(context)
            .addApi(LocationServices.API)
            .build()
        googleApiClient.connect()
        val locationRequest = LocationRequest.create()
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.interval = 100
        locationRequest.fastestInterval = 100
        locationRequest.numUpdates = 1
        val builder =
            LocationSettingsRequest.Builder().addLocationRequest(locationRequest)
        builder.setAlwaysShow(true)
        val result =
            LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build())
        result.setResultCallback { result: LocationSettingsResult ->
            val status = result.status
            locationSettingUpdateLiveData.value = result
            when (status.statusCode) {
                LocationSettingsStatusCodes.SUCCESS -> {
                    locationUnableLiveData.value = true
                }
                LocationSettingsStatusCodes.RESOLUTION_REQUIRED -> {
                    locationUnableLiveData.value = false
                }
                LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> {
                    locationUnableLiveData.value = false
                }
            }
        }
    }

}