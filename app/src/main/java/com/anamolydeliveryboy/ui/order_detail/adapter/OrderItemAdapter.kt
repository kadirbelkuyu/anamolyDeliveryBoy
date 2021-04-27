package com.anamolydeliveryboy.ui.order_detail.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.anamolydeliveryboy.CommonActivity
import com.anamolydeliveryboy.R
import com.anamolydeliveryboy.model.OrderItemModel
import kotlinx.android.synthetic.main.row_order_item.view.*


class OrderItemAdapter(
    val context: Context,
    val modelList: ArrayList<OrderItemModel>
) :
    RecyclerView.Adapter<OrderItemAdapter.MyViewHolder>() {

    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tv_title: TextView = view.tv_order_item_title
        val tv_unit: TextView = view.tv_order_item_unit
        val tv_qty: TextView = view.tv_order_item_qty

        init {

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.row_order_item, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val mList = modelList[position]

        holder.tv_title.text = CommonActivity.getStringByLanguage(
            context,
            mList.product_name_en,
            mList.product_name_ar,
            mList.product_name_nl
        )
        holder.tv_unit.text = "${mList.unit_value} ${mList.unit}"
        holder.tv_qty.text = mList.order_qty

    }

    override fun getItemCount(): Int {
        return modelList.size
    }

}