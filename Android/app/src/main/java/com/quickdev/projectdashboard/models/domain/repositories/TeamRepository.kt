package com.quickdev.projectdashboard.models.domain.repositories

import com.quickdev.projectdashboard.App
import com.quickdev.projectdashboard.data.database.AppDatabase
import com.quickdev.projectdashboard.data.network.CompanyService
import com.quickdev.projectdashboard.data.network.TeamService
import com.quickdev.projectdashboard.models.domain.Team
import com.quickdev.projectdashboard.models.domain.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class TeamRepository(database: AppDatabase) {

    private val teamDao = database.teamDao
    private val userDao = database.userDao

    suspend fun getTeams(): List<Team> {
        return withContext(Dispatchers.IO) {
            val call = TeamService.HTTP.getAll()
            try {
                val teams = call.await()
                teamDao.clear()
                teamDao.insertAll(*teams.toTypedArray())
            } catch (e: Exception) {
                e.printStackTrace()
            }

            val teams = teamDao.getAll()
            teams.forEach { team -> // Fill all objects
                team.lead = userDao.getById(team.leadId)
                team.members = team.memberIds.map { memberId -> userDao.getById(memberId) }
            }

            teams
        }
    }

    suspend fun getById(id: Int): Team {
        return withContext(Dispatchers.IO) {
            val call = TeamService.HTTP.getById(id)
            try {
                val team = call.await()
                teamDao.update(team)
            } catch (e: Exception) {
                e.printStackTrace()
            }

            val team = teamDao.getById(id)
            team.lead = userDao.getById(team.leadId)
            team.members = team.memberIds.map { memberId -> userDao.getById(memberId) }

            team
        }
    }

    suspend fun getUsersFromCompany(): List<User> {
        val userHelper = App.getUserHelper()
        if (!userHelper.isSignedIn)
            return listOf()

        val user = userHelper.getSignedInUser()!!
        val companyId = user.companyId!!

        return withContext(Dispatchers.IO) {
            var list = listOf<User>()
            val call = CompanyService.HTTP.getUsersFromCompany(companyId)
            try {
                list = call.await()
            } catch (e: Exception) {
                e.printStackTrace()
            }

            list
        }
    }
}
