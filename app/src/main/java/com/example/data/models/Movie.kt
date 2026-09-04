package com.example.data.models

import kotlinx.serialization.Serializable

@Serializable
data class Movie(
    val id: String,
    val name: String,
    val originName: String,
    val slug: String,
    val content: String,
    val type: String, // "single" (phim lẻ) or "series" (phim bộ)
    val status: String, // "completed" or "ongoing"
    val thumbUrl: String,
    val posterUrl: String,
    val year: Int,
    val quality: String, // "FHD", "HD", "UHD"
    val lang: String, // "Vietsub", "Thuyết Minh"
    val duration: String,
    val episodeCurrent: String,
    val ratingImdb: Double,
    val ratingTmdb: Double,
    val categories: List<String>,
    val countries: List<String>,
    val actors: List<String>,
    val directors: List<String>,
    val servers: List<MovieServer> = emptyList()
)

@Serializable
data class MovieServer(
    val serverName: String,
    val episodes: List<Episode>
)

@Serializable
data class Episode(
    val name: String,
    val slug: String,
    val m3u8Url: String
)
