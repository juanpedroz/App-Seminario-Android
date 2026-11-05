package ar.edu.unicen.seminario.data.repository

import ar.edu.unicen.seminario.data.mapper.MovieMapper
import ar.edu.unicen.seminario.data.remote.api.MovieApiService
import ar.edu.unicen.seminario.domain.model.Movie
import ar.edu.unicen.seminario.domain.model.MovieDetail
import ar.edu.unicen.seminario.domain.model.PagedResult
import ar.edu.unicen.seminario.domain.repository.MovieRepository
import kotlinx.coroutines.CancellationException
import javax.inject.Inject

class MovieRepositoryImpl @Inject constructor(
    private val api: MovieApiService,
    private val mapper: MovieMapper
) : MovieRepository {

    override suspend fun getPopularMovies(page: Int): Result<PagedResult<Movie>> =
        safeApiCall {
            mapper.toDomain(api.getPopularMovies(page = page))
        }

    override suspend fun getMovieDetail(movieId: Int): Result<MovieDetail> =
        safeApiCall {
            mapper.toDomain(api.getMovieDetail(movieId = movieId))
        }

    private suspend fun <T> safeApiCall(block: suspend () -> T): Result<T> =
        try {
            Result.success(block())
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Result.failure(e)
        }
}
