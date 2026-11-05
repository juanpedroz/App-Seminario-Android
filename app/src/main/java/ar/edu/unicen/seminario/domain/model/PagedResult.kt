package ar.edu.unicen.seminario.domain.model

data class PagedResult<T>(
    val page: Int,
    val results: List<T>,
    val totalPages: Int,
    val totalResults: Int
)
