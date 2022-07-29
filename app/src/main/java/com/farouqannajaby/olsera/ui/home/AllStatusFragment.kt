package com.farouqannajaby.olsera.ui.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.farouqannajaby.olsera.databinding.FragmentAllStatusBinding
import com.farouqannajaby.olsera.helper.ViewModelFactory
import com.farouqannajaby.olsera.ui.home.adapter.OfficeAdapter

class AllStatusFragment : Fragment() {

    private var _binding : FragmentAllStatusBinding? =  null
    private val binding get() = _binding!!

    private lateinit var homeViewModel: HomeViewModel
    private lateinit var adapter: OfficeAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAllStatusBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val index = arguments?.getInt(ARG_SECTION_NUMBER, 0)
        homeViewModel = obtainViewModel(context as AppCompatActivity)

        homeViewModel.getAllOffice().observe(viewLifecycleOwner) { officeList ->
            if (officeList != null) {
                adapter.setListOffice(officeList)
            }
        }

        adapter = OfficeAdapter()

        binding.rvvData.layoutManager = LinearLayoutManager(context)
        binding.rvvData.setHasFixedSize(true)
        binding.rvvData.adapter = adapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    private fun obtainViewModel(activity: AppCompatActivity): HomeViewModel {
        val factory = ViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory).get(HomeViewModel::class.java)
    }

    companion object {
        const val ARG_SECTION_NUMBER = "section_number"
    }
}