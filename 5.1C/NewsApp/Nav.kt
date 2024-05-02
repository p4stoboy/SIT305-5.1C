import SIT305.a5_1c.MainScreen
import SIT305.a5_1c.StoryScreen
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun StoryApp() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "main") {
        composable("main") {
            MainScreen(navController)
        }
        composable(
            route = "story/{storyId}",
            arguments = listOf(
                navArgument("storyId") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val storyId = backStackEntry.arguments?.getInt("storyId") ?: 0
            StoryScreen(navController, storyId)
        }
    }
}