package SIT305.a5_1c

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController



@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MainScreen(nc: NavController) {
    val stories = StoryRepository.stories
    val topStories = stories.take(5)

    Column(modifier = Modifier.fillMaxSize()) {
        Text(
            text = "Top Stories",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(8.dp)
        )
        LazyRow(modifier = Modifier.fillMaxWidth().height(200.dp)) {
            items(topStories) { story ->
                TopStoryCard(nc, story)
            }
        }
        HorizontalDivider(
            color = Color.Black,
            thickness = 2.dp,
        )
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(stories) { story ->
                FullWidthStoryCard(nc, story)
            }
        }
    }
}


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun StoryScreen(nc: NavController, storyId: Int) {
    val story = StoryRepository.stories.find { it.id == storyId }
    if (story == null) {
        Text("Story not found")
        return
    }
    val relatedStories = StoryRepository.getRelatedStories(storyId)
    val painter = painterResource(id = story.imageResId)

    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.7f)
            ) {
                Image(
                    painter = painter,
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentScale = ContentScale.Crop
                )
                Text(
                    text = story.headline,
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.padding(8.dp)
                )
                Text(
                    text = story.body,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(start = 8.dp, end = 8.dp, bottom = 8.dp)
                )
                HorizontalDivider(
                    color = Color.Black,
                    thickness = 2.dp,
                    modifier = Modifier.padding(8.dp)
                )
            }
            Text(
                text = "Related Stories",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(8.dp)
            )
            LazyRow(modifier = Modifier.fillMaxWidth().height(200.dp)) {
                items(relatedStories) { relatedStory ->
                    TopStoryCard(nc, relatedStory)
                }
            }
        }

        // Kind of feel like it needed this nav
        IconButton(
            onClick = { nc.popBackStack() },
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(16.dp)
                .size(48.dp)
                .background(Color.White, shape = CircleShape)
                .padding(8.dp)
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Back",
                tint = Color.Black
            )
        }
    }
}

@Composable
fun TopStoryCard(nc: NavController, story: Story) {
    Card(
        modifier = Modifier
            .width(200.dp)
            .padding(8.dp)
            .clickable {
                nc.navigate("story/${story.id}")
            },
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column {
            Image(
                painter = painterResource(id = story.imageResId),
                contentDescription = "Story Image",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
                contentScale = ContentScale.Crop
            )
            Text(
                text = story.headline,
                style = MaterialTheme.typography.titleMedium,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(8.dp)
            )
        }
    }
}

@Composable
fun FullWidthStoryCard(nc: NavController, story: Story) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable {
                nc.navigate("story/${story.id}")
            },
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column {
            Image(
                painter = painterResource(id = story.imageResId),
                contentDescription = "Story Image",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                contentScale = ContentScale.Crop
            )
            Text(
                text = story.headline,
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(8.dp)
            )
            Text(
                text = story.date.toString(),
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(start = 8.dp, end = 8.dp, bottom = 8.dp)
            )
        }
    }
}