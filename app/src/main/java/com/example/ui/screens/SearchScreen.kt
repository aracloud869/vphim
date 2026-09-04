package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.remote.MockMovieData
import com.example.ui.components.MovieCard
import com.example.ui.theme.BrandRed
import com.example.ui.theme.DarkBackground
import com.example.ui.theme.DarkSurfaceVariant
import com.example.ui.viewmodel.MovieViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    viewModel: MovieViewModel,
    onMovieClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val searchQuery by viewModel.searchQuery.collectAsState()
    val searchResults by viewModel.searchResults.collectAsState()
    val selectedCategory by viewModel.selectedCategory.collectAsState()
    val selectedCountry by viewModel.selectedCountry.collectAsState()
    val selectedType by viewModel.selectedType.collectAsState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(DarkBackground)
            .padding(16.dp)
            .testTag("search_screen")
    ) {
        Text(
            text = "Tìm kiếm Phim",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        // Search Text Field
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { viewModel.onSearchQueryChanged(it) },
            placeholder = { Text("Nhập tên phim, thể loại, diễn viên...", color = Color.Gray) },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search", tint = Color.LightGray) },
            trailingIcon = {
                if (searchQuery.isNotEmpty()) {
                    IconButton(onClick = { viewModel.onSearchQueryChanged("") }) {
                        Icon(Icons.Default.Clear, contentDescription = "Clear", tint = Color.LightGray)
                    }
                }
            },
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = BrandRed,
                unfocusedBorderColor = Color.DarkGray,
                containerColor = DarkSurfaceVariant
            ),
            shape = RoundedCornerShape(12.dp),
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .testTag("search_input_field")
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Filter 1: Categories selector (Scrollable Row)
        Text(
            text = "Thể loại",
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color.LightGray,
            modifier = Modifier.padding(bottom = 6.dp)
        )
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            item {
                FilterTag(
                    text = "Tất cả",
                    selected = selectedCategory == null,
                    onClick = { viewModel.onCategorySelected(null) }
                )
            }
            items(MockMovieData.allCategories) { category ->
                FilterTag(
                    text = category,
                    selected = selectedCategory == category,
                    onClick = { viewModel.onCategorySelected(category) }
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Filter 2: Type (Phim lẻ vs Phim bộ)
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            FilterTag(
                text = "Phim Lẻ",
                selected = selectedType == "single",
                onClick = { viewModel.onTypeSelected(if (selectedType == "single") null else "single") }
            )
            FilterTag(
                text = "Phim Bộ",
                selected = selectedType == "series",
                onClick = { viewModel.onTypeSelected(if (selectedType == "series") null else "series") }
            )
            if (selectedCategory != null || selectedCountry != null || selectedType != null || searchQuery.isNotEmpty()) {
                TextButton(
                    onClick = { viewModel.resetFilters() },
                    colors = ButtonDefaults.textButtonColors(contentColor = BrandRed)
                ) {
                    Text("Xóa bộ lọc", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Search Results Grid
        if (searchResults.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Empty Search",
                        tint = Color.DarkGray,
                        modifier = Modifier.size(64.dp)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "Không tìm thấy phim phù hợp",
                        color = Color.Gray,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .testTag("search_results_grid")
            ) {
                items(searchResults) { movie ->
                    MovieCard(
                        movie = movie,
                        onClick = { onMovieClick(movie.slug) },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(80.dp)) // Nav bar safe area
    }
}

@Composable
fun FilterTag(
    text: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(if (selected) BrandRed else DarkSurfaceVariant)
            .clickable { onClick() }
            .padding(horizontal = 12.dp, vertical = 6.dp)
    ) {
        Text(
            text = text,
            color = Color.White,
            fontSize = 12.sp,
            fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal
        )
    }
}
