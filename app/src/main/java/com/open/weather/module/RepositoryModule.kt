package com.open.weather.module

import com.open.weather.db.CityDao
import com.open.weather.intent.ApiService2
import com.open.weather.repository.LocationRepository
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.scopes.ActivityRetainedScoped
import javax.inject.Singleton


@Module
@InstallIn(ActivityRetainedComponent::class)
object RepositoryModule {


    @Singleton
    @ActivityRetainedScoped
    fun  providerLocationRepository(cityDao: CityDao, apiService: ApiService2): LocationRepository {
        return LocationRepository(cityDao, apiService)
    }

}