package data.local.db.repository

import androidx.annotation.WorkerThread
import data.local.db.dao.TeamDao
import data.local.db.dao.TeamItem
import kotlinx.coroutines.flow.Flow

class TeamTblRepository (private val teamDao: TeamDao) {

    val allTeams: Flow<MutableList<TeamItem>> = teamDao.getAllTeams()

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert(teamItem: TeamItem){
        teamDao.insert(teamItem)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun delete(teamItem: TeamItem){
        teamDao.delete(teamItem)
    }

}
