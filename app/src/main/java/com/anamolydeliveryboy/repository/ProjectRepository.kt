package com.anamolydeliveryboy.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.anamolydeliveryboy.response.CommonResponse
import com.anamolydeliveryboy.retrofit.ApiRequest
import com.anamolydeliveryboy.retrofit.RetrofitRequest.retrofitInstance
import com.google.gson.Gson
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Created on 10-02-2020.
 */
class ProjectRepository {
    private val apiRequest: ApiRequest = retrofitInstance!!.create(ApiRequest::class.java)
    val gson = Gson()

    companion object {
        private val TAG = ProjectRepository::class.java.simpleName
    }

    fun checkLogin(params: Map<String, String>): LiveData<CommonResponse?> {
        Log.d(TAG, "Post Data::$params")
        val data = MutableLiveData<CommonResponse?>()
        apiRequest.login(params).enqueue(object : Callback<String> {
            override fun onResponse(
                call: Call<String>,
                response: Response<String>
            ) {
                Log.d(TAG, "onResponse response:: $response")
                if (response.body() != null) {
                    Log.d(TAG, "onResponse data:: ${response.body()!!}")

                    val jsonObject = JSONObject(response.body().toString())

                    val commonResponse = CommonResponse()
                    commonResponse.responce = jsonObject.getBoolean("responce")
                    if (jsonObject.has("code"))
                        commonResponse.code = jsonObject.getString("code")
                    if (jsonObject.has("message"))
                        commonResponse.message = jsonObject.getString("message")
                    if (jsonObject.has("data"))
                        commonResponse.data = jsonObject.getString("data")

                    data.value = commonResponse
                }
            }

            override fun onFailure(
                call: Call<String>,
                t: Throwable
            ) {
                Log.d(TAG, "onFailure error:: ${t.printStackTrace()}")
                data.value = null
            }
        })
        return data
    }

    fun getHomeData(params: Map<String, String>): LiveData<CommonResponse?> {
        Log.d(TAG, "Post Data::$params")
        val data = MutableLiveData<CommonResponse?>()
        apiRequest.getHomeData(params).enqueue(object : Callback<String> {
            override fun onResponse(
                call: Call<String>,
                response: Response<String>
            ) {
                Log.d(TAG, "onResponse response:: $response")
                if (response.body() != null) {
                    Log.d(TAG, "onResponse data:: ${response.body()!!}")

                    val jsonObject = JSONObject(response.body().toString())

                    val commonResponse = CommonResponse()
                    commonResponse.responce = jsonObject.getBoolean("responce")
                    if (jsonObject.has("code"))
                        commonResponse.code = jsonObject.getString("code")
                    if (jsonObject.has("message"))
                        commonResponse.message = jsonObject.getString("message")
                    if (jsonObject.has("data"))
                        commonResponse.data = jsonObject.getString("data")

                    data.value = commonResponse
                }
            }

            override fun onFailure(
                call: Call<String>,
                t: Throwable
            ) {
                Log.d(TAG, "onFailure error:: ${t.printStackTrace()}")
                data.value = null
            }
        })
        return data
    }

    fun getDeliveredOrderList(params: Map<String, String>): LiveData<CommonResponse?> {
        Log.d(TAG, "Post Data::$params")
        val data = MutableLiveData<CommonResponse?>()
        apiRequest.getOrderDeliveredList(params).enqueue(object : Callback<String> {
            override fun onResponse(
                call: Call<String>,
                response: Response<String>
            ) {
                Log.d(TAG, "onResponse response:: $response")
                if (response.body() != null) {
                    Log.d(TAG, "onResponse data:: ${response.body()!!}")

                    val jsonObject = JSONObject(response.body().toString())

                    val commonResponse = CommonResponse()
                    commonResponse.responce = jsonObject.getBoolean("responce")
                    if (jsonObject.has("code"))
                        commonResponse.code = jsonObject.getString("code")
                    if (jsonObject.has("message"))
                        commonResponse.message = jsonObject.getString("message")
                    if (jsonObject.has("data"))
                        commonResponse.data = jsonObject.getString("data")

                    data.value = commonResponse
                }
            }

            override fun onFailure(
                call: Call<String>,
                t: Throwable
            ) {
                Log.d(TAG, "onFailure error:: ${t.printStackTrace()}")
                data.value = null
            }
        })
        return data
    }

