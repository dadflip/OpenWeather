package com.open.weather.entity

import com.open.weather.db.City

data class LocateEntity(var city:City,var select:Boolean, var open:Boolean, var now:NowEntity?,var oneDay:DailyEntity?)