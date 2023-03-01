package domain.teams.repository

import data.Result
import data.local.AppLocalDataSource
import data.remote.TeamsRemoteDataSource
import data.response.TeamsResponse
import javax.inject.Inject

class TeamsRepository @Inject constructor(
    private val appLocalDataSource: AppLocalDataSource,
    private val remoteDataSource: TeamsRemoteDataSource) {
    suspend fun getTeamRepo(): Result<TeamsResponse?> {
        return remoteDataSource.getDataTeams()
    }

}