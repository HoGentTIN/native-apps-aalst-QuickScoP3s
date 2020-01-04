package com.quickdev.projectdashboard.ui.auth

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import com.afollestad.vvalidator.form
import com.google.android.material.snackbar.Snackbar
import com.quickdev.projectdashboard.R
import com.quickdev.projectdashboard.databinding.FragmentAuthLoginBinding
import com.quickdev.projectdashboard.viewmodels.LoginViewModel

class LoginFragment : Fragment() {

    private lateinit var binding: FragmentAuthLoginBinding

    private val viewModel: LoginViewModel by lazy {
        val activity = requireNotNull(this.activity) {
            "You can only access the viewModel after onActivityCreated()"
        }

        ViewModelProviders
                .of(this, LoginViewModel.Factory(activity.application))
                .get(LoginViewModel::class.java)
    }

    /*****  EINDE VALIDATIE  *****/

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentAuthLoginBinding.inflate(inflater)

        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        startObservers()
        registerValidation()
        registerListeners()

        return binding.root
    }

    private fun registerListeners() {
        binding.btnLoginRegister.setOnClickListener { view: View ->
            view.findNavController().navigate(R.id.action_fragment_login_to_fragment_register)
        }
    }

    private fun registerValidation() {
        form {
            inputLayout(binding.txtLoginEmail) {
                isNotEmpty().description(R.string.error_empty)
                isEmail().description(R.string.error_invalid_email)
            }
            inputLayout(binding.txtLoginPassword) {
                isNotEmpty().description(R.string.error_empty)
            }
            submitWith(binding.btnLogin) {
                binding.viewModel?.loginUser()
            }
        }
    }

    private fun startObservers() {
        binding.viewModel?.loginResponse?.observe(this, Observer { httpCode: Int? ->
            if (httpCode != null) {
                when (httpCode) {
                    200 -> {
                        activity?.setResult(Activity.RESULT_OK)
                        activity?.finish()
                    }
                    401 -> Snackbar.make(
                        binding.btnLogin,
                        R.string.error_login_failed,
                        Snackbar.LENGTH_SHORT
                    ).show()
                    504 -> Snackbar.make(
                        binding.btnLogin,
                        R.string.httperror_504,
                        Snackbar.LENGTH_SHORT
                    ).show()
                    else -> Snackbar.make(
                        binding.btnLogin,
                        R.string.httperror_400,
                        Snackbar.LENGTH_SHORT
                    ).show()
                }
            }
        })
    }
}
