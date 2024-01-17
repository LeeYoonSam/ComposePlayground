package com.ys.composeplayground.core.data.repository.fake

import com.ys.composeplayground.core.data.repository.UserDataRepository
import com.ys.composeplayground.core.model.data.DarkThemeConfig
import com.ys.composeplayground.core.model.data.ThemeBrand
import com.ys.composeplayground.core.model.data.UserData
import kotlinx.coroutines.flow.Flow

class FakeUserDataRepository : UserDataRepository {
    override val userData: Flow<UserData>
        get() = TODO("Not yet implemented")

    override suspend fun setThemeBrand(themeBrand: ThemeBrand) {
        TODO("Not yet implemented")
    }

    override suspend fun setDarkThemeConfig(darkThemeConfig: DarkThemeConfig) {
        TODO("Not yet implemented")
    }

    override suspend fun setDynamicColorPreference(useDynamicColor: Boolean) {
        TODO("Not yet implemented")
    }
}