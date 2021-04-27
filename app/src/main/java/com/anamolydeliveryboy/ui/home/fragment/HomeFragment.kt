package com.anamolydeliveryboy.ui.home.fragment

import Config.BaseURL
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.anamolydeliveryboy.CommonActivity
import com.anamolydeliveryboy.R
import com.anamolydeliveryboy.model.OrderModel
import com.anamolydeliveryboy.response.CommonResponse
import com.anamolydeliveryboy.ui.home.adapter.HomeOrderAdapter
import com.anamolydeliveryboy.ui.home.adapter.OrderPickupAdapter
import com.anamolydeliveryboy.ui.order_detail.OrderDetailActivity
import com.anamolydeliveryboy.ui.tracking.TrackingActivity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.anamolydeliveryboy.ui.home.MainActivity
import dialogs.LoaderDialog
import kotlinx.android.synthetic.main.fragment_home.view.*
import org.json.JSONArray
import org.json.JSONObject
import utils.ConnectivityReceiver
import utils.SessionManagement
import utils.TrackerService
import java.io.Serializable

class HomeFragment : Fragment() {

    companion object {
        val TAG = HomeFragment::class.java.simpleName
    }

    val pickupOrderModelList = ArrayList<OrderModel>()
    val assignOrderModelList = ArrayList<OrderModel>()

    lateinit var orderPickupAdapter: OrderPickupAdapter
    lateinit var homeOrderAdapter: HomeOrderAdapter

    private lateinit var homeViewModel: HomeViewModel
    private lateinit var contexts: Context

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        val rootView = inflater.inflate(R.layout.fragment_home, container, false)

        orderPickupAdapter = OrderPickupAdapter(
            contexts,
            pickupOrderModelList,
            object : OrderPickupAdapter.OnItemClickListener {
                override fun onClick(position: Int, orderModel: OrderModel) {
                    Intent(context, OrderDetailActivity::class.java).apply {
                        putExtra("orderData", orderModel as Serializable)
                        startActivity(this)
                    }
                }

                override fun onDeliveredClick(position: Int, orderModel: OrderModel) {
                    if (ConnectivityReceiver.isConnected) {
                        makeDelivered(position, orderModel)
                    }
                }
            })

        homeOrderAdapter = HomeOrderAdapter(
            contexts,
            assignOrderModelList,
            false,
            object : HomeOrderAdapter.OnItemClickListener {
                override fun onClick(position: Int, orderModel: OrderModel) {
                    Intent(context, OrderDetailActivity::class.java).apply {
                        putExtra("orderData", orderModel as Serializable)
                        startActivity(this)
                    }
                }

                override fun onLocationClick(position: Int, orderModel: OrderModel) {
                    Intent(context, TrackingActivity::class.java).apply {
                        putExtra("orderData", orderModel as Serializable)
                        startActivity(this)
                    }
                }

                override fun onPickupClick(position: Int, orderModel: OrderModel) {
                    if (ConnectivityReceiver.isConnected) {
                        homeViewModel.permissionLiveData.observe(
                            this@HomeFragment,
                            Observer { hasGranted ->
                                if (hasGranted) {
                                    homeViewModel.locationUnableLiveData.observe(this@HomeFragment,
                                        Observer { result ->
                                            if (result) {
                                                makePickup(position, orderModel)
                                            } else {
                                                (contexts as MainActivity).displayLocationSettingsRequest(
                                                    contexts
                                                )
                                            }
                                            homeViewModel.locationUnableLiveData.removeObservers(
                                                this@HomeFragment
                                            )
                                        })
                                    homeViewModel.checkLocationSettingsRequest(contexts)
                                }
                                homeViewModel.permissionLiveData.removeObservers(this@HomeFragment)
                            })
                        homeViewModel.checkPermission(contexts)
                    }
                }
            })

        rootView.rv_home_pickup_order.apply {
            layoutManager = LinearLayoutManager(contexts)
            adapter = orderPickupAdapter
        }

        rootView.rv_home_assigned_order.apply {
            layoutManager = LinearLayoutManager(contexts)
            adapter = homeOrderAdapter
        }

        if (ConnectivityReceiver.isConnected) {
            val params = HashMap<String, String>()
            params["delivery_boy_id"] =
                SessionManagement.UserData.getSession(contexts, BaseURL.KEY_ID)

            rootView.pb_home_assign.visibility = View.VISIBLE
            rootView.rv_home_assigned_order.visibility = View.INVISIBLE

            homeViewModel.makeGetHomeData(params)
                .observe(this, Observer { response: CommonResponse? ->
                    rootView.pb_home_assign.visibility = View.GONE
                    rootView.rv_home_assigned_order.visibility = View.VISIBLE
                    pickupOrderModelList.clear()
                    assignOrderModelList.clear()
                    if (response != null) {
                        if (response.responce!!) {

                            val jsonObject = JSONObject(response.data)

                            val gson = Gson()
                            val type = object : TypeToken<ArrayList<OrderModel>>() {}.type
                            val pickedOrderList = gson.fromJson<ArrayList<OrderModel>>(
                                jsonObject.getJSONArray("picked").toString(), type
                            )
                            val assignOrderList = gson.fromJson<ArrayList<OrderModel>>(
                                jsonObject.getJSONArray("assigned").toString(), type
                            )

                            pickupOrderModelList.addAll(pickedOrderList)
                            assignOrderModelList.addAll(assignOrderList)
                            orderPickupAdapter.notifyDataSetChanged()
                            homeOrderAdapter.notifyDataSetChanged()

                            CommonActivity.runLayoutAnimation(rootView.rv_home_pickup_order)
                            CommonActivity.runLayoutAnimation(rootView.rv_home_assigned_order)

                            SessionManagement.PermanentData.setSession(
                                contexts,
                                "orderIds",
                                ""
                            )

                            if (pickedOrderList.size > 0) {
                                val stroHasmap = HashMap<String, Any>()
                                for (orderModelpicked in pickedOrderList) {
                                    stroHasmap[orderModelpicked.order_id!!.toString()] = "0"
                                }
                                updateServiceOrderId(stroHasmap)
                            } else {
                                stopTrackerService()
                            }
                        } else {
                            CommonActivity.showToast(contexts, response.message!!)
                        }
                    }
                })
        } else {
            rootView.pb_home_assign.visibility = View.GONE
        }

