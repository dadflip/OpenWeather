package com.open.weather

import android.app.Application
import android.content.Context
import androidx.multidex.MultiDex
import com.open.baselib.utils.ToastUtil
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class OpenApplication :Application(){

    companion object{
        var instance:OpenApplication?=null

        fun getContext():Context{
            return instance!!
        }
    }


    override fun onCreate() {
        super.onCreate()
        instance=this
        ToastUtil.init(this)
        MultiDex.install(this)

    }
}