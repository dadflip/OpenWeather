package com.open.weather.location

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.open.baselib.base.BaseActivity
import com.open.baselib.utils.L
import com.open.weather.R
import com.open.weather.adapter.LocateManageAdapter
import com.open.weather.databinding.ActivityLocationManagerBinding
import com.open.weather.db.AppDataBase
import com.open.weather.db.CityDao
import com.open.weather.db.City
import com.open.weather.db.CityWeatherDao
import com.open.weather.entity.LocateEntity
import com.open.weather.utils.RecycleViewDivider
import com.open.weather.vm.LocateViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

/**
 * 地点管理Activity
 */
@AndroidEntryPoint
class LocationManageActivity : BaseActivity() {

    private lateinit var mCityListView: RecyclerView
    private var mCityList = arrayListOf<LocateEntity>()
    private lateinit var mBindIng: ActivityLocationManagerBinding
    private var mLastId: Int = 0
    private var mIsOpen = false
    private var mDeleteArrayPos = arrayListOf<Int>()
    private lateinit var mDialogBuilder:AlertDialog.Builder


    private val mAdapter: LocateManageAdapter by lazy {
        LocateManageAdapter(mCityList)
    }

    private val mLocateViewModel: LocateViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBindIng = DataBindingUtil.setContentView(this, R.layout.activity_location_manager)

        mBindIng.handler = ManagerHandler()
        mCityListView = mBindIng.locateCityRlv
        mBindIng.isOpen = mIsOpen

        initView()
        initData()
    }

    override fun initView() {
        mCityListView.layoutManager = LinearLayoutManager(this) as RecyclerView.LayoutManager?
        mCityListView.addItemDecoration(RecycleViewDivider(this, LinearLayoutManager.VERTICAL))
        mCityListView.adapter = mAdapter

        mAdapter.setOnItemChildClickListener { _, view, position ->
            when (view.id) {
                R.id.item_locate_delete_cb -> {
                    mAdapter.data[position].select = !mAdapter.data[position].select
                    // mAdapter.notifyItemChanged(position)
                    if (mAdapter.data[position].select) {
                        mDeleteArrayPos.add(position)
                    }
                }
            }
        }

    }

    override fun initData() {
        super.initData()

        mLocateViewModel.getWeatherList().observe(this, Observer<List<LocateEntity>> { data ->
            mCityList = data as ArrayList<LocateEntity>
            if (mCityList.isNotEmpty()) {
                mAdapter.setNewData(data)
                mLastId = data[data.size - 1].city.id!!
            }

        })


    }

    private fun initSelect() {
        for ((i, locate) in mCityList.withIndex()) {
            mCityList[i].open = mIsOpen
        }
        mAdapter.setNewData(mCityList)

        if (mIsOpen) {
            mBindIng.locateDeleteTv.visibility = View.VISIBLE
        } else
            mBindIng.locateDeleteTv.visibility = View.GONE


    }

    override fun onBackPressed() {
        if (mIsOpen) {
            mIsOpen = false
            initSelect()
        } else
            super.onBackPressed()

    }

    inner class ManagerHandler {

        fun toSearch(view: View) {
            var intent = Intent(this@LocationManageActivity, SearchActivity::class.java)
            intent.putExtra(SearchActivity.KEY_LAST_ID, mLastId)
            startActivity(intent)

        }

        fun back(view: View) {
            finish()
        }

        fun openMore(view: View) {
            mIsOpen = true
            mBindIng.isOpen = mIsOpen
            initSelect()
        }

        fun delete(view: View) {


            mDialogBuilder=AlertDialog.Builder(this@LocationManageActivity).setTitle("提示")
                .setMessage("是否确定删除")
                .setPositiveButton("确定") { _, _ ->  deleteCityList()}
                .setNegativeButton("取消") { dialog, _ -> dialog.dismiss()}

            mDialogBuilder.create().show()

        }
    }

    private fun deleteCityList(){
        for (mDeleteArrayPo in mDeleteArrayPos) {
            mLocateViewModel.deleteCity(mCityList[mDeleteArrayPo].city)

        }
    }


}