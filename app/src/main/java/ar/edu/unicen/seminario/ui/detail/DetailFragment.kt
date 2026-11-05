package ar.edu.unicen.seminario.ui.detail

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import ar.edu.unicen.seminario.R
import ar.edu.unicen.seminario.databinding.FragmentDetailBinding
import ar.edu.unicen.seminario.domain.model.MovieDetail
import ar.edu.unicen.seminario.ui.common.UiState
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DetailFragment : Fragment(R.layout.fragment_detail) {

    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!

    private val viewModel: DetailViewModel by viewModels()

    private val movieId: Int by lazy {
        requireArguments().getInt(ARG_MOVIE_ID)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentDetailBinding.bind(view)

        binding.detailToolbar.setNavigationOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }
        binding.detailToolbar.navigationContentDescription = getString(R.string.back)
        binding.detailRetryButton.setOnClickListener {
            viewModel.retry()
        }

        observeUiState()
        viewModel.loadMovieDetail(movieId)
    }

    private fun observeUiState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    render(state)
                }
            }
        }
    }

    private fun render(state: UiState<MovieDetail>) {
        when (state) {
            is UiState.Loading -> {
                binding.detailProgressBar.isVisible = true
                binding.detailContent.isVisible = false
                binding.detailErrorContainer.isVisible = false
            }

            is UiState.Success -> {
                binding.detailProgressBar.isVisible = false
                binding.detailErrorContainer.isVisible = false
                binding.detailContent.isVisible = true
                bindDetail(state.data)
            }

            is UiState.Error -> {
                binding.detailProgressBar.isVisible = false
                binding.detailContent.isVisible = false
                binding.detailErrorContainer.isVisible = true
                binding.detailErrorText.text = state.message
                    ?: getString(R.string.error_loading_detail)
            }
        }
    }

    private fun bindDetail(detail: MovieDetail) {
        val context = binding.root.context

        binding.detailTitle.text = detail.title
        binding.detailReleaseDate.text = context.getString(
            R.string.movie_release_date_format,
            detail.releaseDate ?: context.getString(R.string.movie_release_date_unknown)
        )
        binding.detailRating.text = context.getString(
            R.string.movie_rating_format,
            detail.voteAverage
        )
        binding.detailRuntime.text = detail.runtime?.let {
            context.getString(R.string.movie_runtime_format, it)
        } ?: context.getString(R.string.movie_runtime_unknown)
        binding.detailGenres.text = if (detail.genres.isEmpty()) {
            context.getString(R.string.no_genres)
        } else {
            context.getString(
                R.string.genres_format,
                detail.genres.joinToString { it.name }
            )
        }
        binding.detailOverview.text = detail.overview
            ?: context.getString(R.string.no_overview)

        binding.detailBackdrop.contentDescription = context.getString(
            R.string.detail_backdrop_description,
            detail.title
        )
        binding.detailPoster.contentDescription = context.getString(
            R.string.movie_poster_description,
            detail.title
        )

        Glide.with(binding.detailBackdrop)
            .load(detail.backdropUrl)
            .placeholder(R.drawable.ic_movie)
            .error(R.drawable.ic_movie)
            .into(binding.detailBackdrop)

        Glide.with(binding.detailPoster)
            .load(detail.posterUrl)
            .placeholder(R.drawable.ic_movie)
            .error(R.drawable.ic_movie)
            .into(binding.detailPoster)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val ARG_MOVIE_ID = "movieId"

        fun newInstance(movieId: Int): DetailFragment = DetailFragment().apply {
            arguments = bundleOf(ARG_MOVIE_ID to movieId)
        }
    }
}
