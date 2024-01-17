//package com.ys.composeplayground.core.datastore.di
//
//import android.content.Context
//import androidx.datastore.core.DataStore
//import androidx.datastore.core.DataStoreFactory
//import androidx.datastore.dataStoreFile
//import com.ys.composeplayground.core.datastore.UserPreferencesSerializer
//import kotlinx.coroutines.CoroutineDispatcher
//import kotlinx.coroutines.CoroutineScope
//
//@Module
//@InstallIn(SingletonComponent::class)
//object DataStoreModule {
//
//    @Provides
//    @Singleton
//    internal fun providesUserPreferencesDataStore(
//        @ApplicationContext context: Context,
//        @Dispatcher(IO) ioDispatcher: CoroutineDispatcher,
//        @ApplicationScope scope: CoroutineScope,
//        userPreferencesSerializer: UserPreferencesSerializer,
//    ): DataStore<UserPreferences> =
//        DataStoreFactory.create(
//            serializer = userPreferencesSerializer,
//            scope = CoroutineScope(scope.coroutineContext + ioDispatcher),
//            migrations = listOf(
//                IntToStringIdsMigration,
//            ),
//        ) {
//            context.dataStoreFile("user_preferences.pb")
//        }
//}
