package domain.matches.repository

import data.Result
import data.local.AppLocalDataSource
import data.remote.MatchesRemoteDataSource
import data.response.MatchesResponse
import javax.inject.Inject

class MatchRepository @Inject constructor(
    private val appLocalDataSource: AppLocalDataSource,
    private val remoteDataSource: MatchesRemoteDataSource
) {
    suspend fun getMatchRepo(): Result<MatchesResponse?> {
        return remoteDataSource.getDataMatches()
    }

    suspend fun getMatchPerTeamRepo(id: String): Result<MatchesResponse?> {
        return remoteDataSource.getDataMatchesPerTeam(id)
    }

}