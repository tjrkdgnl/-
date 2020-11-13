package com.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.application.GlobalApplication

class AlcoholCategoryViewModel: ViewModel() {

    //보기 방식을 바꿨을 때 현재 sort에 따라서 셋팅되어야함.
    var currentSort:String = GlobalApplication.DEFAULT_SORT


    var currentPosition = MutableLiveData(0)

    var totalCountList = mutableListOf(0,0,0,0,0)
}