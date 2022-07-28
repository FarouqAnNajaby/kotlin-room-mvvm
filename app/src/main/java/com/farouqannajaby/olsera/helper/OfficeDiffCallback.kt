package com.farouqannajaby.olsera.helper

import androidx.recyclerview.widget.DiffUtil
import com.farouqannajaby.olsera.database.Office

class OfficeDiffCallback(private val mOldOffice: List<Office>,
                         private val mNewOffice: List<Office>) : DiffUtil.Callback() {

    override fun getOldListSize(): Int {
        return mOldOffice.size
    }

    override fun getNewListSize(): Int {
        return mNewOffice.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return mOldOffice[oldItemPosition].id == mNewOffice[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldEmployee = mOldOffice[oldItemPosition]
        val newEmployee = mNewOffice[newItemPosition]
        return oldEmployee.title == newEmployee.title &&
                oldEmployee.alamat == newEmployee.alamat
    }
}