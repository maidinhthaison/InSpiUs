package data.remote.api

import data.response.TeamsResponse
import data.response.DownloadResponse
import retrofit2.http.GET
import retrofit2.http.Streaming
import retrofit2.http.Url

interface ApiAppService {

    @GET("/teams")
    suspend fun callListTeams(): TeamsResponse

    @Streaming
    @GET
    suspend fun downloadFileWithUrl(@Url fileUrl: String): DownloadResponse
}