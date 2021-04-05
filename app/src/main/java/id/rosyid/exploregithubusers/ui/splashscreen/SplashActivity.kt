package id.rosyid.exploregithubusers.ui.splashscreen

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import id.rosyid.exploregithubusers.databinding.ActivitySplashBinding
import id.rosyid.exploregithubusers.ui.explore.ExploreActivity
import id.rosyid.exploregithubusers.utils.Fullscreen
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {

    private val timeout = 3000L
    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val intent = Intent(this, ExploreActivity::class.java)
        lifecycleScope.launch(Dispatchers.IO) {
            delay(timeout)
            withContext(Dispatchers.Main) {
                startActivity(intent)
                finish()
            }
        }
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus) Fullscreen.apply(window)
    }
}
