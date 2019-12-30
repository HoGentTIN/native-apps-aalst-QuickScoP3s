package com.quickdev.projectdashboard.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.quickdev.projectdashboard.databinding.FragmentHomeProfileBinding

class ProfileFragment : Fragment() {

	private lateinit var binding: FragmentHomeProfileBinding

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
		binding = FragmentHomeProfileBinding.inflate(inflater)
		//binding.viewModel = viewModel
		binding.lifecycleOwner = this

		return binding.root
	}
}