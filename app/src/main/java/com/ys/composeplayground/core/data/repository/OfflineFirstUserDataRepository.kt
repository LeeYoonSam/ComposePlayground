///*
// * Copyright 2022 The Android Open Source Project
// *
// * Licensed under the Apache License, Version 2.0 (the "License");
// * you may not use this file except in compliance with the License.
// * You may obtain a copy of the License at
// *
// *     https://www.apache.org/licenses/LICENSE-2.0
// *
// * Unless required by applicable law or agreed to in writing, software
// * distributed under the License is distributed on an "AS IS" BASIS,
// * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// * See the License for the specific language governing permissions and
// * limitations under the License.
// */
//
//package com.ys.composeplayground.core.data.repository
//
//import com.ys.composeplayground.core.datastore.CpPreferencesDataSource
//import com.ys.composeplayground.core.model.data.DarkThemeConfig
//import com.ys.composeplayground.core.model.data.ThemeBrand
//import com.ys.composeplayground.core.model.data.UserData
//import kotlinx.coroutines.flow.Flow
//import javax.inject.Inject
//
//internal class OfflineFirstUserDataRepository @Inject constructor(
//    private val niaPreferencesDataSource: CpPreferencesDataSource,
//) : UserDataRepository {
//
//    override val userData: Flow<UserData> =
//        niaPreferencesDataSource.userData
//
//
//    override suspend fun setThemeBrand(themeBrand: ThemeBrand) {
//        niaPreferencesDataSource.setThemeBrand(themeBrand)
//    }
//
//    override suspend fun setDarkThemeConfig(darkThemeConfig: DarkThemeConfig) {
//        niaPreferencesDataSource.setDarkThemeConfig(darkThemeConfig)
//    }
//
//    override suspend fun setDynamicColorPreference(useDynamicColor: Boolean) {
//        niaPreferencesDataSource.setDynamicColorPreference(useDynamicColor)
//    }
//}
