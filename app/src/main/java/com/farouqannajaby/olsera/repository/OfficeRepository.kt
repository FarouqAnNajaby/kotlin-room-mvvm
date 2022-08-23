package com.farouqannajaby.olsera.repository

import android.app.Application
import androidx.lifecycle.LiveData
import com.farouqannajaby.olsera.database.Office
import com.farouqannajaby.olsera.database.OfficeDB
import com.farouqannajaby.olsera.database.OfficeDao
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class OfficeRepository(application: Application) {

    private var officeDao: OfficeDao

    private val executorService: ExecutorService = Executors.newSingleThreadExecutor()

    init {
        val db = OfficeDB.getDatabase(application)
        officeDao = db.officeDao()
    }
    fun getAllOffice(): LiveData<List<Office>> = officeDao.getAllOffice()

    fun insert(office: Office) {
        executorService.execute { officeDao.addOffice(office) }
    }
    fun delete(office: Office) {
        executorService.execute { officeDao.deleteOffice(office) }
    }
    fun update(office: Office) {
        executorService.execute { officeDao.updateOffice(office) }
    }

    fun getOfficebyStatus(status: String): LiveData<List<Office>> = officeDao.getOfficebyStatus(status)
}