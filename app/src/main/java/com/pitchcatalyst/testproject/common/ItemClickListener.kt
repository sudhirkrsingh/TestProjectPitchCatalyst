package com.pitchcatalyst.testproject.common

import com.pitchcatalyst.testproject.data.DataReq

interface ItemClickListener {
    fun onCheckboxClick(item: DataReq, isChecked: Boolean)
}