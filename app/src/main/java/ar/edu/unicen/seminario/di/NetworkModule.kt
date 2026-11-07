package ar.edu.unicen.seminario.di

import android.util.Log
import ar.edu.unicen.seminario.BuildConfig
import ar.edu.unicen.seminario.data.remote.api.MovieApiService
import ar.edu.unicen.seminario.data.remote.interceptor.ApiKeyInterceptor
import ar.edu.unicen.seminario.data.remote.interceptor.LanguageInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    private const val BASE_URL = "https://api.themoviedb.org/3/"
    private const val LOG_TAG = "HttpClient"

    @Provides
    @Singleton
    fun provideLoggingInterceptor(): HttpLoggingInterceptor =
        HttpLoggingInterceptor { message ->
            val safeMessage = message.replace(
                Regex("api_key=[^&\\s]+"),
                "api_key=***"
            )
            Log.d(LOG_TAG, safeMessage)
        }.apply {
            level = if (BuildConfig.DEBUG) {
                HttpLoggingInterceptor.Level.BODY
            } else {
                HttpLoggingInterceptor.Level.NONE
            }
        }

    @Provides
    @Singleton
    fun provideOkHttpClient(
        apiKeyInterceptor: ApiKeyInterceptor,
        languageInterceptor: LanguageInterceptor,
        loggingInterceptor: HttpLoggingInterceptor
    ): OkHttpClient =
        OkHttpClient.Builder()
            .addInterceptor(apiKeyInterceptor)
            .addInterceptor(languageInterceptor)
            .addInterceptor(loggingInterceptor)
            .build()

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    @Provides
    @Singleton
    fun provideMovieApiService(retrofit: Retrofit): MovieApiService =
        retrofit.create(MovieApiService::class.java)
}
