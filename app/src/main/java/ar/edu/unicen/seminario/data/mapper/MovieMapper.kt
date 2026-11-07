package ar.edu.unicen.seminario.data.mapper

import ar.edu.unicen.seminario.data.remote.dto.GenreDto
import ar.edu.unicen.seminario.data.remote.dto.MovieDetailDto
import ar.edu.unicen.seminario.data.remote.dto.MovieDto
import ar.edu.unicen.seminario.data.remote.dto.PagedResponseDto
import ar.edu.unicen.seminario.domain.model.Genre
import ar.edu.unicen.seminario.domain.model.Movie
import ar.edu.unicen.seminario.domain.model.MovieDetail
import ar.edu.unicen.seminario.domain.model.PagedResult
import javax.inject.Inject

class MovieMapper @Inject constructor() {

    fun toDomain(dto: MovieDto): Movie = Movie(
        id = dto.id,
        title = dto.title,
        overview = dto.overview.orNullIfBlank(),
        posterUrl = buildPosterUrl(dto.posterPath),
        backdropUrl = buildBackdropUrl(dto.backdropPath),
        releaseDate = dto.releaseDate.orNullIfBlank(),
        voteAverage = dto.voteAverage,
        voteCount = dto.voteCount,
        popularity = dto.popularity,
        originalLanguage = dto.originalLanguage.orNullIfBlank(),
        genreIds = dto.genreIds.orEmpty()
    )

    fun toDomain(dto: MovieDetailDto): MovieDetail = MovieDetail(
        id = dto.id,
        title = dto.title,
        overview = dto.overview.orNullIfBlank(),
        posterUrl = buildPosterUrl(dto.posterPath),
        backdropUrl = buildBackdropUrl(dto.backdropPath),
        releaseDate = dto.releaseDate.orNullIfBlank(),
        runtime = dto.runtime,
        voteAverage = dto.voteAverage,
        voteCount = dto.voteCount,
        status = dto.status.orNullIfBlank(),
        tagline = dto.tagline.orNullIfBlank(),
        genres = dto.genres.orEmpty().map { it.toDomain() }
    )

    fun toDomain(dto: PagedResponseDto<MovieDto>): PagedResult<Movie> = PagedResult(
        page = dto.page,
        results = dto.results.map { toDomain(it) },
        totalPages = dto.totalPages,
        totalResults = dto.totalResults
    )

    private fun GenreDto.toDomain(): Genre = Genre(
        id = id,
        name = name
    )

    private fun buildPosterUrl(path: String?): String? =
        path.orNullIfBlank()?.let { "$POSTER_BASE_URL$it" }

    private fun buildBackdropUrl(path: String?): String? =
        path.orNullIfBlank()?.let { "$BACKDROP_BASE_URL$it" }

    // TMDB manda "" en vez de null cuando falta un dato
    private fun String?.orNullIfBlank(): String? = this?.takeIf { it.isNotBlank() }

    private companion object {
        const val POSTER_BASE_URL = "https://image.tmdb.org/t/p/w500"
        const val BACKDROP_BASE_URL = "https://image.tmdb.org/t/p/w780"
    }
}
