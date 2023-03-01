package data.remote.api.interceptor

import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class AppInterceptor @Inject constructor() : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()
        val requestBuilder = original.newBuilder()
        requestBuilder.header("cache-control", "no-cache")
        requestBuilder.header("accept", "application/json")
        return chain.proceed(requestBuilder.build())
    }
}