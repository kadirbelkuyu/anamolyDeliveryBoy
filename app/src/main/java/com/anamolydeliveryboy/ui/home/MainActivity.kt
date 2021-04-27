package com.anamolydeliveryboy.ui.home

import Config.BaseURL
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.android.gms.location.LocationSettingsStatusCodes
import com.onesignal.OneSignal
import com.anamolydeliveryboy.CommonActivity
import com.anamolydeliveryboy.R
import com.anamolydeliveryboy.model.SideMenuModel
import com.anamolydeliveryboy.response.CommonResponse
import com.anamolydeliveryboy.ui.completed_order.CompletedOrderFragment
import com.anamolydeliveryboy.ui.home.adapter.SideMenuAdapter
import com.anamolydeliveryboy.ui.home.fragment.HomeFragment
import com.anamolydeliveryboy.ui.profile.ProfileFragment
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.nav_header_main.*
import utils.ContextWrapper
import utils.LanguagePrefs
import utils.SessionManagement
import utils.TrackerService
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class MainActivity : AppCompatActivity() {

    companion object {
        val TAG = MainActivity::class.java.simpleName
    }

    var menu_logout: MenuItem? = null

    lateinit var mainViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        LanguagePrefs(this)
        mainViewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        setContentView(R.layout.activity_main)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        val END_SCALE = 0.8f

        val toggle = object : ActionBarDrawerToggle(
            this,
            drawer_layout,
            toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        ) {

            override fun onDrawerSlide(drawerView: View, slideOffset: Float) {
                super.onDrawerSlide(drawerView, slideOffset)

                // Scale the View based on current slide offset
                val diffScaledOffset = slideOffset * (1 - END_SCALE)
                val offsetScale = 1 - diffScaledOffset
                content.scaleX = offsetScale
                content.scaleY = offsetScale

                // Translate the View, accounting for the scaled width
                val xOffset = drawerView.width * slideOffset
                val xOffsetDiff = content.width * diffScaledOffset / 2
                val xTranslation = xOffset - xOffsetDiff
                content.translationX = xTranslation
            }
        }
        //After instantiating your ActionBarDrawerToggle
        toggle.isDrawerIndicatorEnabled = false
        val drawable = ContextCompat.getDrawable(
            this,
            R.drawable.ic_menu
        )
        drawable?.setColorFilter(
            ContextCompat.getColor(this, R.color.colorWhite),
            android.graphics.PorterDuff.Mode.MULTIPLY
        )
        toggle.setHomeAsUpIndicator(drawable)
        toggle.toolbarNavigationClickListener = View.OnClickListener {
            if (drawer_layout.isDrawerVisible(GravityCompat.START)) {
                drawer_layout.closeDrawer(GravityCompat.START)
            } else {
                drawer_layout.openDrawer(GravityCompat.START)
            }
        }
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()
        drawer_layout.setScrimColor(ContextCompat.getColor(this, android.R.color.transparent))

        Glide.with(this)
            .load(
                BaseURL.IMG_PROFILE_URL + SessionManagement.UserData.getSession(
                    this,
                    BaseURL.KEY_IMAGE
                )
            )
            .placeholder(R.mipmap.ic_launcher)
            .error(R.mipmap.ic_launcher)
            .into(iv_header_logo)

        tv_header_name.text = SessionManagement.UserData.getSession(this, BaseURL.KEY_NAME)
        tv_header_number.text = SessionManagement.UserData.getSession(this, BaseURL.KEY_MOBILE)

        val sideMenuModelList = ArrayList<SideMenuModel>()
        sideMenuModelList.add(SideMenuModel(getString(R.string.profile), false, false))
        sideMenuModelList.add(SideMenuModel(getString(R.string.recent_orders), false, false))
        sideMenuModelList.add(SideMenuModel(getString(R.string.completed_orders), false, false))
        sideMenuModelList.add(
            SideMenuModel(
                resources.getString(R.string.available),
                true,
                false,
                SessionManagement.UserData.getSession(this, "status") == "1"
            )
        )

        val sideMenuAdapter =
            SideMenuAdapter(this, sideMenuModelList, object : SideMenuAdapter.OnItemClickListener {
                override fun onClick(position: Int, sideMenuModel: SideMenuModel) {
                    setItemSelected(sideMenuModel)
                }
            })

        rv_side_menu.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = sideMenuAdapter
        }

        if (savedInstanceState == null) {
            loadHome()
        }

        mainViewModel.makeFirebaseSubscribe(this, true)

    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            val fr = supportFragmentManager.findFragmentById(R.id.contentPanel)
            val fm_name = fr?.javaClass!!.simpleName

            if (!fm_name.contentEquals("HomeFragment")) {
                loadHome()
            } else {
                super.onBackPressed()
            }
        }
    }

    val fm_home = HomeFragment()
    private fun loadHome() {
        menu_logout?.isVisible = false
        setHeaderTitle(resources.getString(R.string.app_name))

        val fragmentManager = supportFragmentManager
        fragmentManager.beginTransaction()
            .replace(R.id.contentPanel, fm_home, "Home_fragment")
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            .commit()

        drawer_layout.closeDrawer(GravityCompat.START)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        menu_logout = menu.findItem(R.id.action_logout)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_logout) {
            SessionManagement(this).logoutSessionLogin()
            finish()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setItemSelected(sideMenuModel: SideMenuModel) {
        menu_logout?.isVisible = false
        app_bar.elevation = 1F
        when (sideMenuModel.title) {
            resources.getString(R.string.profile) -> {
                setHeaderTitle(sideMenuModel.title)
                menu_logout?.isVisible = true
                app_bar.elevation = 0F

                val fragmentManager = supportFragmentManager
                fragmentManager.beginTransaction()
                    .replace(R.id.contentPanel, ProfileFragment(), "")
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .commit()
            }
            resources.getString(R.string.recent_orders) -> {
                loadHome()
            }
            resources.getString(R.string.completed_orders) -> {
                setHeaderTitle(sideMenuModel.title)

                val fragmentManager = supportFragmentManager
                fragmentManager.beginTransaction()
                    .replace(R.id.contentPanel, CompletedOrderFragment(), "")
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .commit()
            }
            resources.getString(R.string.available) -> {
                val params = HashMap<String, String>()
                params["delivery_boy_id"] =
                    SessionManagement.UserData.getSession(this, BaseURL.KEY_ID)
                params["status"] = if (sideMenuModel.isChecked) "1" else "0"

                mainViewModel.makeAddAvailable(params)
                    .observe(this, Observer { response: CommonResponse? ->
                        if (response != null) {
                            if (response.responce!!) {
                                SessionManagement.UserData.setSession(
                                    this,
                                    "is_available",
                                    response.data!!
                                )
                            }
                        }
                    })
            }
        }
        drawer_layout.closeDrawer(GravityCompat.START)
    }

    fun setHeaderTitle(title: String) {
        toolbar.title = title
    }

    private val REQUEST_CHECK_SETTINGS = 0x1

    fun displayLocationSettingsRequest(context: Context) {
        /*val googleApiClient = GoogleApiClient.Builder(context)
            .addApi(LocationServices.API)
            .build()
        googleApiClient.connect()
        val locationRequest = LocationRequest.create()
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.interval = 100
        locationRequest.fastestInterval = 100
        val builder =
            LocationSettingsRequest.Builder().addLocationRequest(locationRequest)
        builder.setAlwaysShow(true)
        val result =
            LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build())
        result.setResultCallback { result ->
            val status = result.status
            when (status.statusCode) {
                LocationSettingsStatusCodes.SUCCESS -> {
                    Log.i(
                        TAG, "All location settings are satisfied."
                    )
                    if (!CommonActivity.isMyServiceRunning(
                            this@MainActivity,
                            TrackerService::class.java
                        )
                    ) {
                        fm_home.startTrackerService()
                    }
                }
                LocationSettingsStatusCodes.RESOLUTION_REQUIRED -> {
                    Log.i(
                        TAG,
                        "Location settings are not satisfied. Show the user a dialog to upgrade location settings "
                    )
                    try {
                        // Show the dialog by calling startResolutionForResult(), and check the result
                        // in onActivityResult().
                        status.startResolutionForResult(
                            this@MainActivity,
                            REQUEST_CHECK_SETTINGS
                        )
                    } catch (e: IntentSender.SendIntentException) {
                        Log.i(TAG, "PendingIntent unable to execute request.")
                    }
                }
                LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> Log.i(
                    TAG,
                    "Location settings are inadequate, and cannot be fixed here. Dialog not created."
                )
            }
        }*/
        mainViewModel.locationSettingUpdateLiveData.observe(this, Observer { result ->
            val status = result.status
            when (status.statusCode) {
                LocationSettingsStatusCodes.SUCCESS -> {
                    Log.i(
                        TAG, "All location settings are satisfied."
                    )
                    if (!CommonActivity.isMyServiceRunning(
                            this@MainActivity,
                            TrackerService::class.java
                        )
                    ) {
                        fm_home.startTrackerService()
                    }
                }
                LocationSettingsStatusCodes.RESOLUTION_REQUIRED -> {
                    Log.i(
                        TAG,
                        "Location settings are not satisfied. Show the user a dialog to upgrade location settings "
                    )
                    try {
                        // Show the dialog by calling startResolutionForResult(), and check the result
                        // in onActivityResult().
                        status.startResolutionForResult(
                            this@MainActivity,
                            REQUEST_CHECK_SETTINGS
                        )
                    } catch (e: IntentSender.SendIntentException) {
                        Log.i(TAG, "PendingIntent unable to execute request.")
                    }
                }
                LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> Log.i(
                    TAG,
                    "Location settings are inadequate, and cannot be fixed here. Dialog not created."
                )
            }
            mainViewModel.locationSettingUpdateLiveData.removeObservers(this)
        })
        mainViewModel.checkLocationSettingsRequest(context)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CHECK_SETTINGS) {
            if (resultCode == Activity.RESULT_OK) {
                if (!CommonActivity.isMyServiceRunning(
                        this@MainActivity,
                        TrackerService::class.java
                    )
                ) {
                    fm_home.startTrackerService()
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                displayLocationSettingsRequest(this)
            }
        }
    }

    override fun attachBaseContext(newBase: Context?) {
        val newLocale = Locale(LanguagePrefs.getLang(newBase!!)!!)
        // .. create or get your new Locale object here.
        val context = ContextWrapper.wrap(newBase, newLocale)
        super.attachBaseContext(context)
    }

}
