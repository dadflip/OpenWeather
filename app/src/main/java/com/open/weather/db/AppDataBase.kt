package com.open.weather.db

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.open.weather.OpenApplication

@Database(entities = [City::class, CityWeather::class], version = 3 , exportSchema = false)
abstract class AppDataBase :RoomDatabase(){
    abstract fun getCityDao():CityDao
    abstract fun getCityWeatherDao():CityWeatherDao

    companion object{
       private val MIGRATION_1_2= object :Migration(1,2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE City ADD COLUMN updateTime TEXT")
            }
        }
       private val MIGRATION_2_3=object :Migration(2,3){
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("CREATE TABLE 'CityWeather'('id' INTEGER,'now_weather' TEXT,'one_day_w' TEXT,Primary Key(id)) ")
            }
        }
        val instance = Single.sin


    }



    private object Single {

        val sin :AppDataBase= Room.databaseBuilder(
            OpenApplication.getContext(),
            AppDataBase::class.java,
            "City.db"
        )
            .allowMainThreadQueries()
            .addMigrations(MIGRATION_1_2)
            .addMigrations(MIGRATION_2_3)
            .build()
    }



}