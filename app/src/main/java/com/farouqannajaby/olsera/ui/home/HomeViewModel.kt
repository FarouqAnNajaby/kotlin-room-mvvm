package com.farouqannajaby.olsera.ui.home

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.farouqannajaby.olsera.database.Office
import com.farouqannajaby.olsera.repository.OfficeRepository

class HomeViewModel(application: Application) : ViewModel() {

    private val officeRepository: OfficeRepository = OfficeRepository(application)

    fun getAllOffice(): LiveData<List<Office>> = officeRepository.getAllOffice()

    fun getOfficeByStatus(status: String?): LiveData<List<Office>>? =
        status?.let { officeRepository.getOfficebyStatus(it) }

}