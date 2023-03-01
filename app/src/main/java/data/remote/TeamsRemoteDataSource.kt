package data.remote

import data.Result
import data.remote.api.ApiAppService
import data.response.TeamsResponse
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class TeamsRemoteDataSource @Inject constructor(
    private val apiAppService: ApiAppService,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : BaseRemoteDataSource() {

    suspend fun getDataTeams():
            Result<TeamsResponse> {
        return withContext(dispatcher) {
            getResult {
                apiAppService.callListTeams()
            }
        }
    }

}