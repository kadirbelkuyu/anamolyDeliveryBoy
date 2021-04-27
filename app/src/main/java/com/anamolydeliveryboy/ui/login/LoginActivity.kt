package com.anamolydeliveryboy.ui.login

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.anamolydeliveryboy.CommonActivity
import com.anamolydeliveryboy.R
import com.anamolydeliveryboy.response.CommonResponse
import com.anamolydeliveryboy.ui.home.MainActivity
import com.thekhaeng.pushdownanim.PushDownAnim
import dialogs.LoaderDialog
import kotlinx.android.synthetic.main.activity_login.*
import utils.ConnectivityReceiver
import utils.ContextWrapper
import utils.LanguagePrefs
import java.util.*
import kotlin.collections.HashMap

class LoginActivity : AppCompatActivity(), View.OnClickListener {

    lateinit var languagePrefs: LanguagePrefs

    lateinit var loginViewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        languagePrefs = LanguagePrefs(this)
        loginViewModel = ViewModelProvider(this).get(LoginViewModel::class.java)
        setContentView(R.layout.activity_login)

        PushDownAnim.setPushDownAnimTo(btn_login)

        loginViewModel.getAttemptStatus.observe(this, Observer { status: Boolean ->
            if (status) {
                if (ConnectivityReceiver.isConnected) {
                    val params = HashMap<String, String>()
                    params["boy_phone"] = et_login_phone_number.text.toString()
                    params["boy_password"] = et_login_password.text.toString()

                    val loaderDialog = LoaderDialog(this)
                    loaderDialog.show()

                    loginViewModel.makeLogin(params)
                        .observe(this, Observer { response: CommonResponse? ->
                            loaderDialog.dismiss()
                            if (response != null) {
                                if (response.responce!!) {
                                    loginViewModel.storeLoginData(this, response.data!!)
                                    Intent(this, MainActivity::class.java).apply {
                                        startActivity(this)
                                        finish()
                                    }
                                } else {
                                    CommonActivity.showToast(this, response.message!!)
                                }
                            }
                        })
                }
            }
        })

        if (LanguagePrefs.getLang(this) == "en") {
            tv_login_english.isSelected = true
        } else if (LanguagePrefs.getLang(this) == "sv") {
            tv_login_dutch.isSelected = true
        }

        btn_login.setOnClickListener(this)
        tv_login_dutch.setOnClickListener(this)
        tv_login_english.setOnClickListener(this)

    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_login -> {
                loginViewModel.attemptLogin(this, et_login_phone_number, et_login_password)
            }
            R.id.tv_login_dutch -> {
                tv_login_dutch.isSelected = true
                tv_login_english.isSelected = false

                languagePrefs.saveLanguage("sv")
                languagePrefs.initLanguage("sv")
                recreate()
            }
            R.id.tv_login_english -> {
                tv_login_dutch.isSelected = false
                tv_login_english.isSelected = true

                languagePrefs.saveLanguage("en")
                languagePrefs.initLanguage("en")
                recreate()
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
