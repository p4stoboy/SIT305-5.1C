package SOT305.a5_1c_2

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView
import kotlinx.coroutines.launch
import java.util.regex.Pattern


@Composable
fun LoginScreen(
    navController: NavController,
    userRepo: UserRepo,
    userViewModel: UserViewModel,
    onLoginSuccess: () -> Unit
) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var loginError by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("LOGIN")

        TextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("username") }
        )

        TextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("password") },
            visualTransformation = PasswordVisualTransformation()
        )

        if (loginError) {
            Text(
                text = "Invalid username or password",
                color = Color.Red,
                modifier = Modifier.padding(top = 8.dp)
            )
        }

        Button(
            onClick = {
                userViewModel.viewModelScope.launch {
                    val user = userRepo.getUserByCredentials(username, password)
                    if (user != null) {
                        userViewModel.logIn(user)
                        onLoginSuccess()
                    } else {
                        loginError = true
                    }
                }
            },
            modifier = Modifier.padding(top = 16.dp)
        ) {
            Text("LOGIN")
        }

        TextButton(
            onClick = {
                navController.navigate("signup")
            },
            modifier = Modifier.padding(top = 8.dp)
        ) {
            Text("SIGN UP")
        }
    }
}

@Composable
fun SignupScreen(
    userRepo: UserRepo,
    userViewModel: UserViewModel,
    onBackButton: () -> Unit,
    onSignupSuccess: () -> Unit
) {
    var fullName by remember { mutableStateOf("") }
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var signupError by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Sign up")

        TextField(
            value = fullName,
            onValueChange = { fullName = it },
            label = { Text("Full name") }
        )

        TextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("Username") }
        )

        TextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("password:") },
            visualTransformation = PasswordVisualTransformation()
        )

        TextField(
            value = confirmPassword,
            onValueChange = { confirmPassword = it },
            label = { Text("Confirm password:") },
            visualTransformation = PasswordVisualTransformation()
        )

        if (password != confirmPassword) {
            Text(
                text = "Passwords do not match",
                color = Color.Red,
                modifier = Modifier.padding(top = 8.dp)
            )
        }

        if (signupError) {
            Text(
                text = "Error occurred during signup",
                color = Color.Red,
                modifier = Modifier.padding(top = 8.dp)
            )
        }

        Button(
            onClick = {
                if (password == confirmPassword) {
                    userViewModel.viewModelScope.launch {
                        val newUser = User(name = fullName, username = username, password = password)
                        try {
                        userRepo.insertUser(newUser)
                        } catch (e: Exception) {
                            signupError = true
                            return@launch
                        }
                        userViewModel.logIn(newUser)
                        onSignupSuccess()
                    }
                }
            },
            modifier = Modifier.padding(top = 16.dp)
        ) {
            Text("Create account")
        }
        IconButton(
            onClick = {
                onBackButton()
            },
            modifier = Modifier
                .padding(16.dp)
                .size(48.dp)
                .align(Alignment.Start)
        ) {
            Icon(Icons.Default.ArrowBack, contentDescription = "Back")
        }
    }
}

@Composable
fun HomeScreen(userViewModel: UserViewModel, onPlayVideo: (String) -> Unit, onNavigateToPlaylist: () -> Unit, onLogout: () -> Unit) {
    var youtubeUrl by remember { mutableStateOf("") }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("YouTube URL:")

        TextField(
            value = youtubeUrl,
            onValueChange = { youtubeUrl = it },
            label = { Text("Enter YouTube URL") }
        )

        Button(
            onClick = {
                onPlayVideo(youtubeUrl)
            },
            modifier = Modifier.padding(top = 16.dp)
        ) {
            Text("PLAY")
        }

        Button(
            onClick = {
                userViewModel.addVideoToPlaylist(youtubeUrl)
            },
            modifier = Modifier.padding(top = 8.dp)
        ) {
            Text("ADD TO PLAYLIST")
        }

        Button(
            onClick = {
                onNavigateToPlaylist()
            },
            modifier = Modifier.padding(top = 8.dp)
        ) {
            Text("MY PLAYLIST")
        }

        Button(
            onClick = {
                onLogout()
            },
            modifier = Modifier.padding(top = 8.dp)
        ) {
            Text("LOGOUT")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlaylistScreen(
    userViewModel: UserViewModel,
    onNavigateBack: () -> Unit,
    onPlayVideo: (String) -> Unit
) {
    val playlist by userViewModel.playlist.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("My Playlist") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(16.dp)
        ) {
            items(playlist.split(",")) { videoUrl ->
                if (videoUrl.isNotBlank()) {
                    PlaylistItem(
                        videoUrl = videoUrl,
                        onPlayVideo = onPlayVideo
                    )
                }
            }
        }
    }
}

@Composable
fun PlaylistItem(
    videoUrl: String,
    onPlayVideo: (String) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onPlayVideo(videoUrl) }
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Default.PlayArrow,
            contentDescription = "Play",
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = videoUrl,
            modifier = Modifier.weight(1f),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
    Divider()
}

@Composable
fun VideoPlaybackScreen(youtubeId: String, onBackButton: () -> Unit) {
    // handle player lifecycle
    val context = LocalContext.current
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    val youTubePlayerView = remember { YouTubePlayerView(context) }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        AndroidView(
            factory = { youTubePlayerView },
            modifier = Modifier.fillMaxSize()
        ) { view ->
            lifecycle.addObserver(view)
            view.addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
                override fun onReady(youTubePlayer: YouTubePlayer) {
                    youTubePlayer.loadVideo(youtubeId, 0f)
                }
            })
        }
        IconButton(
            onClick = {
                onBackButton()
            },
            modifier = Modifier
                .padding(16.dp)
                .size(48.dp)
                .align(Alignment.BottomStart)
        ) {
            Icon(Icons.Default.ArrowBack, contentDescription = "Back")
        }
    }

    DisposableEffect(youTubePlayerView) {
        onDispose {
            youTubePlayerView.release()
        }
    }

}
 // https://youtu.be/3Vx6i3lv5L8