package com.farouqannajaby.olsera.ui.home.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.farouqannajaby.olsera.database.Office
import com.farouqannajaby.olsera.databinding.ItemOfficeBinding
import com.farouqannajaby.olsera.helper.OfficeDiffCallback
import com.farouqannajaby.olsera.ui.home.insert.AddUpdateOfficeActivity

class OfficeAdapter : RecyclerView.Adapter<OfficeAdapter.OfficeViewHolder>() {

    private val listOffice = ArrayList<Office>()

    fun setListOffice(listOffice: List<Office>) {
        val diffCallback = OfficeDiffCallback(this.listOffice, listOffice)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        this.listOffice.clear()
        this.listOffice.addAll(listOffice)
        diffResult.dispatchUpdatesTo(this)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OfficeViewHolder {

        val binding = ItemOfficeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return OfficeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: OfficeViewHolder, position: Int) {
        holder.bind(listOffice[position])
    }

    override fun getItemCount(): Int {
        return listOffice.size
    }

    inner class OfficeViewHolder(private val binding: ItemOfficeBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(office: Office) {
            with(binding) {
                tvNameAndCity.text = office.title
                tvActive.text = office.status
                binding.btnArrow.setOnClickListener {
                    val intent = Intent(it.context, AddUpdateOfficeActivity::class.java)
                    intent.putExtra(AddUpdateOfficeActivity.EXTRA_OFFICE, office)
                    intent.putExtra(AddUpdateOfficeActivity.EXTRA_LATITUDE, office)
                    intent.putExtra(AddUpdateOfficeActivity.EXTRA_LONGTITUDE, office)
                    it.context.startActivity(intent)
                }
            }
        }
    }
}
