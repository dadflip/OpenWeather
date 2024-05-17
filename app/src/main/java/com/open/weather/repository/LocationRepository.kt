package com.open.weather.repository

import androidx.lifecycle.LiveData
import com.open.baselib.entity.BaseResult
import com.open.weather.db.City
import com.open.weather.db.CityDao
import com.open.weather.entity.Location
import com.open.weather.intent.ApiService2
import com.open.weather.ir.Repository
import javax.inject.Inject
import javax.inject.Named


class LocationRepository @Inject constructor(private val cityDao: CityDao, @Named("city") private var  apiService: ApiService2):Repository {


    fun insertCity(city: City){
        cityDao.insertCity(city)
    }

    fun getCityInfo(map: Map<String, String>):LiveData<BaseResult<List<Location>>>{
        return apiService.getCityInfo(map)
    }

    fun searchCity(map: Map<String, String>):LiveData<BaseResult<List<Location>>>{
        return apiService.searchCity(map)
    }

}