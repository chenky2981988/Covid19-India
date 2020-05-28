package developer.chirag.covid19.ui.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import developer.chirag.covid19.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var activityBinding: ActivityMainBinding;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(activityBinding.root)

    }
}
