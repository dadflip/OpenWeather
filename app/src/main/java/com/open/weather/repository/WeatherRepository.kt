package com.open.weather.repository

import androidx.lifecycle.LiveData
import com.open.baselib.entity.BaseResult
import com.open.weather.db.CityWeatherDao
import com.open.weather.entity.AirEntity
import com.open.weather.entity.DailyEntity
import com.open.weather.entity.HourlyEntity
import com.open.weather.entity.NowEntity
import com.open.weather.intent.ApiService2
import com.open.weather.ir.Repository
import javax.inject.Inject

/**
 * 天气相关的仓库类，负责从网络获取天气相关的数据
 */
class WeatherRepository @Inject constructor(private val weatherDao: CityWeatherDao,
                    private val apiService: ApiService2
):Repository{

    fun getNowWeatherInfo(map: Map<String, String>):LiveData<BaseResult<NowEntity>>{
        return apiService.getNoWData(map)
    }

    fun getFutureWeatherList(map: Map<String, String>):LiveData<BaseResult<List<DailyEntity>>>{
        return apiService.getFutureWeatherList(map)
    }

    fun getAirNowInfo(map: Map<String, String>):LiveData<BaseResult<AirEntity>>{
        return apiService.getAriNow(map)
    }

    fun getHourlyList(map: Map<String, String>):LiveData<BaseResult<List<HourlyEntity>>>{
        return apiService.getHourlyList(map)
    }

}