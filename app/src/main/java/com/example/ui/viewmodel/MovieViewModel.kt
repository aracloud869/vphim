package com.example.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.data.local.BookmarkedMovieEntity
import com.example.data.local.WatchHistoryEntity
import com.example.data.models.Movie
import com.example.data.repository.MovieRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class MovieViewModel(private val repository: MovieRepository) : ViewModel() {

    // Main States
    val movies = MutableStateFlow<List<Movie>>(repository.getMoviesList())
    val trendingMovies = MutableStateFlow<List<Movie>>(repository.getTrendingMovies())
    
    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    private val _selectedCategory = MutableStateFlow<String?>(null)
    val selectedCategory = _selectedCategory.asStateFlow()

    private val _selectedCountry = MutableStateFlow<String?>(null)
    val selectedCountry = _selectedCountry.asStateFlow()

    private val _selectedType = MutableStateFlow<String?>(null) // "single" or "series"
    val selectedType = _selectedType.asStateFlow()

    private val _searchResults = MutableStateFlow<List<Movie>>(repository.getMoviesList())
    val searchResults = _searchResults.asStateFlow()

    // Reactive Bookmarks from Room DB
    val bookmarks: StateFlow<List<BookmarkedMovieEntity>> = repository.allBookmarks
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    // Reactive Watch History from Room DB
    val watchHistory: StateFlow<List<WatchHistoryEntity>> = repository.allWatchHistory
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    init {
        // Initially, search results are all movies
        updateSearchResults()
    }

    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
        updateSearchResults()
    }

    fun onCategorySelected(category: String?) {
        _selectedCategory.value = category
        updateSearchResults()
    }

    fun onCountrySelected(country: String?) {
        _selectedCountry.value = country
        updateSearchResults()
    }

    fun onTypeSelected(type: String?) {
        _selectedType.value = type
        updateSearchResults()
    }

    fun resetFilters() {
        _searchQuery.value = ""
        _selectedCategory.value = null
        _selectedCountry.value = null
        _selectedType.value = null
        _searchResults.value = repository.getMoviesList()
    }

    private fun updateSearchResults() {
        _searchResults.value = repository.searchMovies(
            keyword = _searchQuery.value,
            category = _selectedCategory.value,
            country = _selectedCountry.value,
            type = _selectedType.value
        )
    }

    // Bookmark Actions
    fun toggleBookmark(movie: Movie, isBookmarked: Boolean) {
        viewModelScope.launch {
            if (isBookmarked) {
                repository.removeBookmark(movie.id)
            } else {
                repository.addBookmark(movie)
            }
        }
    }

    fun isMovieBookmarked(movieId: String): Flow<Boolean> {
        return repository.isBookmarked(movieId)
    }

    // History Actions
    fun saveProgress(
        movieSlug: String,
        movieName: String,
        posterUrl: String,
        episodeName: String,
        episodeSlug: String,
        lastTimeSeconds: Long,
        totalDurationSeconds: Long
    ) {
        viewModelScope.launch {
            repository.saveWatchHistory(
                movieSlug = movieSlug,
                movieName = movieName,
                posterUrl = posterUrl,
                episodeName = episodeName,
                episodeSlug = episodeSlug,
                lastTimeSeconds = lastTimeSeconds,
                totalDurationSeconds = totalDurationSeconds
            )
        }
    }

    fun removeHistory(movieSlug: String) {
        viewModelScope.launch {
            repository.removeWatchHistory(movieSlug)
        }
    }

    fun getWatchHistoryForMovie(movieSlug: String): Flow<WatchHistoryEntity?> {
        return repository.getWatchHistoryForMovie(movieSlug)
    }

    // Factory Class
    class Factory(private val repository: MovieRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(MovieViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return MovieViewModel(repository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
