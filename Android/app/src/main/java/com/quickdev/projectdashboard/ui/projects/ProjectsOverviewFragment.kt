package com.quickdev.projectdashboard.ui.projects

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.quickdev.projectdashboard.R
import com.quickdev.projectdashboard.databinding.ProjectsFragmentOverviewBinding

class ProjectsOverviewFragment : Fragment() {

    private lateinit var binding: ProjectsFragmentOverviewBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.projects_fragment_overview, container, false)

        return binding.root
    }
}
