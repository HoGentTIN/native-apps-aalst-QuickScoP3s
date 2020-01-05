package com.quickdev.projectdashboard.ui.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.quickdev.projectdashboard.data.UserHelper

class StartupActivity : AppCompatActivity() {

    private lateinit var userHelper: UserHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        userHelper = UserHelper(this)
        runStartup()
    }

    private fun runStartup() {
        if (!userHelper.isSignedIn) {
            startActivityForResult(Intent(this, AuthActivity::class.java), LOGININ_USER)
            return
        }

        startApp()
    }

    private fun startApp() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == LOGININ_USER && resultCode == Activity.RESULT_OK)
            runStartup()
    }

    companion object {
        private const val LOGININ_USER: Int = 0
    }
}
