package com.anamolydeliveryboy.ui.profile

import Config.BaseURL
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.anamolydeliveryboy.CommonActivity
import com.anamolydeliveryboy.R
import com.anamolydeliveryboy.response.CommonResponse
import com.thekhaeng.pushdownanim.PushDownAnim
import dialogs.LoaderDialog
import kotlinx.android.synthetic.main.activity_change_password.*
import utils.SessionManagement

class ChangePasswordActivity : CommonActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val changePasswordViewModel =
            ViewModelProvider(this).get(ChangePasswordViewModel::class.java)
        setContentView(R.layout.activity_change_password)
        setHeaderTitle(resources.getString(R.string.change_password))

        PushDownAnim.setPushDownAnimTo(btn_change_password_submit)

        changePasswordViewModel.getAttemptStatus.observe(this, Observer { status: Boolean ->
            if (status) {
                val params = HashMap<String, String>()
                params["delivery_boy_id"] =
                    SessionManagement.UserData.getSession(this, BaseURL.KEY_ID)
                params["c_password"] = et_change_password_old.text.toString()
                params["n_password"] = et_change_password_new.text.toString()
                params["r_password"] = et_change_password_new_con.text.toString()

                val loaderDialog = LoaderDialog(this)
                loaderDialog.show()

                changePasswordViewModel.makeChangePassword(params)
                    .observe(this, Observer { response: CommonResponse? ->
                        loaderDialog.dismiss()
                        if (response != null) {
                            if (response.responce!!) {
                                CommonActivity.showToast(
                                    this,
                                    resources.getString(R.string.password_changed_successfully)
                                )
                                finish()
                            } else {
                                CommonActivity.showToast(this, response.message!!)
                            }
                        }
                    })

            }
        })

        btn_change_password_submit.setOnClickListener {
            changePasswordViewModel.attemptChangePassword(
                this,
                et_change_password_old,
                et_change_password_new,
                et_change_password_new_con
            )
        }

    }

}
