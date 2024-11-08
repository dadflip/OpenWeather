package com.open.baselib.liveData

import androidx.lifecycle.LiveData
import com.open.baselib.entity.BaseResult
import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.Callback
import retrofit2.Response
import java.lang.reflect.Type
import java.util.concurrent.atomic.AtomicBoolean

class LiveDataCallAdapter<T>(private val responseType: Type) : CallAdapter<T, LiveData<T>> {
    override fun adapt(call: Call<T>): LiveData<T> {
        return object : LiveData<T>() {
            private val started = AtomicBoolean(false)
            override fun onActive() {
                super.onActive()
                if (started.compareAndSet(false, true)) {//确保执行一次
                    call.enqueue(object : Callback<T> {
                        override fun onFailure(call: Call<T>, t: Throwable) {
                            //失败了就给一个错误的数据
                            val value = BaseResult<T>("500","500", "", "",null,null,null,null) as T
                            postValue(value)
                        }

                        override fun onResponse(call: Call<T>, response: Response<T>) {
                            //成功了，将数据发送出去
                            postValue(response.body())
                        }
                    })
                }
            }
        }
    }

    override fun responseType() = responseType
}