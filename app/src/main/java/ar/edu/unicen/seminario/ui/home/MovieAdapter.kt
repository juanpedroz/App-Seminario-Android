package ar.edu.unicen.seminario.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ar.edu.unicen.seminario.R
import ar.edu.unicen.seminario.databinding.ItemMovieBinding
import ar.edu.unicen.seminario.domain.model.Movie
import com.bumptech.glide.Glide

class MovieAdapter(
    private val onMovieClick: (Movie) -> Unit
) : ListAdapter<Movie, MovieAdapter.MovieViewHolder>(MovieDiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val binding = ItemMovieBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return MovieViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class MovieViewHolder(
        private val binding: ItemMovieBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(movie: Movie) {
            val context = binding.root.context

            binding.movieTitle.text = movie.title
            binding.movieReleaseDate.text = context.getString(
                R.string.movie_release_date_format,
                movie.releaseDate ?: context.getString(R.string.movie_release_date_unknown)
            )
            binding.movieRating.text = context.getString(
                R.string.movie_rating_format,
                movie.voteAverage
            )
            binding.moviePoster.contentDescription = context.getString(
                R.string.movie_poster_description,
                movie.title
            )

            Glide.with(binding.moviePoster)
                .load(movie.posterUrl)
                .placeholder(R.drawable.ic_movie)
                .error(R.drawable.ic_movie)
                .into(binding.moviePoster)

            binding.root.setOnClickListener { onMovieClick(movie) }
        }
    }

    private object MovieDiffCallback : DiffUtil.ItemCallback<Movie>() {
        override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean =
            oldItem == newItem
    }
}
