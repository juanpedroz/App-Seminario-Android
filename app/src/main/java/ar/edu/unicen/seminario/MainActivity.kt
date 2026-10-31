package ar.edu.unicen.seminario

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import ar.edu.unicen.seminario.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private var counter : Int = 0
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //enableEdgeToEdge()

        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root) // tambien puede ser main porque es el id de la raiz

        setContentView(R.layout.activity_main)

        //val textHelloWorld : TextView = findViewById(R.id.hellow_world_text_view)

        //val buttonClickMe : Button = findViewById(R.id.click_me_button)

//        buttonClickMe.setOnClickListener {
//            //counter++
//            textHelloWorld.text = getString(R.string.how_are_you)
//        }

        binding.clickMeButton.setOnClickListener {
            binding.hellowWorldTextView.text = getString(R.string.how_are_you)
        }


//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
//            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
//            insets
//            }
    }
}