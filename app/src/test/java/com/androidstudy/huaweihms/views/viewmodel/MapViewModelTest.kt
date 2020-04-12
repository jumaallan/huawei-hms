package com.androidstudy.huaweihms.views.viewmodel

import com.androidstudy.huaweihms.data.remote.LocationDataRequest
import com.androidstudy.huaweihms.data.remote.LocationRequest
import com.androidstudy.huaweihms.repository.MapRepository
import com.nhaarman.mockitokotlin2.mock
import org.junit.Test

class MapViewModelTest {

    @Test
    fun `fetch reverse geo code using current latlong`() {

    }

    private fun createViewModel(
        mapRepository: MapRepository = mock()
    ) = MapViewModel(
        mapRepository = mapRepository
    )

    companion object {
        val fakeLocation = LocationRequest(
            LocationDataRequest(
                -1.101822,
                37.014404
            ),
            "en",
            "KE",
            true
        )
    }
}