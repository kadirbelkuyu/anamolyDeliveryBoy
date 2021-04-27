package com.anamolydeliveryboy.ui.completed_order

import Config.BaseURL
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.anamolydeliveryboy.CommonActivity

import com.anamolydeliveryboy.R
import com.anamolydeliveryboy.model.OrderModel
import com.anamolydeliveryboy.response.CommonResponse
import com.anamolydeliveryboy.ui.home.adapter.HomeOrderAdapter
import com.anamolydeliveryboy.ui.order_detail.OrderDetailActivity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.fragment_completed_order.view.*
import utils.ConnectivityReceiver
import utils.SessionManagement
import java.io.Serializable

class CompletedOrderFragment : Fragment() {

    lateinit var contexts: Context

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val completedOrderViewModel =
            ViewModelProvider(this).get(CompletedOrderViewModel::class.java)
        val rootView = inflater.inflate(R.layout.fragment_completed_order, container, false)

        if (ConnectivityReceiver.isConnected) {
            val params = HashMap<String, String>()
            params["delivery_boy_id"] =
                SessionManagement.UserData.getSession(contexts, BaseURL.KEY_ID)

            rootView.pb_completed_order.visibility = View.VISIBLE
            rootView.rv_completed_order.visibility = View.GONE

            completedOrderViewModel.makeGetCompletedOrderList(params)
                .observe(this, Observer { response: CommonResponse? ->
                    rootView.pb_completed_order.visibility = View.GONE
                    rootView.rv_completed_order.visibility = View.VISIBLE
                    if (response != null) {
                        if (response.responce!!) {
                            val gson = Gson()
                            val type = object : TypeToken<ArrayList<OrderModel>>() {}.type
                            val orderModelList =
                                gson.fromJson<ArrayList<OrderModel>>(response.data, type)

                            rootView.rv_completed_order.apply {
                                layoutManager = LinearLayoutManager(contexts)
                                adapter = HomeOrderAdapter(
                                    contexts,
                                    orderModelList,
                                    true,
                                    object : HomeOrderAdapter.OnItemClickListener {
                                        override fun onClick(
                                            position: Int,
                                            orderModel: OrderModel
                                        ) {
                                            Intent(context, OrderDetailActivity::class.java).apply {
                                                putExtra("orderData", orderModel as Serializable)
                                                startActivity(this)
                                            }
                                        }

                                        override fun onLocationClick(
                                            position: Int,
                                            orderModel: OrderModel
                                        ) {

                                        }

                                        override fun onPickupClick(
                                            position: Int,
                                            orderModel: OrderModel
                                        ) {

                                        }
                                    })
                                CommonActivity.runLayoutAnimation(this)
                            }
                        } else {
                            CommonActivity.showToast(contexts, response.message!!)
                        }
                    }
                })
        } else {
            rootView.pb_completed_order.visibility = View.GONE
        }

        return rootView
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        contexts = context
    }

}
