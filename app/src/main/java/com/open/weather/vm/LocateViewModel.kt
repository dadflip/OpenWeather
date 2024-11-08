package com.open.weather.vm

import android.text.TextUtils
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.google.gson.Gson
import com.open.baselib.entity.BaseResult
import com.open.weather.base.BaseViewModel
import com.open.weather.config.Config
import com.open.weather.db.AppDataBase
import com.open.weather.db.City
import com.open.weather.db.CityDao
import com.open.weather.db.CityWeatherDao
import com.open.weather.entity.*
import com.open.weather.repository.LocationRepository


class LocateViewModel @ViewModelInject constructor(private val locationRepository: LocationRepository, private val mDao: CityDao) :BaseViewModel(){

    private val mCityWeatherDao: CityWeatherDao by lazy {
        AppDataBase.instance.getCityWeatherDao()
    }


    val mInputCity:MutableLiveData<String> = MutableLiveData()

    private lateinit var mSearchLiveData: MutableLiveData<CityEntity>

    private lateinit var mLocateLiveData:LiveData<List<LocateEntity>>

    private lateinit var mCityInfoLiveData:LiveData<BaseResult<List<Location>>>





    fun searchCity(name:String):LiveData<BaseResult<List<Location>>>{
        return locationRepository.searchCity(getParams(name))
    }



    fun getCityList():LiveData<List<City>>{
        return mDao.getAll()
    }

    fun getLocateCity():City{
       return mDao.getLocalCity()
    }


    fun insertCity(city: City){
       mDao.insertCity(city)
    }

    fun deleteCity(city: City){
        mDao.deleteCity(city)
    }


    fun getCityById(i:Int):City{
        return mDao.getCityById(i)
    }



    /**
     * 获取city列表并且转换成我们需要的List<LocateEntity>
     */
    fun getWeatherList():LiveData<List<LocateEntity>>{
        mLocateLiveData=Transformations.map(mDao.getAll()){
            list->

            transform(list)
        }

        return mLocateLiveData
    }

    fun getCityInfo(name:String):LiveData<BaseResult<List<Location>>>{
        //mCityInfoLiveData =mRepository.getCityInfo(getCityInfoParams(name))
        return  locationRepository.getCityInfo(getCityInfoParams(name))
    }

    fun getCityInfoLiveData():LiveData<BaseResult<List<Location>>>{
        return mCityInfoLiveData
    }



    private fun getCityInfoParams(name: String):Map<String, String>{
        return  mutableMapOf("location" to name,"key" to Config.API_KEY)
    }

    private fun transform(list: List<City>):List<LocateEntity>{
        var locateList= ArrayList<LocateEntity>()
        for (city in list){
            var now:NowEntity?=null
            var oneDay:DailyEntity?=null
            val weather=mCityWeatherDao.getWeatherById(city.id!!)

            if (weather.nowWeather!=null&&!TextUtils.isEmpty(weather.nowWeather)){
               now =Gson().fromJson(weather.nowWeather, NowEntity::class.javaObjectType)

            }
            if (weather.oneDay!=null&&!TextUtils.isEmpty(weather.oneDay)){
                oneDay=Gson().fromJson(weather.oneDay, DailyEntity::class.javaObjectType)
            }

            locateList.add(
                LocateEntity(city,
                    select = false,
                    open = false,
                    now = now,
                    oneDay = oneDay
                )
            )
        }
        return locateList

    }


}