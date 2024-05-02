package SOT305.a5_1c_2

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import java.util.regex.Pattern

/**
 * Experimenting with new pattern here, instead of passing navController to Screens we are passing callbacks that execute
 * navigation actions. This decouples the navigation logic from the screens but also makes it less clear what logic is
 * being executed when callbacks are fired in a screen. Not sure which pattern I prefer yet.
 */

// regex to get video ID for YoutubePlayer library
fun getVideoIdFromUrl(youtubeUrl: String): String? {
    val pattern = Pattern.compile(
        "(?:https?:\\/\\/)?(?:www\\.)?(?:youtube\\.com\\/(?:[^\\/\\n\\s]+\\/\\S*\\/|(?:v|e(?:mbed)?)\\/|\\S*?[?&]v=)|youtu\\.be\\/)([a-zA-Z0-9_-]{11})"
    )
    val matcher = pattern.matcher(youtubeUrl)

    if (matcher.find()) {
        return matcher.group(1)
    }
    return null
}


@Composable
fun UserManagerApp(userRepo: UserRepo) {
    val navController = rememberNavController()
    val userViewModel = UserViewModel(userRepo)

    NavHost(navController = navController, startDestination = "login") {
        composable("login") {
            LoginScreen(
                navController = navController,
                userViewModel = userViewModel,
                userRepo = userRepo,
                onLoginSuccess = {
                    navController.navigate("home") {
                        popUpTo("login") { inclusive = true }
                    }
                }
            )
        }
        composable("signup") {
            SignupScreen(
                userRepo = userRepo,
                userViewModel = userViewModel,
                onBackButton = { navController.popBackStack() },
                onSignupSuccess = {
                    navController.navigate("home") {
                        popUpTo("signup") { inclusive = true }
                    }
                }
            )
        }
        composable("home") {
            HomeScreen(
                userViewModel = userViewModel,
                onPlayVideo = { youtubeUrl ->
                    val youtubeId = getVideoIdFromUrl(youtubeUrl)
                    navController.navigate("playVideo/$youtubeId")
                },
                onNavigateToPlaylist = {
                    navController.navigate("playlist")
                },
                onLogout = {
                    userViewModel.logOut()
                    navController.navigate("login") {
                        popUpTo("home") { inclusive = true }
                    }
                }
            )
        }
        composable(
            route = "playVideo/{youtubeId}",
            arguments = listOf(navArgument("youtubeId") { type = NavType.StringType })
        ) { backStackEntry ->
            val youtubeId = backStackEntry.arguments?.getString("youtubeId") ?: ""
            if (youtubeId.isEmpty()) {
                navController.popBackStack()
            }
            VideoPlaybackScreen(youtubeId, onBackButton = { navController.popBackStack() })
        }
        composable("playlist") {
            PlaylistScreen(
                userViewModel = userViewModel,
                onNavigateBack = {
                    navController.popBackStack()
                },
                onPlayVideo = { youtubeUrl ->
                    val youtubeId = getVideoIdFromUrl(youtubeUrl)
                    navController.navigate("playVideo/$youtubeId")
                }
            )
        }
    }
}