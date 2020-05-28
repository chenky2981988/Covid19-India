package developer.chirag.covid19.ui.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import developer.chirag.covid19.R
import developer.chirag.covid19.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var activityBinding: ActivityMainBinding;
    private var backPressTime = 0L
    private val backPressSnackbar by lazy {
        Snackbar.make(activityBinding.root, R.string.back_message, Snackbar.LENGTH_SHORT)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(activityBinding.root)

    }


    override fun onBackPressed() {

        if(backPressTime + 2000 > System.currentTimeMillis()) {
            backPressSnackbar.dismiss()
            super.onBackPressed()
            return
        } else{
            backPressSnackbar.show()
        }
        backPressTime = System.currentTimeMillis()
    }
}
