package SIT305.a5_1c

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import SIT305.a5_1c.ui.theme._5_1CTheme
import StoryApp
import android.os.Build
import androidx.annotation.RequiresApi

/**
 * I have used JetPack Compose instead of the Recycler/Fragment pattern as it is more modern and conventional, suits
 * my programming style far more and is more robust / easier to extend later in semester. I have also used the experimental Material3 components in the app
 * as they have the best support for Date functionality. I have also used a NavController to handle navigation state.
 */

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            _5_1CTheme {
                StoryApp()
            }
        }
    }
}


