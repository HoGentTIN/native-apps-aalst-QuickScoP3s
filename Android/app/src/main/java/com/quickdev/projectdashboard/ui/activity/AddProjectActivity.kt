package com.quickdev.projectdashboard.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.quickdev.projectdashboard.R
import com.quickdev.projectdashboard.databinding.ActivityAddprojectBinding

class AddProjectActivity : AppCompatActivity() {

	private lateinit var binding: ActivityAddprojectBinding

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		binding = DataBindingUtil.setContentView(this, R.layout.activity_addproject)
	}

}