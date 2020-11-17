package com.jeoksyeo.wet.activity.alcohol_rated

import android.content.Context
import com.vuforia.engine.wet.databinding.AlcoholRatedBinding

interface AlcoholRatedContact {

    interface BaseView{
        fun getView():AlcoholRatedBinding
    }

    interface BasesPresenter {
        var view: BaseView
        fun initProfile(provider: String?)

        fun initTabLayout(context: Context)

    }
}