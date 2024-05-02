package SOT305.a5_1c_2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import SOT305.a5_1c_2.ui.theme._5_1C_2Theme
import androidx.lifecycle.lifecycleScope

/**
 * Very similar to TaskManager app but using different Nav pattern where we pass callbacks to screens.
 *
 * This is probably a poor way to handle Auth state, but it's a good way to learn about Compose and coroutine scoping / lifecycle.
 *
 * Also lol @ typo in namespace
 */

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val userDao = getDatabase(applicationContext).userDao()
        val userRepo = UserRepo(userDao, lifecycleScope)

        setContent {
            _5_1C_2Theme {
                UserManagerApp(userRepo)
            }
        }
    }
}

