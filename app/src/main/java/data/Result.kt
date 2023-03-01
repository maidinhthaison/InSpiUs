package data

sealed class Result<out T>{
    data class Success<out T>(val data: T) : Result<T>()
    //remote
    object NetworkError : Result<Nothing>()
    data class GenericError(val code: Int, val message: String) : Result<Nothing>()
    data class UnknownError(val exception: Exception) : Result<Nothing>()
    //local
    object  FileNotFoundError: Result<Nothing>()
    object  IOError: Result<Nothing>()

    override fun toString(): String {
        return when (this) {
            is Success<*> -> "Success[data=$data]"
            is GenericError -> "Error[exception=$code + $message]"
            is UnknownError -> "Error exception=$exception"
            else -> "Network Error"
        }
    }
}