package com.anamolydeliveryboy.ui.login

import android.content.Context
import android.view.View
import android.widget.EditText
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.anamolydeliveryboy.CommonActivity
import com.anamolydeliveryboy.R
import com.anamolydeliveryboy.repository.ProjectRepository
import com.anamolydeliveryboy.response.CommonResponse
import org.json.JSONObject
import utils.SessionManagement

/**
 * Created on 06-04-2020.
 */
class LoginViewModel : ViewModel() {
    val projectRepository = ProjectRepository()

    val getAttemptStatus = MutableLiveData<Boolean>()

    fun attemptLogin(context: Context, et_number: EditText, et_password: EditText) {
        et_number.error = null
        et_password.error = null

        val number = et_number.text.toString()
        val password = et_password.text.toString()

        var focusView: View? = null
        var cancel = false

        if (password.isEmpty()) {
            et_password.error = context.resources.getString(R.string.error_field_required)
            focusView = et_password
            cancel = true
        } /*else if (!CommonActivity.isPasswordValid(password)) {
            et_password.error = context.resources.getString(R.string.error_password_short)
            focusView = et_password
            cancel = true
        }*/

        if (number.isEmpty()) {
            et_number.error = context.resources.getString(R.string.error_field_required)
            focusView = et_number
            cancel = true
        } else if (!CommonActivity.isPhoneValid(number)) {
            et_number.error = context.resources.getString(R.string.error_phone_short)
            focusView = et_number
            cancel = true
        }

        if (cancel) {
            focusView?.requestFocus()
            getAttemptStatus.value = false
        } else {
            getAttemptStatus.value = true
        }
    }

    fun makeLogin(params: HashMap<String, String>): LiveData<CommonResponse?> {
        return projectRepository.checkLogin(params)
    }

    fun storeLoginData(context: Context, response: String) {
        val jsonObject = JSONObject(response)

        val delivery_boy_id = jsonObject.getString("delivery_boy_id")
        val boy_phone = jsonObject.getString("boy_phone")
        val boy_name = jsonObject.getString("boy_name")
        val boy_email = jsonObject.getString("boy_email")
        val boy_photo = jsonObject.getString("boy_photo")
        val vehicle_id = jsonObject.getString("vehicle_id")
        val licence_no = jsonObject.getString("boy_licence")
        val id_proof = jsonObject.getString("boy_id_proof")
        val id_photo = jsonObject.getString("id_photo")
        val licence_photo = jsonObject.getString("licence_photo")
        val status = jsonObject.getString("status")

        val sessionManagement = SessionManagement(context)

        sessionManagement.createLoginSession(
            delivery_boy_id,
            "1",
            boy_email,
            boy_name,
            boy_phone,
            boy_photo
        )

        SessionManagement.UserData.setSession(context, "vehicle_id", vehicle_id)
        SessionManagement.UserData.setSession(context, "licence_no", licence_no)
        SessionManagement.UserData.setSession(context, "id_proof", id_proof)
        SessionManagement.UserData.setSession(context, "licence_photo", licence_photo)
        SessionManagement.UserData.setSession(context, "id_photo", id_photo)
        SessionManagement.UserData.setSession(context, "status", status)

    }

}