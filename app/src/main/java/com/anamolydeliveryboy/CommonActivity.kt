package com.anamolydeliveryboy

import Config.GlobleVariable
import android.app.Activity
import android.app.ActivityManager
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.TypedValue
import android.view.Gravity
import android.view.MenuItem
import android.view.View
import android.view.Window
import android.view.animation.AnimationUtils
import android.view.inputmethod.InputMethodManager
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import org.json.JSONArray
import org.json.JSONException
import utils.ContextWrapper
import utils.LanguagePrefs
import utils.MyBounceInterpolator
import java.text.SimpleDateFormat
import java.util.*


/**
 * Created on 17-02-2020.
 */
open class CommonActivity : AppCompatActivity() {

    var menu_like: MenuItem? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        LanguagePrefs(this)
        if (supportActionBar != null) {
            supportActionBar?.setHomeButtonEnabled(true)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar?.setDisplayShowTitleEnabled(true)
            /*val upArrow = ContextCompat.getDrawable(this, R.drawable.abc_ic_ab_back_material)
            upArrow?.setColorFilter(
                ContextCompat.getColor(this, R.color.colorBlack),
                PorterDuff.Mode.SRC_ATOP
            )
            supportActionBar?.setHomeAsUpIndicator(upArrow)*/
        }
    }

    fun setHeaderTitle(title: String) {
        val mActionBar = supportActionBar
        if (mActionBar != null) {
            mActionBar.title = title
            /*mActionBar.setDisplayShowTitleEnabled(false)
            val mInflater = LayoutInflater.from(this)

            val mCustomView = mInflater.inflate(R.layout.custom_actionbar, null)
            val layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            layoutParams.gravity = Gravity.CENTER_VERTICAL
            mCustomView.layoutParams = layoutParams
            val mTitleTextView = mCustomView.tv_actionbar_title
            mTitleTextView!!.text = title
            mActionBar.customView = mCustomView
            mActionBar.setDisplayShowCustomEnabled(true)

            val upArrow = resources.getDrawable(R.drawable.abc_ic_ab_back_material)
            upArrow.setColorFilter(resources.getColor(R.color.colorBlack), PorterDuff.Mode.SRC_ATOP)*/
            //mActionBar.setHomeAsUpIndicator(R.drawable.ic_back)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item!!.itemId == android.R.id.home) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                finishAfterTransition()
            } else {
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    companion object {

        var toast: Toast? = null
        fun showToast(context: Context, message: CharSequence) {
            if (toast != null) {
                toast!!.cancel()
            }
            toast = Toast.makeText(context, message, Toast.LENGTH_LONG)
            toast!!.setGravity(Gravity.TOP or Gravity.FILL_HORIZONTAL, 0, 0)
            val layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            val linearLayoutMain = LinearLayout(context)
            linearLayoutMain.layoutParams = layoutParams
            linearLayoutMain.setPadding(30, 10, 30, 10)
            val linearLayout = LinearLayout(context)
            linearLayout.layoutParams = layoutParams
            linearLayout.orientation = LinearLayout.HORIZONTAL
            linearLayout.setBackgroundColor(ContextCompat.getColor(context, R.color.colorBlack))
            linearLayout.setPadding(50, 50, 50, 50)
            val textView = TextView(context)
            textView.setTextColor(ContextCompat.getColor(context, R.color.colorWhite))
            textView.text = message
            linearLayout.addView(textView)
            linearLayoutMain.addView(linearLayout)
            toast!!.view = linearLayoutMain
            toast!!.duration = Toast.LENGTH_LONG
            toast!!.show()
        }

        fun isEmailValid(email: String): Boolean {
            val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
            return email.matches(emailPattern.toRegex())
        }

        fun isPhoneValid(phone: String): Boolean {
            return phone.length >= 8
        }

        fun isPasswordValid(password: String): Boolean {
            return password.length >= 6
        }

        fun isValidPassword(password: String?): Boolean {
            password?.let {
                val passwordPattern =
                    "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{6,}$"
                val passwordMatcher = Regex(passwordPattern)
                return passwordMatcher.find(password) != null
            } ?: return false
        }

        fun dpToPx(context: Context, dp: Float): Int {
            return Math.round(
                TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP,
                    dp,
                    context.resources.displayMetrics
                )
            )
        }

        fun spToPx(context: Context, sp: Float): Int {
            return Math.round(
                TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_SP,
                    sp,
                    context.resources.displayMetrics
                )
            )
        }

        fun changeStatusBarColor(activity: Activity, isLight: Boolean) {
            val w: Window = activity.window
            if (isLight) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    var flags: Int = w.decorView.systemUiVisibility
                    flags = flags or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
                    w.decorView.systemUiVisibility = flags
                }
            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    var flags: Int = w.decorView.systemUiVisibility
                    flags = flags or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
                    w.clearFlags(flags)
                }
            }
        }

        fun runLayoutAnimation(recyclerView: RecyclerView) {
            val context = recyclerView.context
            val controller =
                AnimationUtils.loadLayoutAnimation(context, R.anim.layout_animation_fall_down)

            recyclerView.layoutAnimation = controller
            recyclerView.adapter!!.notifyDataSetChanged()
            recyclerView.scheduleLayoutAnimation()
        }

        fun runBounceAnimation(context: Context, view: View) {
            val animation =
                AnimationUtils.loadAnimation(context, R.anim.bounce)
            animation.interpolator = MyBounceInterpolator(0.2, 20.0)
            view.startAnimation(animation)
        }

        fun getCurrentDateTime(isDateOnly: Boolean): String {
            val inputPattern = "yyyy-MM-dd HH:mm:ss"
            val inputPattern2 = "yyyy-MM-dd"
            val inputFormat = SimpleDateFormat(inputPattern, GlobleVariable.LOCALE)
            val inputFormat2 = SimpleDateFormat(inputPattern2, GlobleVariable.LOCALE)
            return if (isDateOnly) {
                inputFormat2.format(Date().time)
            } else {
                inputFormat.format(Date().time)
            }
        }

        fun getConvertDate(dateTime: String, step: Int): String {
            val inputPattern = "yyyy-MM-dd"
            val outputPattern1 = "dd-MMM-yyyy"
            val inputFormat = SimpleDateFormat(inputPattern, GlobleVariable.LOCALE)
            val outputFormat1 = SimpleDateFormat(outputPattern1, GlobleVariable.LOCALE)
            return outputFormat1.format(inputFormat.parse(dateTime)!!)
        }

        fun getConvertDateTime(dateTime: Date, step: Int): String {
            val inputPattern = "yyyy-MM-dd HH:mm:ss"
            val outputPattern1 = "dd-MMM-yyyy hh:mm a"
            val inputFormat = SimpleDateFormat(inputPattern, GlobleVariable.LOCALE)
            val outputFormat1 = SimpleDateFormat(outputPattern1, GlobleVariable.LOCALE)
            return outputFormat1.format(dateTime)
        }

        fun getConvertTime(dateTime: String, step: Int): String {
            val inputPattern = "HH:mm:ss"
            val outputPattern1 = "hh:mm a"
            val outputPattern2 = "HH:mm"
            val inputFormat = SimpleDateFormat(inputPattern, GlobleVariable.LOCALE)
            val outputFormat1 = SimpleDateFormat(outputPattern1, GlobleVariable.LOCALE)
            val outputFormat2 = SimpleDateFormat(outputPattern2, GlobleVariable.LOCALE)
            return when (step) {
                1 -> outputFormat1.format(inputFormat.parse(dateTime)!!)
                2 -> outputFormat2.format(inputFormat.parse(dateTime)!!)
                else -> outputFormat1.format(inputFormat.parse(dateTime)!!)
            }
        }

        fun hideShowKeyboard(activity: Activity, hide: Boolean) {
            val imm: InputMethodManager =
                activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            //Find the currently focused view, so we can grab the correct window token from it.
            //Find the currently focused view, so we can grab the correct window token from it.
            var view = activity.currentFocus
            if (view == null) {
                view = View(activity)
            }
            if (hide) {
                imm.hideSoftInputFromWindow(view.windowToken, 0)
            } else {
                imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
            }
        }

        fun getStringByLanguage(
            context: Context,
            textEN: String?,
            textAR: String?,
            textNL: String?
        ): String? {
            return if (LanguagePrefs.getLang(context).equals("sv")
                && !textNL.isNullOrEmpty()
            ) {
                textNL
            } else if (LanguagePrefs.getLang(context).equals("ar")
                && !textAR.isNullOrEmpty()
            ) {
                textAR
            } else {
                return if (textEN.isNullOrEmpty()) {
                    textNL
                } else {
                    textEN
                }
            }
        }

        fun isMyServiceRunning(context: Context, serviceClass: Class<*>): Boolean {
            val manager: ActivityManager =
                context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
            for (service in manager.getRunningServices(Int.MAX_VALUE)) {
                if (serviceClass.name == service.service.className) {
                    return true
                }
            }
            return false
        }

        fun convertToHashMap(jsonString: String): HashMap<String, Any> {
            var myHashMap = HashMap<String, Any>()
            try {
                val jArray = JSONArray(jsonString)
                for (i in 0 until jArray.length()) {
                    val jObject = jArray.getJSONObject(i)
                    val keyString = jObject.names()!![0] as String
                    myHashMap[keyString] = jObject.get(keyString)
                }
            } catch (e: JSONException) {
                e.printStackTrace()
            }
            return myHashMap
        }

    }

    override fun attachBaseContext(newBase: Context?) {
        val newLocale = Locale(LanguagePrefs.getLang(newBase!!)!!)
        // .. create or get your new Locale object here.
        val context = ContextWrapper.wrap(newBase, newLocale)
        super.attachBaseContext(context)
    }

}
