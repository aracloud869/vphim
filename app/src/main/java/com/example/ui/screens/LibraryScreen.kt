package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
import com.example.data.local.BookmarkedMovieEntity
import com.example.data.local.WatchHistoryEntity
import com.example.ui.components.MovieCard
import com.example.ui.theme.BrandRed
import com.example.ui.theme.DarkBackground
import com.example.ui.theme.DarkSurfaceVariant
import com.example.ui.viewmodel.MovieViewModel

@Composable
fun LibraryScreen(
    viewModel: MovieViewModel,
    onMovieClick: (String) -> Unit,
    onWatchClick: (String, String) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedTab by remember { mutableStateOf(0) }
    val tabTitles = listOf("Tủ Phim", "Lịch Sử Xem")

    val bookmarks by viewModel.bookmarks.collectAsState()
    val watchHistory by viewModel.watchHistory.collectAsState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(DarkBackground)
            .padding(16.dp)
            .testTag("library_screen")
    ) {
        Text(
            text = "Thư viện của tôi",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        // Custom M3 Tab Layout
        TabRow(
            selectedTabIndex = selectedTab,
            containerColor = Color.Transparent,
            contentColor = BrandRed,
            indicator = { tabPositions ->
                SecondaryTabRowDefaults.Indicator(
                    modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                    color = BrandRed
                )
            },
            modifier = Modifier.fillMaxWidth().testTag("library_tab_row")
        ) {
            tabTitles.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTab == index,
                    onClick = { selectedTab = index },
                    text = {
                        Text(
                            text = title,
                            fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Medium,
                            fontSize = 15.sp,
                            color = if (selectedTab == index) Color.White else Color.Gray
                        )
                    },
                    modifier = Modifier.testTag("library_tab_$index")
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        when (selectedTab) {
            0 -> BookmarkTabContent(
                bookmarks = bookmarks,
                onMovieClick = onMovieClick
            )
            1 -> WatchHistoryTabContent(
                history = watchHistory,
                onWatchClick = onWatchClick,
                onDeleteClick = { viewModel.removeHistory(it) }
            )
        }

        Spacer(modifier = Modifier.height(80.dp)) // Navigation bar safe zone
    }
}

@Composable
fun BookmarkTabContent(
    bookmarks: List<BookmarkedMovieEntity>,
    onMovieClick: (String) -> Unit
) {
    if (bookmarks.isEmpty()) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(
                    imageVector = Icons.Default.Favorite,
                    contentDescription = "No bookmarks",
                    tint = Color.DarkGray,
                    modifier = Modifier.size(64.dp)
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "Tủ phim đang trống",
                    color = Color.Gray,
                    fontWeight = FontWeight.Medium
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Lưu phim yêu thích của bạn để xem sau",
                    color = Color.DarkGray,
                    fontSize = 12.sp
                )
            }
        }
    } else {
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.fillMaxSize().testTag("bookmarks_grid")
        ) {
            items(bookmarks) { bookmark ->
                // Map BookmarkedMovieEntity back to standard parameters to render MovieCard
                val mockMovie = remember(bookmark) {
                    com.example.data.models.Movie(
                        id = bookmark.id,
                        name = bookmark.name,
                        originName = bookmark.originName,
                        slug = bookmark.slug,
                        content = "",
                        type = bookmark.type,
                        status = "completed",
                        thumbUrl = "",
                        posterUrl = bookmark.posterUrl,
                        year = bookmark.year,
                        quality = bookmark.quality,
                        lang = "Vietsub",
                        duration = "",
                        episodeCurrent = "Full",
                        ratingImdb = 0.0,
                        ratingTmdb = 0.0,
                        categories = bookmark.categoriesString.split(","),
                        countries = emptyList(),
                        actors = emptyList(),
                        directors = emptyList()
                    )
                }

                MovieCard(
                    movie = mockMovie,
                    onClick = { onMovieClick(bookmark.slug) }
                )
            }
        }
    }
}

@Composable
fun WatchHistoryTabContent(
    history: List<WatchHistoryEntity>,
    onWatchClick: (String, String) -> Unit,
    onDeleteClick: (String) -> Unit
) {
    if (history.isEmpty()) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(
                    imageVector = Icons.Default.PlayArrow,
                    contentDescription = "No history",
                    tint = Color.DarkGray,
                    modifier = Modifier.size(64.dp)
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "Chưa có lịch sử xem phim",
                    color = Color.Gray,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    } else {
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxSize().testTag("history_list")
        ) {
            items(history) { item ->
                WatchHistoryItemRow(
                    item = item,
                    onPlayClick = { onWatchClick(item.movieSlug, item.episodeSlug) },
                    onDeleteClick = { onDeleteClick(item.movieSlug) }
                )
            }
        }
    }
}

@Composable
fun WatchHistoryItemRow(
    item: WatchHistoryEntity,
    onPlayClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = DarkSurfaceVariant),
        modifier = Modifier
            .fillMaxWidth()
            .height(100dp)
            .clickable { onPlayClick() }
    ) {
        Row(modifier = Modifier.fillMaxSize()) {
            // Movie Poster Image
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(item.posterUrl)
                    .crossfade(true)
                    .build(),
                contentDescription = item.movieName,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .width(70dp)
                    .fillMaxHeight()
            )

            Spacer(modifier = Modifier.width(12.dp))

            // Text Info & Progress
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .padding(vertical = 10.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = item.movieName,
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = "Đang xem: ${item.episodeName}",
                        fontSize = 12.sp,
                        color = BrandRed,
                        fontWeight = FontWeight.Medium
                    )
                }

                // Progress percentage bar calculation
                val percentage = if (item.totalDurationSeconds > 0) {
                    (item.lastTimeSeconds.toFloat() / item.totalDurationSeconds.toFloat() * 100).toInt()
                } else 0

                Column(modifier = Modifier.fillMaxWidth().padding(end = 8.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Đã xem $percentage%",
                            fontSize = 11.sp,
                            color = Color.LightGray
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    LinearProgressIndicator(
                        progress = if (item.totalDurationSeconds > 0) (item.lastTimeSeconds.toFloat() / item.totalDurationSeconds.toFloat()) else 0f,
                        color = BrandRed,
                        trackColor = Color.DarkGray,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(4.dp)
                            .clip(RoundedCornerShape(2.dp))
                    )
                }
            }

            // Delete History button
            IconButton(
                onClick = onDeleteClick,
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .padding(end = 8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Xóa lịch sử",
                    tint = Color.Gray
                )
            }
        }
    }
}
