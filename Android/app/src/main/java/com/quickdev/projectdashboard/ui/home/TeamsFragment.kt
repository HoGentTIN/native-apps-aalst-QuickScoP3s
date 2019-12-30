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
import com.quickdev.projectdashboard.databinding.FragmentHomeTeamsBinding
import com.quickdev.projectdashboard.models.domain.repositories.TeamRepository
import com.quickdev.projectdashboard.viewmodels.TeamsViewModel
import com.quickdev.projectdashboard.viewmodels.adapters.TeamAdapter
import com.quickdev.projectdashboard.viewmodels.adapters.TeamItemClickListener

class TeamsFragment : Fragment() {

    private lateinit var binding: FragmentHomeTeamsBinding
    private lateinit var teamsAdapter: TeamAdapter

    private val viewModel: TeamsViewModel by lazy {
        val activity = requireNotNull(this.activity) {
            "You can only access the viewModel after onActivityCreated()"
        }

        val database = getDatabase(activity)
        val repo = TeamRepository(database)

        ViewModelProviders
            .of(this, TeamsViewModel.Factory(repo))
            .get(TeamsViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentHomeTeamsBinding.inflate(inflater)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        teamsAdapter = TeamAdapter(TeamItemClickListener { teamId: Int ->

        })

        binding.listProjects.layoutManager = LinearLayoutManager(context)
        binding.listProjects.adapter = teamsAdapter

        registerObservers()
        startListeners()

        binding.btnTeamsAdd.show()

        return binding.root
    }

    private fun registerObservers() {
        binding.viewModel?.teams?.observe(this, Observer { projects ->
            teamsAdapter.setList(projects)
        })

        binding.viewModel?.isLoading?.observe(this, Observer { isLoading: Boolean? ->
            if (isLoading != null)
                binding.pullRefreshProjects.isRefreshing = isLoading
        })
    }

    private fun startListeners() {
        binding.pullRefreshProjects.setOnRefreshListener {
            binding.viewModel?.refreshData()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.btnTeamsAdd.hide()
    }
}
