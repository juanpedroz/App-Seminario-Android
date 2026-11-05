package ar.edu.unicen.seminario.ui.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ar.edu.unicen.seminario.domain.model.MovieDetail
import ar.edu.unicen.seminario.domain.repository.MovieRepository
import ar.edu.unicen.seminario.ui.common.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val repository: MovieRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState<MovieDetail>>(UiState.Loading)
    val uiState: StateFlow<UiState<MovieDetail>> = _uiState.asStateFlow()

    private var loadedMovieId: Int? = null

    fun loadMovieDetail(movieId: Int) {
        if (loadedMovieId == movieId && _uiState.value is UiState.Success) {
            return
        }

        loadedMovieId = movieId

        viewModelScope.launch {
            _uiState.value = UiState.Loading
            repository.getMovieDetail(movieId)
                .onSuccess { _uiState.value = UiState.Success(it) }
                .onFailure { _uiState.value = UiState.Error(it.message) }
        }
    }

    fun retry() {
        loadedMovieId?.let { movieId ->
            loadedMovieId = null
            loadMovieDetail(movieId)
        }
    }
}
