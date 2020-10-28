package com.model.common

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName

class Name {
    @SerializedName("kr")
    @Expose
    var kr: String? = null

    @SerializedName("en")
    @Expose
    var en: String? = null
}