package data.remote.api

import data.response.MatchesResponse
import data.response.TeamsResponse
import retrofit2.http.*

interface ApiAppService {

    @GET("/teams")
    suspend fun callListTeams(): TeamsResponse

    @GET("/teams/matches")
    suspend fun callListMatches(): MatchesResponse

    @GET("/teams/{id}/matches")
    suspend fun callGetMatchPerTeam(
        @Path("id") id: String
    ): MatchesResponse

}