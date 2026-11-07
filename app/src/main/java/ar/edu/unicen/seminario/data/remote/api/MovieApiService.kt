package ar.edu.unicen.seminario.data.remote.api

import ar.edu.unicen.seminario.data.remote.dto.MovieDetailDto
import ar.edu.unicen.seminario.data.remote.dto.MovieDto
import ar.edu.unicen.seminario.data.remote.dto.PagedResponseDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieApiService {

    @GET("movie/popular")
    suspend fun getPopularMovies(
        @Query("page") page: Int = 1
    ): PagedResponseDto<MovieDto>

    @GET("movie/{movieId}")
    suspend fun getMovieDetail(
        @Path("movieId") movieId: Int
    ): MovieDetailDto
}