    fun getOrderDetail(params: Map<String, String>): LiveData<CommonResponse?> {
        Log.d(TAG, "Post Data::$params")
        val data = MutableLiveData<CommonResponse?>()
        apiRequest.getOrderDetail(params).enqueue(object : Callback<String> {
            override fun onResponse(
                call: Call<String>,
                response: Response<String>
            ) {
                Log.d(TAG, "onResponse response:: $response")
                if (response.body() != null) {
                    Log.d(TAG, "onResponse data:: ${response.body()!!}")

                    val jsonObject = JSONObject(response.body().toString())

                    val commonResponse = CommonResponse()
                    commonResponse.responce = jsonObject.getBoolean("responce")
                    if (jsonObject.has("code"))
                        commonResponse.code = jsonObject.getString("code")
                    if (jsonObject.has("message"))
                        commonResponse.message = jsonObject.getString("message")
                    if (jsonObject.has("data"))
                        commonResponse.data = jsonObject.getString("data")

                    data.value = commonResponse
                }
            }

            override fun onFailure(
                call: Call<String>,
                t: Throwable
            ) {
                Log.d(TAG, "onFailure error:: ${t.printStackTrace()}")
                data.value = null
            }
        })
        return data
    }

    fun setAvailable(params: Map<String, String>): LiveData<CommonResponse?> {
        Log.d(TAG, "Post Data::$params")
        val data = MutableLiveData<CommonResponse?>()
        apiRequest.setAvailable(params).enqueue(object : Callback<String> {
            override fun onResponse(
                call: Call<String>,
                response: Response<String>
            ) {
                Log.d(TAG, "onResponse response:: $response")
                if (response.body() != null) {
                    Log.d(TAG, "onResponse data:: ${response.body()!!}")

                    val jsonObject = JSONObject(response.body().toString())

                    val commonResponse = CommonResponse()
                    commonResponse.responce = jsonObject.getBoolean("responce")
                    if (jsonObject.has("code"))
                        commonResponse.code = jsonObject.getString("code")
                    if (jsonObject.has("message"))
                        commonResponse.message = jsonObject.getString("message")
                    if (jsonObject.has("data"))
                        commonResponse.data = jsonObject.getString("data")

                    data.value = commonResponse
                }
            }

            override fun onFailure(
                call: Call<String>,
                t: Throwable
            ) {
                Log.d(TAG, "onFailure error:: ${t.printStackTrace()}")
                data.value = null
            }
        })
        return data
    }

    fun registerFCM(params: Map<String, String>): LiveData<CommonResponse?> {
        Log.d(TAG, "Post Data::$params")
        val data = MutableLiveData<CommonResponse?>()
        apiRequest.registerFCM(params).enqueue(object : Callback<String> {
            override fun onResponse(
                call: Call<String>,
                response: Response<String>
            ) {
                Log.d(TAG, "onResponse response:: $response")
                if (response.body() != null) {
                    Log.d(TAG, "onResponse data:: ${response.body()!!}")

                    val jsonObject = JSONObject(response.body().toString())

                    val commonResponse = CommonResponse()
                    commonResponse.responce = jsonObject.getBoolean("responce")
                    if (jsonObject.has("code"))
                        commonResponse.code = jsonObject.getString("code")
                    if (jsonObject.has("message"))
                        commonResponse.message = jsonObject.getString("message")
                    if (jsonObject.has("data"))
                        commonResponse.data = jsonObject.getString("data")

                    data.value = commonResponse
                }
            }

            override fun onFailure(
                call: Call<String>,
                t: Throwable
            ) {
                Log.d(TAG, "onFailure error:: ${t.printStackTrace()}")
                data.value = null
            }
        })
        return data
    }

