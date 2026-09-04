package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.data.local.AppDatabase
import com.example.data.repository.MovieRepository
import com.example.ui.screens.*
import com.example.ui.theme.BrandRed
import com.example.ui.theme.DarkBackground
import com.example.ui.theme.VePhimTheme
import com.example.ui.viewmodel.MovieViewModel

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Init Room Database and Repository
        val database = AppDatabase.getDatabase(applicationContext)
        val repository = MovieRepository(database.movieDao())

        setContent {
            // Instantiate ViewModel with Custom Factory
            val viewModel: MovieViewModel by viewModels {
                MovieViewModel.Factory(repository)
            }

            VePhimTheme {
                val navController = rememberNavController()
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination

                // Define which screens should show the bottom navigation bar
                val bottomBarScreens = listOf("home", "search", "library")
                val showBottomBar = currentDestination?.route in bottomBarScreens

                Scaffold(
                    bottomBar = {
                        if (showBottomBar) {
                            NavigationBar(
                                containerColor = Color(0xFF141414),
                                contentColor = BrandRed,
                                modifier = Modifier
                                    .height(80.dp)
                                    .testTag("app_bottom_nav_bar")
                            ) {
                                // Tab 1: Trang chủ (Home)
                                val homeSelected = currentDestination?.hierarchy?.any { it.route == "home" } == true
                                NavigationBarItem(
                                    selected = homeSelected,
                                    onClick = {
                                        navController.navigate("home") {
                                            popUpTo(navController.graph.findStartDestination().id) {
                                                saveState = true
                                            }
                                            launchSingleTop = true
                                            restoreState = true
                                        }
                                    },
                                    icon = {
                                        Icon(
                                            imageVector = if (homeSelected) Icons.Default.Home else Icons.Outlined.Home,
                                            contentDescription = "Trang chủ"
                                        )
                                    },
                                    label = { Text("Trang chủ", fontSize = 11.sp) },
                                    colors = NavigationBarItemDefaults.colors(
                                        selectedIconColor = BrandRed,
                                        selectedTextColor = BrandRed,
                                        indicatorColor = Color.White.copy(alpha = 0.1f),
                                        unselectedIconColor = Color.Gray,
                                        unselectedTextColor = Color.Gray
                                    ),
                                    modifier = Modifier.testTag("nav_item_home")
                                )

                                // Tab 2: Tìm kiếm (Search)
                                val searchSelected = currentDestination?.hierarchy?.any { it.route == "search" } == true
                                NavigationBarItem(
                                    selected = searchSelected,
                                    onClick = {
                                        navController.navigate("search") {
                                            popUpTo(navController.graph.findStartDestination().id) {
                                                saveState = true
                                            }
                                            launchSingleTop = true
                                            restoreState = true
                                        }
                                    },
                                    icon = {
                                        Icon(
                                            imageVector = if (searchSelected) Icons.Default.Search else Icons.Outlined.Search,
                                            contentDescription = "Tìm kiếm"
                                        )
                                    },
                                    label = { Text("Tìm kiếm", fontSize = 11.sp) },
                                    colors = NavigationBarItemDefaults.colors(
                                        selectedIconColor = BrandRed,
                                        selectedTextColor = BrandRed,
                                        indicatorColor = Color.White.copy(alpha = 0.1f),
                                        unselectedIconColor = Color.Gray,
                                        unselectedTextColor = Color.Gray
                                    ),
                                    modifier = Modifier.testTag("nav_item_search")
                                )

                                // Tab 3: Thư viện (Library)
                                val librarySelected = currentDestination?.hierarchy?.any { it.route == "library" } == true
                                NavigationBarItem(
                                    selected = librarySelected,
                                    onClick = {
                                        navController.navigate("library") {
                                            popUpTo(navController.graph.findStartDestination().id) {
                                                saveState = true
                                            }
                                            launchSingleTop = true
                                            restoreState = true
                                        }
                                    },
                                    icon = {
                                        Icon(
                                            imageVector = if (librarySelected) Icons.Default.Favorite else Icons.Outlined.FavoriteBorder,
                                            contentDescription = "Thư viện"
                                        )
                                    },
                                    label = { Text("Thư viện", fontSize = 11.sp) },
                                    colors = NavigationBarItemDefaults.colors(
                                        selectedIconColor = BrandRed,
                                        selectedTextColor = BrandRed,
                                        indicatorColor = Color.White.copy(alpha = 0.1f),
                                        unselectedIconColor = Color.Gray,
                                        unselectedTextColor = Color.Gray
                                    ),
                                    modifier = Modifier.testTag("nav_item_library")
                                )
                            }
                        }
                    },
                    modifier = Modifier.fillMaxSize().background(DarkBackground)
                ) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = "home",
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        composable("home") {
                            HomeScreen(
                                viewModel = viewModel,
                                onMovieClick = { slug -> navController.navigate("detail/$slug") },
                                onWatchClick = { slug, epSlug -> navController.navigate("player/$slug/$epSlug") },
                                onSearchTrigger = { navController.navigate("search") }
                            )
                        }

                        composable("search") {
                            SearchScreen(
                                viewModel = viewModel,
                                onMovieClick = { slug -> navController.navigate("detail/$slug") }
                            )
                        }

                        composable("library") {
                            LibraryScreen(
                                viewModel = viewModel,
                                onMovieClick = { slug -> navController.navigate("detail/$slug") },
                                onWatchClick = { slug, epSlug -> navController.navigate("player/$slug/$epSlug") }
                            )
                        }

                        composable(
                            route = "detail/{slug}",
                            arguments = listOf(navArgument("slug") { type = NavType.StringType })
                        ) { backStackEntry ->
                            val slug = backStackEntry.arguments?.getString("slug") ?: ""
                            MovieDetailScreen(
                                movieSlug = slug,
                                viewModel = viewModel,
                                onBackClick = { navController.popBackStack() },
                                onWatchClick = { movieSlug, epSlug -> navController.navigate("player/$movieSlug/$epSlug") }
                            )
                        }

                        composable(
                            route = "player/{slug}/{episodeSlug}",
                            arguments = listOf(
                                navArgument("slug") { type = NavType.StringType },
                                navArgument("episodeSlug") { type = NavType.StringType }
                            )
                        ) { backStackEntry ->
                            val slug = backStackEntry.arguments?.getString("slug") ?: ""
                            val episodeSlug = backStackEntry.arguments?.getString("episodeSlug") ?: ""
                            PlayerScreen(
                                movieSlug = slug,
                                episodeSlug = episodeSlug,
                                viewModel = viewModel,
                                onBackClick = { navController.popBackStack() },
                                onEpisodeSelect = { nextEpisodeSlug ->
                                    navController.navigate("player/$slug/$nextEpisodeSlug") {
                                        popUpTo("player/$slug/$episodeSlug") { inclusive = true }
                                    }
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}
