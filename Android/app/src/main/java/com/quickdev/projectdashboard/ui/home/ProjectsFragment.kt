package com.quickdev.projectdashboard.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.quickdev.projectdashboard.data.database.getDatabase
import com.quickdev.projectdashboard.databinding.FragmentHomeProjectsBinding
import com.quickdev.projectdashboard.models.domain.repositories.ProjectRepository
import com.quickdev.projectdashboard.viewmodels.ProjectsViewModel
import com.quickdev.projectdashboard.viewmodels.adapters.ProjectAdapter
import com.quickdev.projectdashboard.viewmodels.adapters.ProjectItemClickListener

class ProjectsFragment : Fragment() {

    private lateinit var binding: FragmentHomeProjectsBinding
    private lateinit var projectsAdapter: ProjectAdapter

    private val viewModel: ProjectsViewModel by lazy {
        val activity = requireNotNull(this.activity) {
            "You can only access the viewModel after onActivityCreated()"
        }

        val database = getDatabase(activity)
        val repo = ProjectRepository(database)

        ViewModelProviders
            .of(this, ProjectsViewModel.Factory(repo))
            .get(ProjectsViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentHomeProjectsBinding.inflate(inflater)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        projectsAdapter = ProjectAdapter(ProjectItemClickListener { projectId: Int ->

        })

        binding.listProjects.layoutManager = LinearLayoutManager(context)
        binding.listProjects.adapter = projectsAdapter

        registerObservers()
        startListeners()

        binding.btnProjectsAdd.show()

        return binding.root
    }

    private fun registerObservers() {
        binding.viewModel?.projects?.observe(this, Observer { projects ->
            projectsAdapter.setList(projects)
        })

        binding.viewModel?.isLoading?.observe(this, Observer { isLoading: Boolean? ->
            if (isLoading != null && !isLoading)
                binding.pullRefreshProjects.isRefreshing = false
        })
    }

    private fun startListeners() {
        binding.pullRefreshProjects.setOnRefreshListener {
            binding.viewModel?.refreshData()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()

        binding.btnProjectsAdd.hide()
    }
}
