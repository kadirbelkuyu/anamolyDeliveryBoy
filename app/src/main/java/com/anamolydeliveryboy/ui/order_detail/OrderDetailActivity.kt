package com.anamolydeliveryboy.ui.order_detail

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.anamolydeliveryboy.CommonActivity
import com.anamolydeliveryboy.R
import com.anamolydeliveryboy.model.OrderModel
import com.anamolydeliveryboy.response.CommonResponse
import com.anamolydeliveryboy.ui.order_detail.adapter.OrderItemAdapter
import kotlinx.android.synthetic.main.activity_order_detail.*
import utils.ConnectivityReceiver

class OrderDetailActivity : CommonActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val orderDetailViewModel = ViewModelProvider(this).get(OrderDetailViewModel::class.java)
        setContentView(R.layout.activity_order_detail)
        setHeaderTitle(resources.getString(R.string.order_details))

        var orderModel = intent.getSerializableExtra("orderData") as OrderModel

        val fullAddress =
            "${orderModel.postal_code}, ${orderModel.house_no}, ${orderModel.add_on_house_no}, ${orderModel.city}, ${orderModel.street_name}"

        tv_order_detail_address.text = fullAddress
        tv_order_detail_id.text = orderModel.order_id
        tv_order_detail_date_time.text = CommonActivity.getConvertDate(
            orderModel.delivery_date!!,
            1
        )
        tv_order_detail_time.text =
            "${CommonActivity.getConvertTime(
                orderModel.delivery_time!!,
                2
            )} ${resources.getString(R.string.to)} ${CommonActivity.getConvertTime(
                orderModel.delivery_time_end!!,
                2
            )}"

        if (ConnectivityReceiver.isConnected) {
            val params = HashMap<String, String>()
            params["user_id"] = orderModel.user_id!!
            params["order_id"] = orderModel.order_id!!

            pb_order_detail.visibility = View.VISIBLE
            rv_order_detail.visibility = View.GONE

            orderDetailViewModel.makeGetOrderDetail(params)
                .observe(this, Observer { response: CommonResponse? ->
                    pb_order_detail.visibility = View.GONE
                    rv_order_detail.visibility = View.VISIBLE
                    if (response != null) {
                        if (response.responce!!) {

                            val gson = Gson()
                            val type = object : TypeToken<OrderModel>() {}.type
                            orderModel = gson.fromJson<OrderModel>(response.data, type)

                            rv_order_detail.apply {
                                layoutManager = LinearLayoutManager(this@OrderDetailActivity)
                                adapter =
                                    OrderItemAdapter(
                                        this@OrderDetailActivity,
                                        orderModel.orderItemModelList!!
                                    )
                                CommonActivity.runLayoutAnimation(this)
                            }

                            tv_order_detail_total_qty.text =
                                orderModel.orderItemModelList?.size.toString()

                        } else {
                            CommonActivity.showToast(this, response.message!!)
                        }
                    }
                })
        } else {
            pb_order_detail.visibility = View.GONE
        }

    }

}
