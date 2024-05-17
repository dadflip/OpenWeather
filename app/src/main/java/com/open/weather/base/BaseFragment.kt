package com.open.weather.base

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.open.weather.config.Config

open class BaseFragment :Fragment(){


    override fun onCreate(savedInstanceState: Bundle?) {

            if (Build.VERSION.SDK_INT>= 21) {
                val decorView: View = requireActivity().window.decorView
                decorView.systemUiVisibility = (
                        View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                                or View.SYSTEM_UI_FLAG_LAYOUT_STABLE)
                requireActivity().window.statusBarColor = Color.TRANSPARENT;


            }

        super.onCreate(savedInstanceState)
    }


}