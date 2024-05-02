package SIT305.a5_1c

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDate


data class Story(
    val id: Int,
    val headline: String,
    val body: String,
    val imageResId: Int,
    val date: LocalDate
)


// Emulate a DAO that provides a list of stories
@RequiresApi(Build.VERSION_CODES.O)
object StoryRepository {
    val text = "This is a placeholder story. It is a story that is used to fill in the gaps of a story that is not yet available. It is a story that is used to fill in the gaps of a story that is not yet available. It is a story that is used to fill in the gaps of a story that is not yet available. It is a story that is used to fill in the gaps of a story that is not yet available. It is a story that is used to fill in the gaps of a story that is not yet available. It is a story that is used to fill in the gaps of a story that is not yet available. It is a story that is used to fill in the gaps of a story that is not yet available. It is a story that is used to fill in the gaps of a story that is not yet available. It is a story that is used to fill in the gaps of a story that is not yet available."
    // Fake news
    val stories = listOf(
        Story(1, "Headline 1", text, R.drawable.placeholder, LocalDate.now()),
        Story(2, "Headline 2", text, R.drawable.placeholder, LocalDate.now().minusDays(1)),
        Story(3, "Headline 1", text, R.drawable.placeholder, LocalDate.now().minusDays(3)),
        Story(4, "Headline 2", text, R.drawable.placeholder, LocalDate.now().minusDays(2)),
        Story(5, "Headline 1", text, R.drawable.placeholder, LocalDate.now().minusDays(10)),
        Story(6, "Headline 2", text, R.drawable.placeholder, LocalDate.now().minusDays(15)),
        Story(7, "Headline 1", text, R.drawable.placeholder, LocalDate.now().minusDays(2)),
        Story(8, "Headline 2", text, R.drawable.placeholder, LocalDate.now().minusDays(1)),
        // Add more placeholder stories
    )

    fun getRelatedStories(storyId: Int): List<Story> {
        // PLACEHOLDER: Return a list of related stories
        return stories.filter { it.id != storyId }.take(5)
    }
}