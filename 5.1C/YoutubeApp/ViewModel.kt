package SOT305.a5_1c_2

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


class UserViewModel(private val userRepo: UserRepo) : ViewModel() {
    private val _playlist = MutableStateFlow("")
    val playlist: StateFlow<String> = _playlist
    private var userId = -1
    var loggedIn = false

    fun logIn(user: User) {
        getPlaylistByUserId(user.id)
        userId = user.id
        loggedIn = true
    }

    fun logOut() {
        userId = -1
        loggedIn = false
    }

    private fun getPlaylistByUserId(userId: Int) {
        viewModelScope.launch {
            userRepo.getPlaylistByUserIdAsFlow(userId).collect { playlist ->
                Log.d("UserViewModel", "Playlist updated: $playlist")
                _playlist.value = playlist
            }
        }
    }

    fun addVideoToPlaylist(videoUrl: String) {
        viewModelScope.launch {
            userRepo.addVideoToPlaylist(userId, videoUrl)
        }
    }
}
