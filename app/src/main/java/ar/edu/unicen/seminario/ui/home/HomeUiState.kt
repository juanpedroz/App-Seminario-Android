package ar.edu.unicen.seminario.ui.home

import ar.edu.unicen.seminario.domain.model.Movie

data class HomeUiState(
    val movies: List<Movie> = emptyList(),
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val isLoadingMore: Boolean = false,
    val errorMessage: String? = null,
    val endReached: Boolean = false
)
