package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface MovieDao {
    // Bookmarks
    @Query("SELECT * FROM bookmarked_movies ORDER BY timestamp DESC")
    fun getAllBookmarks(): Flow<List<BookmarkedMovieEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBookmark(bookmark: BookmarkedMovieEntity)

    @Query("DELETE FROM bookmarked_movies WHERE id = :id")
    suspend fun deleteBookmarkById(id: String)

    @Query("SELECT EXISTS(SELECT * FROM bookmarked_movies WHERE id = :id)")
    fun isBookmarked(id: String): Flow<Boolean>

    // Watch History
    @Query("SELECT * FROM watch_history ORDER BY timestamp DESC")
    fun getAllWatchHistory(): Flow<List<WatchHistoryEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWatchHistory(history: WatchHistoryEntity)

    @Query("DELETE FROM watch_history WHERE movieSlug = :movieSlug")
    suspend fun deleteWatchHistoryBySlug(movieSlug: String)

    @Query("SELECT * FROM watch_history WHERE movieSlug = :movieSlug")
    fun getWatchHistoryBySlug(movieSlug: String): Flow<WatchHistoryEntity?>
}
