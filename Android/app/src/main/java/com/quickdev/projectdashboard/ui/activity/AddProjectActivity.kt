package com.quickdev.projectdashboard.ui.activity

import android.app.Activity
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.afollestad.vvalidator.form
import com.google.android.material.snackbar.Snackbar
import com.quickdev.projectdashboard.R
import com.quickdev.projectdashboard.data.database.getDatabase
import com.quickdev.projectdashboard.databinding.ActivityAddprojectBinding
import com.quickdev.projectdashboard.models.domain.Team
import com.quickdev.projectdashboard.util.assertions.PhoneAssertion
import com.quickdev.projectdashboard.viewmodels.AddProjectViewModel
import com.quickdev.projectdashboard.viewmodels.adapters.TeamDropdownAdapter

class AddProjectActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddprojectBinding

    private val viewModel: AddProjectViewModel by lazy {
        val database = getDatabase(this)

        ViewModelProviders
            .of(this, AddProjectViewModel.Factory(database))
            .get(AddProjectViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_addproject)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        registerValidation()
        registerListeners()
        startObservers()
    }

    private fun registerValidation() {
        form {
            inputLayout(binding.txtAddprojectName) {
                isNotEmpty().description(R.string.error_empty)
            }
            inputLayout(binding.layoutAddprojectTeam) {
                isNotEmpty().description(R.string.error_empty)
            }
            inputLayout(binding.txtAddprojectContactFirstName) {
                isNotEmpty().description(R.string.error_empty)
            }
            inputLayout(binding.txtAddprojectContactLastName) {
                isNotEmpty().description(R.string.error_empty)
            }
            inputLayout(binding.txtAddprojectContactEmail) {
                isNotEmpty().description(R.string.error_empty)
                isEmail().description(R.string.error_invalid_email)
            }
            inputLayout(binding.txtAddprojectContactPhone) {
                isNotEmpty().description(R.string.error_empty)
                assert(PhoneAssertion()).description(R.string.error_invalid_phonenr)
            }
            submitWith(binding.btnAddproject) {
                binding.viewModel?.createProject()
            }
        }
    }

    private fun registerListeners() {
        binding.selectAddprojectTeam.setOnItemClickListener { _, _, position, _ ->
            val team = binding.selectAddprojectTeam.adapter.getItem(position) as Team
            binding.viewModel?.setSelectedTeamId(team.id)
        }
    }

    private fun startObservers() {
        binding.viewModel?.teams?.observe(this, Observer { teams ->
            val adapter = TeamDropdownAdapter(this, teams)
            binding.selectAddprojectTeam.setAdapter(adapter)
        })

        binding.viewModel?.createResponse?.observe(this, Observer { httpCode: Int? ->
            if (httpCode != null) {
                when (httpCode) {
                    200 -> {
                        setResult(Activity.RESULT_OK)
                        finish()
                    }
                    409 -> Snackbar.make(
                        binding.btnAddproject,
                        R.string.error_projectname_exists,
                        Snackbar.LENGTH_SHORT
                    ).show()
                    else -> Snackbar.make(
                        binding.btnAddproject,
                        R.string.httperror_invalid_data,
                        Snackbar.LENGTH_SHORT
                    ).show()
                }
            }
        })
    }
}
