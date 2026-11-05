package ar.edu.unicen.seminario

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import ar.edu.unicen.seminario.databinding.ActivityMainBinding
import ar.edu.unicen.seminario.ui.home.HomeFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), HomeFragment.OnMovieClickListener {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, HomeFragment())
                .commit()
        }
    }

    override fun onMovieClick(movieId: Int) {
        Toast.makeText(
            this,
            getString(R.string.movie_id_toast, movieId),
            Toast.LENGTH_SHORT
        ).show()
    }
}
