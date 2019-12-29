package com.quickdev.projectdashboard.ui.auth

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.afollestad.vvalidator.form
import com.google.android.material.snackbar.Snackbar
import com.quickdev.projectdashboard.R
import com.quickdev.projectdashboard.databinding.FragmentAuthRegisterBinding
import com.quickdev.projectdashboard.util.assertions.CompareToAssertion
import com.quickdev.projectdashboard.util.assertions.PhoneAssertion
import com.quickdev.projectdashboard.viewmodels.RegisterViewModel

class RegisterFragment : Fragment() {

    private lateinit var binding: FragmentAuthRegisterBinding

    private val viewModel: RegisterViewModel by lazy {
        val activity = requireNotNull(this.activity) {
            "You can only access the viewModel after onActivityCreated()"
        }

        ViewModelProviders
                .of(activity, RegisterViewModel.Factory(activity.application))
                .get(RegisterViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentAuthRegisterBinding.inflate(inflater)

        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        registerListeners()
        startObservers()
        registerValidation()

        return binding.root
    }

    private fun registerValidation() {
        form {
            inputLayout(binding.txtRegisterFirstName) {
                isNotEmpty().description(R.string.error_empty)
            }
            inputLayout(binding.txtRegisterLastName) {
                isNotEmpty().description(R.string.error_empty)
            }
            inputLayout(binding.txtRegisterEmail) {
                isNotEmpty().description(R.string.error_empty)
                isEmail().description(R.string.error_invalid_email)
            }
            inputLayout(binding.txtRegisterPhone) {
                isNotEmpty().description(R.string.error_empty)
                assert(PhoneAssertion()).description(R.string.error_invalid_phonenr)
            }
            inputLayout(binding.txtRegisterPassword) {
                isNotEmpty().description(R.string.error_empty)
                length().atLeast(6).description(R.string.error_invalid_password)
            }
            inputLayout(binding.txtRegisterPasswordConfirm) {
                isNotEmpty().description(R.string.error_empty)
                assert(CompareToAssertion(binding.txtRegisterPassword)).description(R.string.error_invalid_passwordConfirm)
            }
            submitWith(binding.btnRegister) {
                binding.viewModel?.registerUser()
            }
        }
    }

    private fun startObservers() {
        binding.viewModel?.registerResponse?.observe(this, Observer { httpCode: Int? ->
            if (httpCode != null) {
                when (httpCode) {
                    200 -> {
                        activity?.setResult(Activity.RESULT_OK)
                        activity?.finish()
                    }
                    400 -> Snackbar.make(
                        binding.root,
                        R.string.error_registration_failed,
                        Snackbar.LENGTH_SHORT
                    ).show()
                    else -> Snackbar.make(
                        binding.root,
                        R.string.httperror_400,
                        Snackbar.LENGTH_SHORT
                    ).show()
                }
            }
        })
    }

    private fun registerListeners() {
        binding.imgRegisterProfile.setOnClickListener { pickUserPicture() }
        binding.txtRegisterCompany.setOnClickListener { view: View ->
            //view.findNavController().navigate(R.id.action_fragment_login_to_fragment_register)
        }
        binding.btnRegistrerenClearPicture.setOnClickListener {
            binding.viewModel?.clearProfilePicture()
        }
    }

    private fun pickUserPicture() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        startActivityForResult(intent, PICK_PHOTO_FOR_AVATAR)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_PHOTO_FOR_AVATAR && resultCode == Activity.RESULT_OK) {
            if (data != null && data.data != null)
                binding.viewModel?.setProfilePicture(data.data!!)
        }
    }

    companion object {
        private const val PICK_PHOTO_FOR_AVATAR: Int = 1
    }
}
