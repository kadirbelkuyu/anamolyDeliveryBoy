package com.anamolydeliveryboy.ui.home.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Switch
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.anamolydeliveryboy.R
import com.anamolydeliveryboy.model.SideMenuModel
import kotlinx.android.synthetic.main.row_side_menu.view.*


class SideMenuAdapter(
    val context: Context,
    val modelList: ArrayList<SideMenuModel>,
    val onItemClickListener: OnItemClickListener
) :
    RecyclerView.Adapter<SideMenuAdapter.MyViewHolder>() {

    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val iv_img: ImageView = view.iv_side_menu_arrow
        val tv_title: TextView = view.tv_side_menu_title
        val switch: Switch = view.sw_side_menu

        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                val sideMenuModel = modelList[position]

                if (sideMenuModel.isEnable) {
                    switch.isChecked = !switch.isChecked
                    sideMenuModel.isChecked = switch.isChecked
                }

                onItemClickListener.onClick(position, sideMenuModel)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.row_side_menu, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val mList = modelList[position]

        holder.tv_title.text = mList.title

        if (mList.isEnable) {
            holder.iv_img.visibility = View.GONE
            holder.switch.visibility = View.VISIBLE
        } else {
            holder.iv_img.visibility = View.VISIBLE
            holder.switch.visibility = View.GONE
        }

        holder.switch.isChecked = mList.isChecked

    }

    override fun getItemCount(): Int {
        return modelList.size
    }

    interface OnItemClickListener {
        fun onClick(position: Int, sideMenuModel: SideMenuModel)
    }

}