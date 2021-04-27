package Config

import com.anamolydeliveryboy.BuildConfig

object BaseURL {

    const val IMG_PROFILE_URL = BuildConfig.IMG_BASE_URL + "uploads/profile/"

    var HEADER_LANG = "english"//dutch,english,arabic

    const val PREFS_NAME = "SeyyarDeliveryBoyLoginPrefs"
    const val PERMANENT_PREFS_NAME = "SeyyarDeliveryBoyPermanentPrefs"

    const val IS_LOGIN = "isLogin"
    const val KEY_ID = "delivery_boy_id"
    const val KEY_TYPE_ID = "user_type_id"
    const val KEY_NAME = "boy_name"
    const val KEY_EMAIL = "boy_email"
    const val KEY_MOBILE = "boy_phone"
    const val KEY_IMAGE = "boy_photo"

}