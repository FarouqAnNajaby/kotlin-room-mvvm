package com.farouqannajaby.olsera.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [Office::class],
    version = 1
)

abstract class OfficeDB : RoomDatabase(){

    abstract fun officeDao(): OfficeDao
    companion object {
        @Volatile
        private var INSTANCE: OfficeDB? = null
        @JvmStatic
        fun getDatabase(context: Context): OfficeDB {
            if (INSTANCE == null) {
                synchronized(OfficeDB::class.java) {
                    INSTANCE = Room.databaseBuilder(context.applicationContext,
                        OfficeDB::class.java, "office_database")
                        .build()
                }
            }
            return INSTANCE as OfficeDB
        }
    }
}