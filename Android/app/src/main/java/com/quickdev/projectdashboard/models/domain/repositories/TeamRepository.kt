package com.quickdev.projectdashboard.models.domain.repositories

import com.quickdev.projectdashboard.data.database.AppDatabase
import com.quickdev.projectdashboard.data.network.TeamService
import com.quickdev.projectdashboard.models.domain.Team
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
            }
            catch (e: Exception) { }

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
            }
            catch (e: Exception) { }

            val team = teamDao.getById(id)
            team.lead = userDao.getById(team.leadId)
            team.members = team.memberIds.map { memberId -> userDao.getById(memberId) }

            team
        }
    }
}