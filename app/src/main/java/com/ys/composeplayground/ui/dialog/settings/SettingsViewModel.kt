package com.ys.composeplayground.ui.dialog.settings

import androidx.lifecycle.ViewModel
import com.ys.composeplayground.core.data.repository.UserDataRepository
import com.ys.composeplayground.core.model.data.DarkThemeConfig
import com.ys.composeplayground.core.model.data.ThemeBrand
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
//    private val userDataRepository: UserDataRepository,
) : ViewModel() {

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