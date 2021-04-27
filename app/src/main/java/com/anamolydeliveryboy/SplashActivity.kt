package com.anamolydeliveryboy

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.onesignal.OneSignal
import com.anamolydeliveryboy.ui.home.MainActivity
import com.anamolydeliveryboy.ui.login.LoginActivity
import utils.ContextWrapper
import utils.LanguagePrefs
import utils.SessionManagement
import java.util.*

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        LanguagePrefs(this)
        //setContentView(R.layout.activity_splash)

        OneSignal.clearOneSignalNotifications()

        val sessionManagement = SessionManagement(this)

        if (sessionManagement.isLoggedIn()) {
            Intent(this, MainActivity::class.java).apply {
                startActivity(this)
                finish()
            }
        } else {
            Intent(this, LoginActivity::class.java).apply {
                startActivity(this)
                finish()
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
