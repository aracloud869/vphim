package com.example.ui.screens

import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.annotation.OptIn
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.MediaItem
import androidx.media3.common.MimeTypes
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import com.example.data.models.Movie
import com.example.ui.theme.BrandRed
import com.example.ui.theme.DarkBackground
import com.example.ui.viewmodel.MovieViewModel
import kotlinx.coroutines.delay

@OptIn(UnstableApi::class)
@Composable
fun PlayerScreen(
    movieSlug: String,
    episodeSlug: String,
    viewModel: MovieViewModel,
    onBackClick: () -> Unit,
    onEpisodeSelect: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val movie = remember(movieSlug) { viewModel.movies.value.firstOrNull { it.slug == movieSlug } }

    if (movie == null) {
        Box(modifier = Modifier.fillMaxSize().background(DarkBackground), contentAlignment = Alignment.Center) {
            Text("Không tìm thấy phim", color = Color.White)
        }
        return
    }

    // Find the current episode details
    val server = movie.servers.firstOrNull()
    val episode = server?.episodes?.firstOrNull { it.slug == episodeSlug } ?: server?.episodes?.firstOrNull()

    if (episode == null) {
        Box(modifier = Modifier.fillMaxSize().background(DarkBackground), contentAlignment = Alignment.Center) {
            Text("Không tìm thấy tập phim", color = Color.White)
        }
        return
    }

    // Load initial progress history from database
    val lastHistory by viewModel.getWatchHistoryForMovie(movieSlug).collectAsState(initial = null)

    // Setup ExoPlayer
    val exoPlayer = remember {
        ExoPlayer.Builder(context).build().apply {
            playWhenReady = true
        }
    }

    // Keep track of player state
    var isPlaying by remember { mutableStateOf(false) }

    // Prepare media item whenever episode URL changes
    LaunchedEffect(episode.m3u8Url) {
        val mediaItem = MediaItem.Builder()
            .setUri(episode.m3u8Url)
            .setMimeType(MimeTypes.APPLICATION_M3U8)
            .build()
        exoPlayer.setMediaItem(mediaItem)
        exoPlayer.prepare()

        // Seek to saved progress if exists for this movie
        lastHistory?.let {
            if (it.episodeSlug == episodeSlug && it.lastTimeSeconds > 0) {
                exoPlayer.seekTo(it.lastTimeSeconds * 1000)
            }
        }
    }

    // Save progress periodically (every 5 seconds) while playing
    LaunchedEffect(exoPlayer, isPlaying) {
        val listener = object : Player.Listener {
            override fun onIsPlayingChanged(playing: Boolean) {
                isPlaying = playing
            }
        }
        exoPlayer.addListener(listener)
        isPlaying = exoPlayer.isPlaying

        while (true) {
            delay(5000)
            if (exoPlayer.isPlaying) {
                val currentSec = exoPlayer.currentPosition / 1000
                val totalSec = exoPlayer.duration / 1000
                if (currentSec > 0 && totalSec > 0) {
                    viewModel.saveProgress(
                        movieSlug = movie.slug,
                        movieName = movie.name,
                        posterUrl = movie.posterUrl,
                        episodeName = episode.name,
                        episodeSlug = episode.slug,
                        lastTimeSeconds = currentSec,
                        totalDurationSeconds = totalSec
                    )
                }
            }
        }
    }

    // Clean up player on dispose
    DisposableEffect(Unit) {
        onDispose {
            // Save final progress on exit
            val currentSec = exoPlayer.currentPosition / 1000
            val totalSec = exoPlayer.duration / 1000
            if (currentSec > 0 && totalSec > 0) {
                viewModel.saveProgress(
                    movieSlug = movie.slug,
                    movieName = movie.name,
                    posterUrl = movie.posterUrl,
                    episodeName = episode.name,
                    episodeSlug = episode.slug,
                    lastTimeSeconds = currentSec,
                    totalDurationSeconds = totalSec
                )
            }
            exoPlayer.release()
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(DarkBackground)
            .testTag("player_screen")
    ) {
        // Top Back bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 40.dp, start = 16.dp, end = 16.dp, bottom = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = onBackClick,
                modifier = Modifier
                    .clip(RoundedCornerShape(50))
                    .background(Color.White.copy(alpha = 0.1f))
                    .testTag("player_back_button")
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Quay lại",
                    tint = Color.White
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column {
                Text(
                    text = movie.name,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    maxLines = 1
                )
                Text(
                    text = "Đang phát: ${episode.name}",
                    color = Color.Gray,
                    fontSize = 12.sp,
                    maxLines = 1
                )
            }
        }

        // Native Media3 Player view
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(240.dp)
                .background(Color.Black)
        ) {
            AndroidView(
                factory = { ctx ->
                    PlayerView(ctx).apply {
                        player = exoPlayer
                        useController = true
                        layoutParams = FrameLayout.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT
                        )
                    }
                },
                modifier = Modifier.fillMaxSize()
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Episode List for quick swap
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            Text(
                text = "Chọn tập phim khác",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )

            Spacer(modifier = Modifier.height(12.dp))

            if (server != null) {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(server.episodes) { ep ->
                        val isCurrent = ep.slug == episodeSlug
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (isCurrent) BrandRed else Color.DarkGray)
                                .clickable { onEpisodeSelect(ep.slug) }
                                .padding(horizontal = 16.dp, vertical = 12.dp)
                        ) {
                            Text(
                                text = ep.name,
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp
                            )
                        }
                    }
                }
            }
        }
    }
}
