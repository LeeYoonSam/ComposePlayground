package com.ys.composeplayground.ui.album.data

data class Album(
    val id: Int,
    val picturePath: String
)

fun getAlbumSample() = listOf<Album>(
    Album(1, "path1"),
    Album(2, "path2"),
    Album(3, "path3"),
    Album(4, "path4"),
    Album(5, "path5"),
    Album(6, "path6"),
    Album(7, "path7"),
    Album(8, "path8"),
    Album(9, "path9"),
    Album(10, "path10")
)