package com.anamolydeliveryboy.ui.order_detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.anamolydeliveryboy.repository.ProjectRepository
import com.anamolydeliveryboy.response.CommonResponse

/**
 * Created on 06-04-2020.
 */
class OrderDetailViewModel : ViewModel() {
    val projectRepository = ProjectRepository()

    fun makeGetOrderDetail(params: HashMap<String, String>): LiveData<CommonResponse?> {
        return projectRepository.getOrderDetail(params)
    }

}