    fun changePassword(params: Map<String, String>): LiveData<CommonResponse?> {
        Log.d(TAG, "Post Data::$params")
        val data = MutableLiveData<CommonResponse?>()
        apiRequest.changePassword(params).enqueue(object : Callback<String> {
            override fun onResponse(
                call: Call<String>,
                response: Response<String>
            ) {
                Log.d(TAG, "onResponse response:: $response")
                if (response.body() != null) {
                    Log.d(TAG, "onResponse data:: ${response.body()!!}")

                    val jsonObject = JSONObject(response.body().toString())

                    val commonResponse = CommonResponse()
                    commonResponse.responce = jsonObject.getBoolean("responce")
                    if (jsonObject.has("code"))
                        commonResponse.code = jsonObject.getString("code")
                    if (jsonObject.has("message"))
                        commonResponse.message = jsonObject.getString("message")
                    if (jsonObject.has("data"))
                        commonResponse.data = jsonObject.getString("data")

                    data.value = commonResponse
                }
            }

            override fun onFailure(
                call: Call<String>,
                t: Throwable
            ) {
                Log.d(TAG, "onFailure error:: ${t.printStackTrace()}")
                data.value = null
            }
        })
        return data
    }

    fun pickupOrder(params: Map<String, String>): LiveData<CommonResponse?> {
        Log.d(TAG, "Post Data::$params")
        val data = MutableLiveData<CommonResponse?>()
        apiRequest.orderPickup(params).enqueue(object : Callback<String> {
            override fun onResponse(
                call: Call<String>,
                response: Response<String>
            ) {
                Log.d(TAG, "onResponse response:: $response")
                if (response.body() != null) {
                    Log.d(TAG, "onResponse data:: ${response.body()!!}")

                    val jsonObject = JSONObject(response.body().toString())

                    val commonResponse = CommonResponse()
                    commonResponse.responce = jsonObject.getBoolean("responce")
                    if (jsonObject.has("code"))
                        commonResponse.code = jsonObject.getString("code")
                    if (jsonObject.has("message"))
                        commonResponse.message = jsonObject.getString("message")
                    if (jsonObject.has("data"))
                        commonResponse.data = jsonObject.getString("data")

                    data.value = commonResponse
                }
            }

            override fun onFailure(
                call: Call<String>,
                t: Throwable
            ) {
                Log.d(TAG, "onFailure error:: ${t.printStackTrace()}")
                data.value = null
            }
        })
        return data
    }

    fun completeOrder(params: Map<String, String>): LiveData<CommonResponse?> {
        Log.d(TAG, "Post Data::$params")
        val data = MutableLiveData<CommonResponse?>()
        apiRequest.orderComplete(params).enqueue(object : Callback<String> {
            override fun onResponse(
                call: Call<String>,
                response: Response<String>
            ) {
                Log.d(TAG, "onResponse response:: $response")
                if (response.body() != null) {
                    Log.d(TAG, "onResponse data:: ${response.body()!!}")

                    val jsonObject = JSONObject(response.body().toString())

                    val commonResponse = CommonResponse()
                    commonResponse.responce = jsonObject.getBoolean("responce")
                    if (jsonObject.has("code"))
                        commonResponse.code = jsonObject.getString("code")
                    if (jsonObject.has("message"))
                        commonResponse.message = jsonObject.getString("message")
                    if (jsonObject.has("data"))
                        commonResponse.data = jsonObject.getString("data")

                    data.value = commonResponse
                }
            }

            override fun onFailure(
                call: Call<String>,
                t: Throwable
            ) {
                Log.d(TAG, "onFailure error:: ${t.printStackTrace()}")
                data.value = null
            }
        })
        return data
    }

}