package com.ys.composeplayground.ui.snackbar

data class SnackbarAction(
    val title: String,
    val onActionPress: () -> Unit
)
