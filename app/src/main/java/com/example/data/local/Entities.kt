package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "bookmarked_movies")
data class BookmarkedMovieEntity(
    @PrimaryKey val id: String,
    val name: String,
    val originName: String,
    val slug: String,
    val posterUrl: String,
    val type: String,
    val year: Int,
    val quality: String,
    val categoriesString: String, // Comma separated
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "watch_history")
data class WatchHistoryEntity(
    @PrimaryKey val movieSlug: String,
    val movieName: String,
    val posterUrl: String,
    val episodeName: String,
    val episodeSlug: String,
    val lastTimeSeconds: Long,
    val totalDurationSeconds: Long,
    val timestamp: Long = System.currentTimeMillis()
)
