package com.ys.composeplayground.ui.dialog.settings

import com.ys.composeplayground.core.model.data.DarkThemeConfig
import com.ys.composeplayground.core.model.data.ThemeBrand

class SettingsViewModel {

}

data class UserEditableSettings(
    val brand: ThemeBrand,
    val useDynamicColor: Boolean,
    val darkThemeConfig: DarkThemeConfig,
)

sealed interface SettingsUiState {
    object Loading : SettingsUiState
    data class Success(val settings: UserEditableSettings) : SettingsUiState
}