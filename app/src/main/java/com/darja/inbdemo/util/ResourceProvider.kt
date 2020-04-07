package com.darja.inbdemo.util

import android.content.res.Resources
import androidx.annotation.StringRes

class ResourceProvider(private val resources: Resources) {

    fun getString(@StringRes resId: Int) =
        resources.getString(resId)

    fun getString(@StringRes resId: Int, vararg args: Any) =
        resources.getString(resId, *args)

}