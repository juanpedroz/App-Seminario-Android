package ar.edu.unicen.seminario.data.remote.interceptor

import ar.edu.unicen.seminario.BuildConfig
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class ApiKeyInterceptor @Inject constructor() : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val apiKey = BuildConfig.TMDB_API_KEY
        require(apiKey.isNotBlank()) {
            "Configura TMDB_API_KEY en local.properties"
        }

        val original = chain.request()
        val urlWithApiKey = original.url.newBuilder()
            .addQueryParameter("api_key", apiKey)
            .build()

        val request = original.newBuilder()
            .url(urlWithApiKey)
            .build()

        return chain.proceed(request)
    }
}
