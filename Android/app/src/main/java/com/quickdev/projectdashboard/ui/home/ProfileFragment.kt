package com.quickdev.projectdashboard.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.quickdev.projectdashboard.databinding.FragmentHomeProfileBinding
import com.quickdev.projectdashboard.viewmodels.ProfileViewModel

class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentHomeProfileBinding

    private val viewModel: ProfileViewModel by lazy {
        val activity = requireNotNull(this.activity) {
            "You can only access the viewModel after onActivityCreated()"
        }

        ViewModelProviders
            .of(this, ProfileViewModel.Factory(activity.application))
            .get(ProfileViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentHomeProfileBinding.inflate(inflater)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        return binding.root
    }
}
