package data.remote

import data.Result
import data.remote.api.ApiAppService
import data.response.MatchesResponse
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class MatchesRemoteDataSource @Inject constructor(
    private val apiAppService: ApiAppService,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : BaseRemoteDataSource() {

    suspend fun getDataMatches():
            Result<MatchesResponse> {
        return withContext(dispatcher) {
            getResult {
                apiAppService.callListMatches()
            }
        }
    }

    suspend fun getDataMatchesPerTeam(id : String):
            Result<MatchesResponse> {
        return withContext(dispatcher) {
            getResult {
                apiAppService.callGetMatchPerTeam(id)
            }
        }
    }
}