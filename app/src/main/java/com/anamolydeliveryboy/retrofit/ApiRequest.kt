package com.anamolydeliveryboy.retrofit

import retrofit2.Call
import retrofit2.http.*

interface ApiRequest {

    @FormUrlEncoded
    @POST("index.php/rest/deliveryboy/login")
    fun login(@FieldMap params: Map<String, String>): Call<String>

    @FormUrlEncoded
    @POST("index.php/rest/deliveryboy/home")
    fun getHomeData(@FieldMap params: Map<String, String>): Call<String>

    @FormUrlEncoded
    @POST("index.php/rest/deliveryboy/completedorders")
    fun getOrderDeliveredList(@FieldMap params: Map<String, String>): Call<String>

    @FormUrlEncoded
    @POST("index.php/rest/deliveryboy/pick_order")
    fun orderPickup(@FieldMap params: Map<String, String>): Call<String>

    @FormUrlEncoded
    @POST("index.php/rest/deliveryboy/delivered_order")
    fun orderComplete(@FieldMap params: Map<String, String>): Call<String>

    @FormUrlEncoded
    @POST("index.php/rest/deliveryboy/status")
    fun setAvailable(@FieldMap params: Map<String, String>): Call<String>

    @FormUrlEncoded
    @POST("index.php/rest/deliveryboy/playerid")
    fun registerFCM(@FieldMap params: Map<String, String>): Call<String>

    @FormUrlEncoded
    @POST("index.php/rest/deliveryboy/changepassword")
    fun changePassword(@FieldMap params: Map<String, String>): Call<String>

    @FormUrlEncoded
    @POST("index.php/rest/order/list")
    fun getOrderDetail(@FieldMap params: Map<String, String>): Call<String>

}