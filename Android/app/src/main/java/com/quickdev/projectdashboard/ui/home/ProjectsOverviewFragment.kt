package com.quickdev.projectdashboard.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.quickdev.projectdashboard.data.database.getDatabase
import com.quickdev.projectdashboard.databinding.FragmentProjectsOverviewBinding
import com.quickdev.projectdashboard.models.domain.repositories.ProjectRepository
import com.quickdev.projectdashboard.viewmodels.ProjectsOverviewViewModel

class ProjectsOverviewFragment : Fragment() {

    private lateinit var binding: FragmentProjectsOverviewBinding

    private val viewModel: ProjectsOverviewViewModel by lazy {
        val activity = requireNotNull(this.activity) {
            "You can only access the viewModel after onActivityCreated()"
        }

        val database = getDatabase(activity)
        val repo = ProjectRepository(database.projectDao)

        ViewModelProviders
            .of(this, ProjectsOverviewViewModel.Factory(repo))
            .get(ProjectsOverviewViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentProjectsOverviewBinding.inflate(inflater)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        return binding.root
    }

    private fun registerObservers() {
        binding.viewModel?.projects?.observe(this, Observer { projects ->

        })
    }
}
