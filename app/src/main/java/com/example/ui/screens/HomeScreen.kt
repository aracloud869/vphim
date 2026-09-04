package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.FeaturedMovieBanner
import com.example.ui.components.MovieSectionRow
import com.example.ui.theme.BrandRed
import com.example.ui.theme.DarkBackground
import com.example.ui.viewmodel.MovieViewModel

@Composable
fun HomeScreen(
    viewModel: MovieViewModel,
    onMovieClick: (String) -> Unit,
    onWatchClick: (String, String) -> Unit,
    onSearchTrigger: () -> Unit,
    modifier: Modifier = Modifier
) {
    val movies by viewModel.movies.collectAsState()
    val trending by viewModel.trendingMovies.collectAsState()
    
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(DarkBackground)
            .verticalScroll(scrollState)
            .testTag("home_screen")
    ) {
        // App header with brand name and a beautiful search bar trigger
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "VePhim",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.ExtraBold,
                color = BrandRed,
                letterSpacing = 1.sp
            )

            // Glassmorphic / rounded search button trigger
            IconButton(
                onClick = onSearchTrigger,
                modifier = Modifier
                    .clip(RoundedCornerShape(50))
                    .background(Color.White.copy(alpha = 0.1f))
                    .testTag("home_search_button")
            ) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search movies",
                    tint = Color.White
                )
            }
        }

        // Hero Movie Banner (e.g. first movie "Mai")
        if (trending.isNotEmpty()) {
            val featuredMovie = trending[0]
            FeaturedMovieBanner(
                movie = featuredMovie,
                onPlayClick = {
                    val defaultEpisode = featuredMovie.servers.firstOrNull()?.episodes?.firstOrNull()
                    if (defaultEpisode != null) {
                        onWatchClick(featuredMovie.slug, defaultEpisode.slug)
                    } else {
                        onMovieClick(featuredMovie.slug)
                    }
                },
                onInfoClick = { onMovieClick(featuredMovie.slug) }
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Section 1: Trending
        MovieSectionRow(
            title = "Phim Thịnh Hành",
            movies = trending,
            onMovieClick = { onMovieClick(it.slug) }
        )

        // Section 2: Newly Updated Single Movies (Phim lẻ)
        val singleMovies = movies.filter { it.type == "single" }
        if (singleMovies.isNotEmpty()) {
            MovieSectionRow(
                title = "Phim Lẻ Mới Cập Nhật",
                movies = singleMovies,
                onMovieClick = { onMovieClick(it.slug) }
            )
        }

        // Section 3: Series Movies (Phim bộ)
        val seriesMovies = movies.filter { it.type == "series" }
        if (seriesMovies.isNotEmpty()) {
            MovieSectionRow(
                title = "Phim Bộ Đặc Sắc",
                movies = seriesMovies,
                onMovieClick = { onMovieClick(it.slug) }
            )
        }

        // Section 4: Anime / Cartoons (Phim hoạt hình)
        val animeMovies = movies.filter { it.categories.contains("Hoạt Hình") }
        if (animeMovies.isNotEmpty()) {
            MovieSectionRow(
                title = "Hoạt Hình Chọn Lọc",
                movies = animeMovies,
                onMovieClick = { onMovieClick(it.slug) }
            )
        }

        Spacer(modifier = Modifier.height(80.dp)) // Extra space to scroll past Navigation Bar
    }
}
