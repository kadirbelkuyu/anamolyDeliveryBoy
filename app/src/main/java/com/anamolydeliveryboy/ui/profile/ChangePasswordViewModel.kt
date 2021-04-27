package com.anamolydeliveryboy.ui.profile

import android.content.Context
import android.view.View
import android.widget.EditText
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.anamolydeliveryboy.R
import com.anamolydeliveryboy.repository.ProjectRepository
import com.anamolydeliveryboy.response.CommonResponse

/**
 * Created on 06-04-2020.
 */
class ChangePasswordViewModel : ViewModel() {
    val projectRepository = ProjectRepository()

    val getAttemptStatus = MutableLiveData<Boolean>()

    fun attemptChangePassword(
        context: Context,
        et_password_old: EditText,
        et_password_new: EditText,
        et_password_con: EditText
    ) {
        et_password_old.error = null
        et_password_new.error = null
        et_password_con.error = null

        val password_old = et_password_old.text.toString()
        val password_new = et_password_new.text.toString()
        val password_con = et_password_con.text.toString()

        var focusView: View? = null
        var cancel = false

        if (password_new.isNotEmpty() && password_new != password_con) {
            et_password_con.error =
                context.resources.getString(R.string.confirm_password_does_t_match_with_new_password)
            focusView = et_password_con
            cancel = true
        }

        if (password_new.isEmpty()) {
            et_password_new.error = context.resources.getString(R.string.error_field_required)
            focusView = et_password_new
            cancel = true
        }/* else if (!CommonActivity.isPasswordValid(password_new)) {
            et_password_new.error = context.resources.getString(R.string.error_password_short)
            focusView = et_password_new
            cancel = true
        }*/

        if (password_old.isEmpty()) {
            et_password_old.error = context.resources.getString(R.string.error_field_required)
            focusView = et_password_old
            cancel = true
        }/* else if (!CommonActivity.isPasswordValid(password_old)) {
            et_password_old.error = context.resources.getString(R.string.error_password_short)
            focusView = et_password_old
            cancel = true
        }*/

        if (cancel) {
            focusView?.requestFocus()
            getAttemptStatus.value = false
        } else {
            getAttemptStatus.value = true
        }
    }

    fun makeChangePassword(params: HashMap<String, String>): LiveData<CommonResponse?> {
        return projectRepository.changePassword(params)
    }

}