package com.example.data.repository

import com.example.data.local.BookmarkedMovieEntity
import com.example.data.local.MovieDao
import com.example.data.local.WatchHistoryEntity
import com.example.data.models.Movie
import com.example.data.remote.MockMovieData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

class MovieRepository(private val movieDao: MovieDao) {

    // Remote/Mock Movie Data accessors
    fun getMoviesList(): List<Movie> = MockMovieData.movies

    fun getMovieBySlug(slug: String): Movie? {
        return MockMovieData.movies.firstOrNull { it.slug == slug }
    }

    fun searchMovies(
        keyword: String,
        category: String? = null,
        country: String? = null,
        type: String? = null,
        year: Int? = null
    ): List<Movie> {
        return MockMovieData.movies.filter { movie ->
            val matchKeyword = keyword.isEmpty() || 
                    movie.name.contains(keyword, ignoreCase = true) || 
                    movie.originName.contains(keyword, ignoreCase = true) || 
                    movie.content.contains(keyword, ignoreCase = true)

            val matchCategory = category == null || movie.categories.contains(category)
            val matchCountry = country == null || movie.countries.contains(country)
            val matchType = type == null || movie.type.equals(type, ignoreCase = true)
            val matchYear = year == null || movie.year == year

            matchKeyword && matchCategory && matchCountry && matchType && matchYear
        }
    }

    fun getTrendingMovies(): List<Movie> {
        // Return first 3 movies as trending
        return MockMovieData.movies.take(3)
    }

    // Local DB Bookmarks
    val allBookmarks: Flow<List<BookmarkedMovieEntity>> = movieDao.getAllBookmarks()

    suspend fun addBookmark(movie: Movie) {
        val entity = BookmarkedMovieEntity(
            id = movie.id,
            name = movie.name,
            originName = movie.originName,
            slug = movie.slug,
            posterUrl = movie.posterUrl,
            type = movie.type,
            year = movie.year,
            quality = movie.quality,
            categoriesString = movie.categories.joinToString(",")
        )
        movieDao.insertBookmark(entity)
    }

    suspend fun removeBookmark(movieId: String) {
        movieDao.deleteBookmarkById(movieId)
    }

    fun isBookmarked(movieId: String): Flow<Boolean> {
        return movieDao.isBookmarked(movieId)
    }

    // Local DB Watch History
    val allWatchHistory: Flow<List<WatchHistoryEntity>> = movieDao.getAllWatchHistory()

    suspend fun saveWatchHistory(
        movieSlug: String,
        movieName: String,
        posterUrl: String,
        episodeName: String,
        episodeSlug: String,
        lastTimeSeconds: Long,
        totalDurationSeconds: Long
    ) {
        val entity = WatchHistoryEntity(
            movieSlug = movieSlug,
            movieName = movieName,
            posterUrl = posterUrl,
            episodeName = episodeName,
            episodeSlug = episodeSlug,
            lastTimeSeconds = lastTimeSeconds,
            totalDurationSeconds = totalDurationSeconds,
            timestamp = System.currentTimeMillis()
        )
        movieDao.insertWatchHistory(entity)
    }

    suspend fun removeWatchHistory(movieSlug: String) {
        movieDao.deleteWatchHistoryBySlug(movieSlug)
    }

    fun getWatchHistoryForMovie(movieSlug: String): Flow<WatchHistoryEntity?> {
        return movieDao.getWatchHistoryBySlug(movieSlug)
    }
}
