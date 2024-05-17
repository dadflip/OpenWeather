package com.open.weather.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.open.weather.R
import com.open.weather.entity.CityBasic
import com.open.weather.entity.Location
import java.lang.StringBuilder

class SearchAdapter(data:List<Location>):BaseQuickAdapter<Location, BaseViewHolder>(R.layout.item_search_layout, data){
    override fun convert(helper: BaseViewHolder, item: Location) {
        helper.setText(R.id.item_search_city_name_tv, item.name)

        val  builder=StringBuilder()
        builder.append(item.adm2)
        builder.append("  ")
        builder.append(item.adm1)
        builder.append("  ")
        builder.append(item.country)



        helper.setText(R.id.item_search_province_tv, builder.toString())
    }

}