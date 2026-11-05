package ar.edu.unicen.seminario.domain.repository

import ar.edu.unicen.seminario.domain.model.Movie
import ar.edu.unicen.seminario.domain.model.MovieDetail
import ar.edu.unicen.seminario.domain.model.PagedResult

interface MovieRepository {

    suspend fun getPopularMovies(page: Int = 1): Result<PagedResult<Movie>>

    suspend fun getMovieDetail(movieId: Int): Result<MovieDetail>
}
