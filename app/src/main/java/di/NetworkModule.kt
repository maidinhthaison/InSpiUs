package di

import com.google.gson.FieldNamingPolicy
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import data.remote.api.ApiAppService
import data.remote.api.interceptor.AppInterceptor
import di.qualifier.AuthInterceptorOkHttpClient
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.security.cert.X509Certificate
import java.text.DateFormat
import java.util.concurrent.TimeUnit
import javax.inject.Singleton
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager
import com.jetpack.demo.BuildConfig
@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    private const val CONNECT_TIMEOUT = 30

    @Provides
    fun provideGsonFactory(): GsonConverterFactory = GsonConverterFactory.create(
        GsonBuilder()
            .enableComplexMapKeySerialization()
            .serializeNulls()
            .setDateFormat(DateFormat.LONG)
            .setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE)
            .setPrettyPrinting()
            .create()
    )

    @AuthInterceptorOkHttpClient
    @Provides
    fun provideAuthInterceptorOkHttpClient(
        authInterceptor: AppInterceptor
    ): OkHttpClient {
        return provideHttpClient(authInterceptor)
    }

    private fun provideHttpClient(interceptor: Interceptor): OkHttpClient {
        val clientBuilder = getUnsafeOkHttpClient()
        clientBuilder.connectTimeout(CONNECT_TIMEOUT.toLong(), TimeUnit.SECONDS)
        if (BuildConfig.DEBUG) {
            val logging = HttpLoggingInterceptor()
            logging.level = HttpLoggingInterceptor.Level.BODY
            clientBuilder.addInterceptor(logging)
        }
        clientBuilder.addInterceptor(interceptor)
        return clientBuilder.build()
    }

    private fun getUnsafeOkHttpClient(): OkHttpClient.Builder {
        val x509TrustManager = object: X509TrustManager {
            override fun checkClientTrusted(chain: Array<out X509Certificate>?, authType: String?) {
            }

            override fun checkServerTrusted(chain: Array<out X509Certificate>?, authType: String?) {
            }

            override fun getAcceptedIssuers(): Array<X509Certificate> {
                return arrayOf()
            }
        }

        val trustManagers = arrayOf<TrustManager>(x509TrustManager)
        val sslContext = SSLContext.getInstance("TLS")
        sslContext.init(null, trustManagers, null)

        val builder = OkHttpClient.Builder()
        builder.sslSocketFactory(sslContext.socketFactory, x509TrustManager)
        builder.hostnameVerifier { _, _ -> true }
        return builder
    }

    @Singleton
    @Provides
    fun provideAuthService(
        @AuthInterceptorOkHttpClient client: OkHttpClient,
        gsonConverterFactory: GsonConverterFactory
    ): ApiAppService {
        return Retrofit.Builder().baseUrl(BuildConfig.BASE_API_URL)
            .client(client)
            .addConverterFactory(gsonConverterFactory)
            .build().create(ApiAppService::class.java)
    }
    @Provides
    fun provideCoroutineDispatcher(): CoroutineDispatcher {
        return Dispatchers.IO
    }
}

