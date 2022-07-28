package com.farouqannajaby.olsera.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface OfficeDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun addOffice(office: Office)

    @Update
    fun updateOffice(office: Office)

    @Delete
    fun deleteOffice(office: Office)

    @Query("SELECT * FROM office")
    fun getAllOffice() : LiveData<List<Office>>

//    @Query("SELECT EXISTS(SELECT * FROM office where status = :_status)")
//    suspend fun getOffice(_status: String) : List<Office>

}