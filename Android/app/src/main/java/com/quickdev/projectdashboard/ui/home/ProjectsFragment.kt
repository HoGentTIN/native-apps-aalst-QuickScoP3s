package com.quickdev.projectdashboard.ui.home

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.quickdev.projectdashboard.data.database.getDatabase
import com.quickdev.projectdashboard.databinding.FragmentHomeProjectsBinding
import com.quickdev.projectdashboard.ui.activity.AddProjectActivity
import com.quickdev.projectdashboard.viewmodels.ProjectsViewModel
import com.quickdev.projectdashboard.viewmodels.adapters.ProjectAdapter

class ProjectsFragment : Fragment() {

    companion object {
        private const val ADD_PROJECT = 1
    }

    private lateinit var binding: FragmentHomeProjectsBinding
    private lateinit var projectsAdapter: ProjectAdapter

    private val viewModel: ProjectsViewModel by lazy {
        val activity = requireNotNull(this.activity) {
            "You can only access the viewModel after onActivityCreated()"
        }

        val database = getDatabase(activity)

        ViewModelProviders
            .of(this, ProjectsViewModel.Factory(database))
            .get(ProjectsViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentHomeProjectsBinding.inflate(inflater)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        projectsAdapter = ProjectAdapter { project, projectNameTV, teamNameTV ->
            val projectId = project.id
//
            val extras = FragmentNavigatorExtras(
                projectNameTV to "projectName",
                teamNameTV to "teamName"
            )
//
            val action = ProjectsFragmentDirections.actionFragmentNavProjectsToFragmentNavProjectDetails(projectId, project.name, project.team!!.name)
            findNavController().navigate(action, extras)
        }

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
            if (isLoading != null)
                binding.pullRefreshProjects.isRefreshing = isLoading
        })
    }

    private fun startListeners() {
        binding.btnProjectsAdd.setOnClickListener {
            startActivityForResult(Intent(this.activity, AddProjectActivity::class.java), ADD_PROJECT)
        }

        binding.pullRefreshProjects.setOnRefreshListener {
            binding.viewModel?.refreshData()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()

        binding.btnProjectsAdd.hide()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == ADD_PROJECT && resultCode == Activity.RESULT_OK)
            binding.viewModel?.refreshData()
    }
}
