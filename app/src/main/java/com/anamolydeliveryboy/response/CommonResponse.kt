package com.anamolydeliveryboy.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created on 10-02-2020.
 */
class CommonResponse {
    @Expose
    @SerializedName("responce")
    var responce: Boolean? = null
    @Expose
    @SerializedName("message")
    var message: String? = null
    @Expose
    @SerializedName("code")
    var code: String? = null
    var data: String? = null
}