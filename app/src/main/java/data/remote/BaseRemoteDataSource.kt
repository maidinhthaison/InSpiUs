package data.remote

import data.Result
import java.io.IOException

abstract class BaseRemoteDataSource {
    //Base class was used to implement calling restful api (if have)
    protected suspend fun <T> getResult(call: suspend () -> T): Result<T> {
        try {
            val response = call()
            return Result.Success(response)
        } catch (e: Exception) {
            if (e is IOException) {
                return Result.NetworkError
            }
            return Result.UnknownError(e)
        }
    }
}