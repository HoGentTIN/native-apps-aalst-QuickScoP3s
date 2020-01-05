package com.quickdev.projectdashboard.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.quickdev.projectdashboard.App
import com.quickdev.projectdashboard.databinding.FragmentHomeProfileBinding
import com.quickdev.projectdashboard.ui.activity.StartupActivity
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

        binding.viewProfileSignout.setOnClickListener {
            App.getUserHelper().signOut()
            startActivity(Intent(requireContext(), StartupActivity::class.java))
            activity!!.finish()
        }

        return binding.root
    }
}
