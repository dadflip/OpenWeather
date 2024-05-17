package com.open.weather.location

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.open.baselib.base.BaseActivity
import com.open.baselib.utils.ToastUtil
import com.open.weather.MainActivity
import com.open.weather.R
import com.open.weather.adapter.SearchAdapter
import com.open.weather.databinding.ActivitySearchBinding
import com.open.weather.db.City
import com.open.weather.entity.Location
import com.open.weather.intent.Api
import com.open.weather.vm.LocateViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class SearchActivity : BaseActivity(){

    companion object{
        const val KEY_LAST_ID="last_id"
    }

    private var mCityDaoList= listOf<City>()

    private val mLocateViewModel: LocateViewModel by viewModels()
    private var mCityList= arrayListOf<Location>()

    private lateinit var mRecyclerView:RecyclerView

    private val mAdapter by lazy {
        SearchAdapter(mCityList)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val bindIng:ActivitySearchBinding=DataBindingUtil.setContentView(this, R.layout.activity_search)

        bindIng?.let {
            it.viewModel=mLocateViewModel
            it.lifecycleOwner=this
            it.handler=SearchHandler()
        }
        mRecyclerView=bindIng.searchRlv
        initView()
        initData()
    }



    override fun initView() {
        mRecyclerView.layoutManager=LinearLayoutManager(this)
        mRecyclerView.adapter=mAdapter
        initListener()
    }


    override fun initData() {
        super.initData()
        mLocateViewModel.getCityList().observe(this, Observer<List<City>>{
            data-> mCityDaoList=data
        })

    }

    private fun initListener(){
        mAdapter.setOnItemClickListener  { _, _, position ->
            var c=mCityList[position]

            var lastId=intent.getIntExtra(KEY_LAST_ID,0)

            lastId++
            if (mCityDaoList.isEmpty()){
                val city=City(lastId,c.name, c.adm1,1,"",c.id)

                mLocateViewModel.insertCity(city)
            }else{
                if (cityExist(c.name)){
                    ToastUtil.showMessage("城市已经添加了")
                    return@setOnItemClickListener
                }
                val city=City(lastId,c.name, c.adm1,0,"",c.id)
                mLocateViewModel.insertCity(city)
            }

            val  intent=Intent(this, MainActivity::class.java)
            intent.putExtra(MainActivity.KEY_IS_ADD,true)
            startActivity(intent)
        }
    }

   private fun searchCity(content:String){

       mLocateViewModel.searchCity(content).observe(this) { data ->
           run {
               if (data.code==Api.SUCCESS_STATUS){
                   mCityList = data.location as ArrayList<Location>
                   mAdapter.setNewData(mCityList)
               }

           }
       }
   }


    private fun cityExist(name:String):Boolean{
        for (city in mCityDaoList){
            if (name == city.name){
                return true
            }
        }
        return false
    }


    inner class SearchHandler{
        //搜索点击事件  获取的值是liveData里面的值
        fun  search(view:View,content:String){
            searchCity(content)
        }

        fun back(view: View){
            finish()
        }
    }



}