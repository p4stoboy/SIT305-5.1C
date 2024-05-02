package SOT305.a5_1c_2


import android.content.Context
import androidx.room.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn


@Database(entities = [User::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
}

@Volatile // unnecessary - for threading
private var INSTANCE: AppDatabase? = null

/**
 * This is a top level function that provides a singleton instance of the database.
 * Doing it this way instead of using a companion object mainly out of spite.
 */
fun getDatabase(context: Context): AppDatabase {
    return INSTANCE ?: synchronized(AppDatabase::class.java) {
        val instance = Room.databaseBuilder(
            context.applicationContext,
            AppDatabase::class.java,
            "task_database"
        ).build()
        INSTANCE = instance
        instance
    }
}

@Entity
data class User(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    var name: String,
    var username: String,
    var password: String,
    var playlist: String = ""
)

@Dao
interface UserDao {
    @Query("SELECT * FROM User WHERE username = :username AND password = :password")
    suspend fun getUserByCredentials(username: String, password: String): User?

    @Query("SELECT * FROM User WHERE id = :userId")
    suspend fun getUserById(userId: Int): User?

    @Query("SELECT playlist FROM User WHERE id = :userId")
    suspend fun getPlaylistByUserId(userId: Int): String

    @Query("SELECT playlist FROM User WHERE id = :userId")
    fun getPlaylistByUserIdAsFlow(userId: Int): Flow<String>

    @Insert
    suspend fun insertUser(user: User): Long

    @Update
    suspend fun updateUser(user: User)
}

class UserRepo(private val userDao: UserDao, private val scope: CoroutineScope) {
    suspend fun getUserByCredentials(username: String, password: String): User? {
        return userDao.getUserByCredentials(username, password)
    }

    suspend fun insertUser(user: User) {
        userDao.insertUser(user)
    }

    fun getPlaylistByUserIdAsFlow(userId: Int): StateFlow<String> {
        return userDao.getPlaylistByUserIdAsFlow(userId)
            .stateIn(
                scope = scope,
                started = SharingStarted.WhileSubscribed(5000L),
                initialValue = ""
            )
    }

    suspend fun addVideoToPlaylist(userId: Int, videoUrl: String) {
        val user = userDao.getUserById(userId)
        user?.let {
            val updatedPlaylist = "${it.playlist}$videoUrl,"
            val updatedUser = it.copy(playlist = updatedPlaylist)
            userDao.updateUser(updatedUser)
        }
    }
}