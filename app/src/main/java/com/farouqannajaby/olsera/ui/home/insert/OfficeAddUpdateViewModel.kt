package com.farouqannajaby.olsera.ui.home.insert

import android.app.Application
import androidx.lifecycle.ViewModel
import com.farouqannajaby.olsera.database.Office
import com.farouqannajaby.olsera.repository.OfficeRepository

class OfficeAddUpdateViewModel(application: Application) : ViewModel() {

    private val mOfficeRepository: OfficeRepository = OfficeRepository(application)
    fun insert(office: Office) {
        mOfficeRepository.insert(office)
    }
    fun update(office: Office) {
        mOfficeRepository.update(office)
    }
    fun delete(office: Office) {
        mOfficeRepository.delete(office)
    }
}