        return rootView
    }

    private fun makePickup(position: Int, orderModel: OrderModel) {

        val params = HashMap<String, String>()
        params["delivery_boy_id"] =
            SessionManagement.UserData.getSession(contexts, BaseURL.KEY_ID)
        params["order_id"] = orderModel.order_id!!

        val loaderDialog = LoaderDialog(contexts)
        loaderDialog.show()

        homeViewModel.makePickupOrder(params).observe(this, Observer { response: CommonResponse? ->
            loaderDialog.dismiss()
            if (response != null) {
                if (response.responce!!) {
                    assignOrderModelList.removeAt(position)
                    homeOrderAdapter.notifyItemRemoved(position)
                    homeOrderAdapter.notifyItemRangeChanged(0, assignOrderModelList.size)

                    pickupOrderModelList.add(orderModel)
                    orderPickupAdapter.notifyItemInserted(pickupOrderModelList.size - 1)
                    orderPickupAdapter.notifyItemRangeChanged(0, pickupOrderModelList.size)

                    val orderids = SessionManagement.PermanentData.getSession(contexts, "orderIds")
                    val stroHasmap = CommonActivity.convertToHashMap(orderids)
                    stroHasmap[orderModel.order_id.toString()] = "0"
                    updateServiceOrderId(stroHasmap)
                } else {
                    CommonActivity.showToast(contexts, response.message!!)
                }
            }
        })
    }

    private fun makeDelivered(position: Int, orderModel: OrderModel) {

        val params = HashMap<String, String>()
        params["delivery_boy_id"] =
            SessionManagement.UserData.getSession(contexts, BaseURL.KEY_ID)
        params["order_id"] = orderModel.order_id!!

        val loaderDialog = LoaderDialog(contexts)
        loaderDialog.show()

        homeViewModel.makeCompleteOrder(params)
            .observe(this, Observer { response: CommonResponse? ->
                loaderDialog.dismiss()
                if (response != null) {
                    if (response.responce!!) {
                        pickupOrderModelList.removeAt(position)
                        orderPickupAdapter.notifyItemRemoved(position)
                        orderPickupAdapter.notifyItemRangeChanged(0, assignOrderModelList.size)

                        val orderids =
                            SessionManagement.PermanentData.getSession(contexts, "orderIds")
                        val stroHasmap = CommonActivity.convertToHashMap(orderids)
                        stroHasmap.remove(orderModel.order_id.toString())
                        updateServiceOrderId(stroHasmap)

                    } else {
                        CommonActivity.showToast(contexts, response.message!!)
                    }
                }
            })
    }

    private fun updateServiceOrderId(storHasmap: HashMap<String, Any>) {

        val jsonArray = JSONArray()
        for (hasmap in storHasmap) {
            val jsonObject = JSONObject()
            jsonObject.put(hasmap.key, hasmap.value)
            jsonArray.put(jsonObject)
        }
        SessionManagement.PermanentData.setSession(
            contexts,
            "orderIds",
            jsonArray.toString()
        )

        if (jsonArray.length() > 0) {
            checkPermission()
        } else {
            stopTrackerService()
        }
    }

    private fun checkPermission() {
        homeViewModel.permissionLiveData.observe(
            this@HomeFragment,
            Observer { hasGranted ->
                if (hasGranted) {
                    (contexts as MainActivity).displayLocationSettingsRequest(contexts)
                }
                homeViewModel.permissionLiveData.removeObservers(this@HomeFragment)
            })
        homeViewModel.checkPermission(contexts)
    }

    fun startTrackerService() {
        if (::contexts.isInitialized) {
            SessionManagement.PermanentData.setSession(contexts, "isServiceStart", true)
            contexts.startService(Intent(contexts, TrackerService::class.java))
        }
    }

    fun stopTrackerService() {
        if (::contexts.isInitialized) {
            SessionManagement.PermanentData.setSession(contexts, "isServiceStart", false)
            contexts.stopService(Intent(contexts, TrackerService::class.java))
        }
    }

    override fun onResume() {
        super.onResume()
        if (!SessionManagement.PermanentData.getSessionBoolean(contexts, "isServiceStart")) {
            Log.d("HomeFragment", "service Stop")
            val notificationManager =
                contexts.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.cancelAll()
            contexts.stopService(Intent(contexts, TrackerService::class.java))
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        contexts = context
    }

}
