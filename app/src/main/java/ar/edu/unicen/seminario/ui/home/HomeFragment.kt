package ar.edu.unicen.seminario.ui.home

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ar.edu.unicen.seminario.R
import ar.edu.unicen.seminario.databinding.FragmentHomeBinding
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home) {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val viewModel: HomeViewModel by viewModels()

    private val movieAdapter = MovieAdapter { movie ->
        (requireActivity() as? OnMovieClickListener)?.onMovieClick(movie.id)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentHomeBinding.bind(view)

        setupRecyclerView()
        setupListeners()
        observeUiState()
    }

    private fun setupRecyclerView() {
        binding.moviesRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = movieAdapter
            setHasFixedSize(true)
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    if (dy <= 0) return
                    val layoutManager = recyclerView.layoutManager as? LinearLayoutManager ?: return
                    val lastVisibleItem = layoutManager.findLastVisibleItemPosition()
                    val itemCount = layoutManager.itemCount
                    if (lastVisibleItem >= itemCount - 3) {
                        viewModel.loadMore()
                    }
                }
            })
        }
    }

    private fun setupListeners() {
        binding.swipeRefresh.setOnRefreshListener {
            viewModel.refresh()
        }
        binding.retryButton.setOnClickListener {
            viewModel.retry()
        }
    }

    private fun observeUiState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.uiState.collect { state ->
                        render(state)
                    }
                }
                launch {
                    viewModel.errorEvents.collect { message ->
                        Snackbar.make(binding.root, message, Snackbar.LENGTH_LONG).show()
                    }
                }
            }
        }
    }

    private fun render(state: HomeUiState) {
        binding.swipeRefresh.isRefreshing = state.isRefreshing
        binding.progressBar.isVisible = state.isLoading
        binding.paginationProgressBar.isVisible = state.isLoadingMore
        binding.errorContainer.isVisible = state.errorMessage != null && state.movies.isEmpty()
        binding.errorText.text = state.errorMessage ?: getString(R.string.error_loading_movies)
        binding.emptyText.isVisible = !state.isLoading &&
            state.movies.isEmpty() &&
            state.errorMessage == null
        binding.moviesRecyclerView.isVisible = state.movies.isNotEmpty()
        movieAdapter.submitList(state.movies)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    interface OnMovieClickListener {
        fun onMovieClick(movieId: Int)
    }
}
