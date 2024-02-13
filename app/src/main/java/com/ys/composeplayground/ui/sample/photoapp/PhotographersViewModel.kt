package com.ys.composeplayground.ui.sample.photoapp

import androidx.lifecycle.ViewModel
import com.ys.composeplayground.R
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class PhotographersViewModel : ViewModel() {
    private val _photographers = MutableStateFlow<List<Photographer>>(emptyList())
    val photographers: StateFlow<List<Photographer>> = _photographers

    fun getById(id: String) = photographers.value.first { it.id == id }

    init {
        _photographers.value = mutableListOf(
            Photographer(
                id = "id1",
                name = "Patricia Stevenson",
                lastSeenOnline = "3 minutes ago",
                avatar = R.drawable.ava1,
                mainImage = R.drawable.image1,
                numOfFollowers = "135K",
                numOfFollowing = "2.6K",
                tags = listOf("food", "urban", "city", "people", "style", "fashion", "environment"),
                photos = mapOf(
                    "City" to listOf(
                        R.drawable.city_1,
                        R.drawable.city_2,
                        R.drawable.city_3,
                        R.drawable.city_4,
                        R.drawable.city_5,
                        R.drawable.city_6
                    ),
                    "Street Art" to listOf(
                        R.drawable.art_1,
                        R.drawable.art_2,
                        R.drawable.art_3,
                        R.drawable.art_4,
                        R.drawable.art_5,
                        R.drawable.art_6
                    )
                )
            ),
            Photographer(
                id = "id2",
                name = "Diana Glow",
                lastSeenOnline = "10 minutes ago",
                avatar = R.drawable.ava2,
                mainImage = R.drawable.image2,
                numOfFollowers = "18K",
                numOfFollowing = "945",
                tags = listOf("family", "people", "portrait", "nature", "style", "fashion", "environment"),
                photos = mapOf(
                    "Fashion" to listOf(
                        R.drawable.fashion_1,
                        R.drawable.fashion_2,
                        R.drawable.fashion_3,
                        R.drawable.fashion_4,
                        R.drawable.fashion_5,
                        R.drawable.fashion_6
                    ),
                    "Nature" to listOf(
                        R.drawable.nature_1,
                        R.drawable.nature_2,
                        R.drawable.nature_3,
                        R.drawable.nature_4,
                        R.drawable.nature_5,
                        R.drawable.nature_6
                    ),
                    "People" to listOf(
                        R.drawable.people_1,
                        R.drawable.people_2,
                        R.drawable.people_3,
                        R.drawable.people_4,
                        R.drawable.people_5,
                        R.drawable.people_6
                    )
                )
            ),
            Photographer(
                id = "id3",
                name = "Kurt Cobain",
                lastSeenOnline = "26 years ago",
                avatar = R.drawable.ava3,
                mainImage = R.drawable.image3,
                numOfFollowers = "1.9M",
                numOfFollowing = "42",
                tags = listOf("music", "live", "concert", "rock", "metal"),
                photos = mapOf(
                    "Live" to listOf(
                        R.drawable.live_1,
                        R.drawable.live_2,
                        R.drawable.live_3,
                        R.drawable.live_4,
                        R.drawable.live_5,
                        R.drawable.live_6
                    ),
                    "B/W" to listOf(
                        R.drawable.bw_1,
                        R.drawable.bw_2,
                        R.drawable.bw_3,
                        R.drawable.bw_4,
                        R.drawable.bw_5,
                        R.drawable.bw_6
                    )
                )
            )
        )
    }
}