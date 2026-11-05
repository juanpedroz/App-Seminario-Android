package ar.edu.unicen.seminario.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ar.edu.unicen.seminario.domain.repository.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: MovieRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState(isLoading = true))
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    private val _errorEvents = MutableSharedFlow<String>()
    val errorEvents: SharedFlow<String> = _errorEvents.asSharedFlow()

    private var currentPage = 0
    private var totalPages = 1
    private var loadJob: Job? = null

    init {
        loadPage(page = 1, refresh = true)
    }

    fun refresh() {
        loadPage(page = 1, refresh = true)
    }

    fun loadMore() {
        if (loadJob?.isActive == true || currentPage >= totalPages) {
            return
        }
        loadPage(page = currentPage + 1, refresh = false)
    }

    fun retry() {
        if (_uiState.value.movies.isEmpty()) {
            refresh()
        } else {
            loadMore()
        }
    }

    private fun loadPage(page: Int, refresh: Boolean) {
        loadJob?.cancel()
        loadJob = viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isLoading = refresh && it.movies.isEmpty(),
                    isRefreshing = refresh && it.movies.isNotEmpty(),
                    isLoadingMore = !refresh,
                    errorMessage = null
                )
            }

            repository.getPopularMovies(page)
                .onSuccess { result ->
                    currentPage = result.page
                    totalPages = result.totalPages
                    _uiState.update { state ->
                        val updatedMovies = if (refresh) {
                            result.results
                        } else {
                            state.movies + result.results
                        }
                        state.copy(
                            movies = updatedMovies,
                            isLoading = false,
                            isRefreshing = false,
                            isLoadingMore = false,
                            errorMessage = null,
                            endReached = currentPage >= totalPages
                        )
                    }
                }
                .onFailure { error ->
                    val message = error.message ?: "Error al cargar películas"
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            isRefreshing = false,
                            isLoadingMore = false,
                            errorMessage = if (it.movies.isEmpty()) message else null
                        )
                    }
                    if (_uiState.value.movies.isNotEmpty()) {
                        _errorEvents.emit(message)
                    }
                }
        }
    }
}
