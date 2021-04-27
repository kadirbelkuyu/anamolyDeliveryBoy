package com.anamolydeliveryboy.model

import java.io.Serializable

/**
 * Created on 04-04-2020.
 */
class SideMenuModel : Serializable {

    var title: String = ""
    var isEnable: Boolean = false
    var isSelected: Boolean = false
    var isChecked: Boolean = false

    constructor()

    constructor(title: String, isEnable: Boolean, isSelected: Boolean) {
        this.title = title
        this.isEnable = isEnable
        this.isSelected = isSelected
    }

    constructor(title: String, isEnable: Boolean, isSelected: Boolean, isChecked: Boolean) {
        this.title = title
        this.isEnable = isEnable
        this.isSelected = isSelected
        this.isChecked = isChecked
    }

}