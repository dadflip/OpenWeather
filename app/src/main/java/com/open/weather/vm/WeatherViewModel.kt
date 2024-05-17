package com.open.weather.vm

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import com.open.baselib.entity.BaseResult
import com.open.weather.base.BaseViewModel
import com.open.weather.entity.AirEntity
import com.open.weather.entity.DailyEntity
import com.open.weather.entity.HourlyEntity
import com.open.weather.entity.NowEntity
import com.open.weather.repository.WeatherRepository

class WeatherViewModel @ViewModelInject constructor(private val weatherRepository: WeatherRepository) :BaseViewModel(){


    fun getWeatherNowInfo(id:String):LiveData<BaseResult<NowEntity>>{
       return weatherRepository.getNowWeatherInfo(getParams(id))

    }

    fun getFutureWeatherList(id:String):LiveData<BaseResult<List<DailyEntity>>>{
        val map=getParams(id)
        return weatherRepository.getFutureWeatherList(map)
    }

    fun getAirNowInfo(id:String):LiveData<BaseResult<AirEntity>>{
        return weatherRepository.getAirNowInfo(getParams(id))
    }


    fun getHourlyList(id: String):LiveData<BaseResult<List<HourlyEntity>>>{
        return weatherRepository.getHourlyList(getParams(id))
    }




}