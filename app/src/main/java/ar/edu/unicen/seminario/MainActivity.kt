package ar.edu.unicen.seminario

import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import ar.edu.unicen.seminario.databinding.ActivityMainBinding
import kotlin.math.log

class MainActivity : AppCompatActivity() {

    private var counter: Int = 0
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //enableEdgeToEdge()

        Log.i("MainActivity", "onCreate")

        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root) // tambien puede ser main porque es el id de la raiz

        //setContentView(R.layout.activity_main)

        //val textHelloWorld : TextView = findViewById(R.id.hellow_world_text_view)

        //val buttonClickMe : Button = findViewById(R.id.click_me_button)

//        buttonClickMe.setOnClickListener {
//            //counter++
//            textHelloWorld.text = getString(R.string.how_are_you)
//        }

        binding.clickMeButton.setOnClickListener {
            counter++
            binding.hellowWorldTextView.text = getString(R.string.how_are_you)
            binding.counterTextView.text = counter.toString()
        }


//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
//            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
//            insets
//            }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        Log.i("MainActivity", "onSaveInstanceState")

        outState.putInt("counter", counter)
    }

    override fun onRestoreInstanceState(
        savedInstanceState: Bundle?,
        persistentState: PersistableBundle?
    ) {
        super.onRestoreInstanceState(savedInstanceState, persistentState)
        Log.i("MainActivity", "onRestoreInstanceState")
        counter = savedInstanceState.getInt("counter")
        binding.counterTextView.text = counter.toString()

    }

    override fun onStart() {
        super.onStart()

        Log.i("MainActivity", "onStar")
    }

    override fun onResume() {
        super.onResume()

        Log.i("MainActivity", "onResume")
    }

    override fun onStop() {
        super.onStop()

        Log.i("MainActivity", "onStop")
    }

    override fun onPause() {
        super.onPause()

        Log.i("MainActivity", "onPause")
    }

    override fun onDestroy() {
        super.onDestroy()

        Log.i("MainActivity", "onDestroy")
    }
}