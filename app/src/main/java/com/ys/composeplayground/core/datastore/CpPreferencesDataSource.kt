//package com.ys.composeplayground.core.datastore
//
//import androidx.datastore.core.DataStore
//import com.ys.composeplayground.core.model.data.DarkThemeConfig
//import com.ys.composeplayground.core.model.data.ThemeBrand
//import com.ys.composeplayground.core.model.data.UserData
//import kotlinx.coroutines.flow.map
//import javax.inject.Inject
//
//class CpPreferencesDataSource @Inject constructor(
//    private val userPreferences: DataStore<UserPreferences>,
//) {
//    val userData = userPreferences.data
//        .map {
//            UserData(
//                themeBrand = when (it.themeBrand) {
//                    null,
//                    ThemeBrandProto.THEME_BRAND_UNSPECIFIED,
//                    ThemeBrandProto.UNRECOGNIZED,
//                    ThemeBrandProto.THEME_BRAND_DEFAULT,
//                    -> ThemeBrand.DEFAULT
//                    ThemeBrandProto.THEME_BRAND_ANDROID -> ThemeBrand.ANDROID
//                },
//                darkThemeConfig = when (it.darkThemeConfig) {
//                    null,
//                    DarkThemeConfigProto.DARK_THEME_CONFIG_UNSPECIFIED,
//                    DarkThemeConfigProto.UNRECOGNIZED,
//                    DarkThemeConfigProto.DARK_THEME_CONFIG_FOLLOW_SYSTEM,
//                    ->
//                        DarkThemeConfig.FOLLOW_SYSTEM
//                    DarkThemeConfigProto.DARK_THEME_CONFIG_LIGHT ->
//                        DarkThemeConfig.LIGHT
//                    DarkThemeConfigProto.DARK_THEME_CONFIG_DARK -> DarkThemeConfig.DARK
//                },
//                useDynamicColor = it.useDynamicColor,
//            )
//        }
//
//    suspend fun setThemeBrand(themeBrand: ThemeBrand) {
//        userPreferences.updateData {
//            it.copy {
//                this.themeBrand = when (themeBrand) {
//                    ThemeBrand.DEFAULT -> ThemeBrandProto.THEME_BRAND_DEFAULT
//                    ThemeBrand.ANDROID -> ThemeBrandProto.THEME_BRAND_ANDROID
//                }
//            }
//        }
//    }
//
//    suspend fun setDynamicColorPreference(useDynamicColor: Boolean) {
//        userPreferences.updateData {
//            it.copy { this.useDynamicColor = useDynamicColor }
//        }
//    }
//
//    suspend fun setDarkThemeConfig(darkThemeConfig: DarkThemeConfig) {
//        userPreferences.updateData {
//            it.copy {
//                this.darkThemeConfig = when (darkThemeConfig) {
//                    DarkThemeConfig.FOLLOW_SYSTEM ->
//                        DarkThemeConfigProto.DARK_THEME_CONFIG_FOLLOW_SYSTEM
//                    DarkThemeConfig.LIGHT -> DarkThemeConfigProto.DARK_THEME_CONFIG_LIGHT
//                    DarkThemeConfig.DARK -> DarkThemeConfigProto.DARK_THEME_CONFIG_DARK
//                }
//            }
//        }
//    }
//}