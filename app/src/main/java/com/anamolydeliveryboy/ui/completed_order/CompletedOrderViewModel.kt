package com.anamolydeliveryboy.ui.completed_order

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.anamolydeliveryboy.repository.ProjectRepository
import com.anamolydeliveryboy.response.CommonResponse

/**
 * Created on 06-04-2020.
 */
class CompletedOrderViewModel : ViewModel() {
    val projectRepository = ProjectRepository()

    fun makeGetCompletedOrderList(params: HashMap<String, String>): LiveData<CommonResponse?> {
        return projectRepository.getDeliveredOrderList(params)
    }
}