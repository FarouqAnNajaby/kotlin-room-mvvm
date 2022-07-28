package com.farouqannajaby.olsera.ui.home.insert

import android.app.Application
import androidx.lifecycle.ViewModel
import com.farouqannajaby.olsera.database.Office
import com.farouqannajaby.olsera.repository.OfficeRepository

class OfficeAddUpdateViewModel(application: Application) : ViewModel() {

    private val mOfficeRepository: OfficeRepository = OfficeRepository(application)
    fun insert(note: Office) {
        mOfficeRepository.insert(note)
    }
    fun update(note: Office) {
        mOfficeRepository.update(note)
    }
    fun delete(note: Office) {
        mOfficeRepository.delete(note)
    }
}