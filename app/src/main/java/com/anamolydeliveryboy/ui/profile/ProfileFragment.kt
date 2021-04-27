package com.anamolydeliveryboy.ui.profile

import Config.BaseURL
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.anamolydeliveryboy.R
import com.thekhaeng.pushdownanim.PushDownAnim
import kotlinx.android.synthetic.main.fragment_profile.view.*
import utils.SessionManagement

/**
 * Created on 06-04-2020.
 */
class ProfileFragment : Fragment() {

    lateinit var contexts: Context

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_profile, container, false)

        Glide.with(contexts)
            .load(
                BaseURL.IMG_PROFILE_URL + SessionManagement.UserData.getSession(
                    contexts,
                    BaseURL.KEY_IMAGE
                )
            )
            .placeholder(R.mipmap.ic_launcher)
            .error(R.mipmap.ic_launcher)
            .into(rootView.iv_profile_img)

        Glide.with(contexts)
            .load(
                BaseURL.IMG_PROFILE_URL + SessionManagement.UserData.getSession(
                    contexts,
                    "licence_photo"
                )
            )
            .into(rootView.iv_profile_licence_photo)

        Glide.with(contexts)
            .load(
                BaseURL.IMG_PROFILE_URL + SessionManagement.UserData.getSession(
                    contexts,
                    "id_photo"
                )
            )
            .into(rootView.iv_profile_id_photo)

        rootView.et_profile_name.setText(
            SessionManagement.UserData.getSession(
                contexts,
                BaseURL.KEY_NAME
            )
        )
        rootView.et_profile_email.setText(
            SessionManagement.UserData.getSession(
                contexts,
                BaseURL.KEY_EMAIL
            )
        )
        rootView.et_profile_phone.setText(
            SessionManagement.UserData.getSession(
                contexts,
                BaseURL.KEY_MOBILE
            )
        )
        rootView.et_profile_id_proof.setText(
            SessionManagement.UserData.getSession(
                contexts,
                "id_proof"
            )
        )
        rootView.et_profile_licence.setText(
            SessionManagement.UserData.getSession(
                contexts,
                "licence_no"
            )
        )

        PushDownAnim.setPushDownAnimTo(rootView.btn_profile_change_password)

        rootView.btn_profile_change_password.setOnClickListener {
            Intent(contexts, ChangePasswordActivity::class.java).apply {
                startActivity(this)
            }
        }

        return rootView
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        contexts = context
    }

}