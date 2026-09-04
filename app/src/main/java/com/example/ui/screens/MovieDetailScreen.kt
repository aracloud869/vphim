package com.example.ui.screens

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.data.models.Movie
import com.example.ui.theme.BrandRed
import com.example.ui.theme.DarkBackground
import com.example.ui.theme.DarkSurfaceVariant
import com.example.ui.viewmodel.MovieViewModel

@Composable
fun MovieDetailScreen(
    movieSlug: String,
    viewModel: MovieViewModel,
    onBackClick: () -> Unit,
    onWatchClick: (String, String) -> Unit,
    modifier: Modifier = Modifier
) {
    val movie = remember(movieSlug) { viewModel.movies.value.firstOrNull { it.slug == movieSlug } }

    if (movie == null) {
        Box(modifier = Modifier.fillMaxSize().background(DarkBackground), contentAlignment = Alignment.Center) {
            Text("Không tìm thấy phim", color = Color.White)
        }
        return
    }

    val isBookmarked by viewModel.isMovieBookmarked(movie.id).collectAsState(initial = false)
    val lastHistory by viewModel.getWatchHistoryForMovie(movie.slug).collectAsState(initial = null)
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(DarkBackground)
            .verticalScroll(scrollState)
            .testTag("movie_detail_screen")
    ) {
        // Hero image header with back arrow
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(280dp)
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(movie.thumbUrl)
                    .crossfade(true)
                    .build(),
                contentDescription = movie.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )

            // Dark gradient overlay
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color.Black.copy(alpha = 0.5f),
                                Color.Transparent,
                                DarkBackground
                            )
                        )
                    )
            )

            // Back button
            IconButton(
                onClick = onBackClick,
                modifier = Modifier
                    .padding(top = 40dp, start = 16dp)
                    .clip(RoundedCornerShape(50))
                    .background(Color.Black.copy(alpha = 0.6f))
                    .align(Alignment.TopStart)
                    .testTag("detail_back_button")
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.White
                )
            }
        }

        // Movie Info content
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            // Main title
            Text(
                text = movie.name,
                style = MaterialTheme.typography.displayLarge,
                fontSize = 26.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color.White
            )

            // Origin title
            Text(
                text = "${movie.originName} (${movie.year})",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.LightGray,
                fontSize = 14.sp
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Meta tags (Imdb, duration, quality, lang)
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                // IMDb Badge
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .background(Color(0xFFFFD700), RoundedCornerShape(4.dp))
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = "IMDb",
                        tint = Color.Black,
                        modifier = Modifier.size(12.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = movie.ratingImdb.toString(),
                        color = Color.Black,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                BadgeTag(text = movie.quality, color = BrandRed)
                BadgeTag(text = movie.lang, color = Color.Gray)
                BadgeTag(text = movie.duration, color = Color.Gray)
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Action Buttons Row: Watch & Bookmark
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Play / Resume Button
                Button(
                    onClick = {
                        val episodeSlug = lastHistory?.episodeSlug ?: movie.servers.firstOrNull()?.episodes?.firstOrNull()?.slug ?: "full"
                        onWatchClick(movie.slug, episodeSlug)
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = BrandRed),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier
                        .weight(1f)
                        .height(48dp)
                        .testTag("detail_play_button")
                ) {
                    Icon(Icons.Default.PlayArrow, contentDescription = "Play", tint = Color.White)
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = if (lastHistory != null) "Xem tiếp (${lastHistory?.episodeName})" else "Xem Ngay",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp
                    )
                }

                // Bookmark Toggle Button
                Button(
                    onClick = { viewModel.toggleBookmark(movie, isBookmarked) },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isBookmarked) Color.DarkGray else Color.White
                    ),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier
                        .width(130dp)
                        .height(48dp)
                        .testTag("detail_bookmark_button")
                ) {
                    Icon(
                        imageVector = if (isBookmarked) Icons.Default.Check else Icons.Default.Add,
                        contentDescription = "Bookmark",
                        tint = if (isBookmarked) Color.White else Color.Black
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = if (isBookmarked) "Đã Lưu" else "Tủ Phim",
                        color = if (isBookmarked) Color.White else Color.Black,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Episode selection
            Text(
                text = "Danh sách Tập phim",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Loop through servers (usually one VIP server for mock)
            movie.servers.forEach { server ->
                Column(modifier = Modifier.padding(bottom = 12.dp)) {
                    Text(
                        text = server.serverName,
                        fontSize = 12.sp,
                        color = Color.Gray,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.padding(bottom = 6.dp)
                    )

                    // Lazy row of episode pills
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        items(server.episodes) { episode ->
                            val isWatched = lastHistory?.episodeSlug == episode.slug
                            EpisodePill(
                                title = episode.name,
                                active = isWatched,
                                onClick = { onWatchClick(movie.slug, episode.slug) }
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Movie Plot Content
            Text(
                text = "Nội dung phim",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = movie.content,
                style = MaterialTheme.typography.bodyMedium,
                color = Color.LightGray,
                lineHeight = 22.sp
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Directors & Actors info
            MovieMetaSection(title = "Đạo diễn", list = movie.directors)
            MovieMetaSection(title = "Diễn viên", list = movie.actors)
            MovieMetaSection(title = "Thể loại", list = movie.categories)
            MovieMetaSection(title = "Quốc gia", list = movie.countries)

            Spacer(modifier = Modifier.height(80.dp))
        }
    }
}

@Composable
fun BadgeTag(text: String, color: Color) {
    Box(
        modifier = Modifier
            .background(color.copy(alpha = 0.2f), RoundedCornerShape(4.dp))
            .border(1.dp, color.copy(alpha = 0.4f), RoundedCornerShape(4.dp))
            .padding(horizontal = 6.dp, vertical = 2.dp)
    ) {
        Text(text = text, color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Medium)
    }
}

@Composable
fun EpisodePill(
    title: String,
    active: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(if (active) BrandRed else DarkSurfaceVariant)
            .border(1.dp, if (active) BrandRed else Color.DarkGray, RoundedCornerShape(8.dp))
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 10.dp)
            .testTag("episode_pill_$title")
    ) {
        Text(
            text = title,
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 13.sp
        )
    }
}

@Composable
fun MovieMetaSection(title: String, list: List<String>) {
    if (list.isEmpty()) return
    Column(modifier = Modifier.padding(bottom = 8.dp)) {
        Row {
            Text(
                text = "$title: ",
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp,
                color = Color.Gray
            )
            Text(
                text = list.joinToString(", "),
                fontSize = 13.sp,
                color = Color.LightGray
            )
        }
    }
}
