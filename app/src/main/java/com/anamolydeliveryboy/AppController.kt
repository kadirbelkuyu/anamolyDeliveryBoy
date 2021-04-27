package com.anamolydeliveryboy

import Config.BaseURL
import android.app.Application
import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import android.util.Log
import com.droidnet.DroidNet
import com.onesignal.OSNotificationOpenedResult
import com.onesignal.OSNotificationReceivedEvent
import com.onesignal.OneSignal
import utils.LanguagePrefs

class AppController : Application() {

    override fun onCreate() {
        super.onCreate()
        instance = this
        setTheme(R.style.AppTheme)

        DroidNet.init(this)

        // Enable verbose OneSignal logging to debug issues if needed.
        OneSignal.setLogLevel(OneSignal.LOG_LEVEL.VERBOSE, OneSignal.LOG_LEVEL.NONE)

        // OneSignal Initialization
        OneSignal.initWithContext(this)
        OneSignal.setAppId(BuildConfig.ONESIGNAL_APP_ID)

        OneSignal.setNotificationOpenedHandler(ExampleNotificationOpenedHandler())
        OneSignal.setNotificationWillShowInForegroundHandler(NotificationWillShowInForegroundHandler())

        LanguagePrefs(this)
        BaseURL.HEADER_LANG = if (LanguagePrefs.getLang(this).equals("sv")) {
            "dutch"
        } else if (LanguagePrefs.getLang(this).equals("ar")) {
            "arabic"
        } else {
            "english"
        }

    }

    companion object {
        @get:Synchronized
        var instance: AppController? = null
            private set
    }

    @Synchronized
    fun getInstance(): AppController? {
        return instance
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        if (LanguagePrefs.getLang(this) != null) {
            LanguagePrefs(this)
        }
        super.onConfigurationChanged(newConfig)
    }

    override fun onLowMemory() {
        super.onLowMemory()
        DroidNet.getInstance().removeAllInternetConnectivityChangeListeners()
    }

    inner class NotificationWillShowInForegroundHandler :
        OneSignal.OSNotificationWillShowInForegroundHandler {
        override fun notificationWillShowInForeground(notificationReceivedEvent: OSNotificationReceivedEvent?) {
            notificationReceivedEvent?.complete(notificationReceivedEvent.notification)
        }
    }

    inner class ExampleNotificationOpenedHandler : OneSignal.OSNotificationOpenedHandler {
        override fun notificationOpened(result: OSNotificationOpenedResult) {
            val actionType = result.action.type
            val notificationID = result.notification.notificationId

            val data = result.notification.additionalData
            val launchUrl =
                result.notification.launchURL // update docs launchUrl
            var customKey: String? = null
            var openURL: String? = null
            val activityToLaunch: Any = SplashActivity::class.java
            if (data != null) {
                Log.i("OneSignalExample", "data::" + data.toString())
                customKey = data.optString("customkey", null)
                openURL = data.optString("openURL", null)
                if (customKey != null) Log.i(
                    "OneSignalExample",
                    "customkey set with value: $customKey"
                )
                if (openURL != null) Log.i(
                    "OneSignalExample",
                    "openURL to webview with URL value: $openURL"
                )
            }

            /*val notificationData = NotificationData(applicationContext)
            notificationData.deleteNotification(notificationID)*/

            if (launchUrl != null) {
                val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(launchUrl))
                browserIntent.flags =
                    Intent.FLAG_ACTIVITY_REORDER_TO_FRONT or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(browserIntent)
            } else if (customKey != null) {
                val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(customKey))
                browserIntent.flags =
                    Intent.FLAG_ACTIVITY_REORDER_TO_FRONT or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(browserIntent)
            } else {
                val intent =
                    Intent(instance, activityToLaunch as Class<*>)
                intent.flags =
                    Intent.FLAG_ACTIVITY_REORDER_TO_FRONT or Intent.FLAG_ACTIVITY_NEW_TASK
                if (data != null) {
                    intent.putExtra("data", data.toString())
                }
                Log.i("OneSignalExample", "openURL =$openURL")
                startActivity(intent)
            }
        }
    }

}