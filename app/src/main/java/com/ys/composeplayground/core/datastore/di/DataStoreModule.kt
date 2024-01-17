//package com.ys.composeplayground.core.datastore.di
//
//import android.content.Context
//import androidx.datastore.core.DataStore
//import androidx.datastore.core.DataStoreFactory
//import androidx.datastore.dataStoreFile
//import com.ys.composeplayground.core.common.core.network.CpDispatchers
//import com.ys.composeplayground.core.common.core.network.Dispatcher
//import com.ys.composeplayground.core.common.core.network.di.ApplicationScope
//import com.ys.composeplayground.core.datastore.UserPreferencesSerializer
//import dagger.Module
//import dagger.Provides
//import dagger.hilt.InstallIn
//import dagger.hilt.android.qualifiers.ApplicationContext
//import dagger.hilt.components.SingletonComponent
//import kotlinx.coroutines.CoroutineDispatcher
//import kotlinx.coroutines.CoroutineScope
//import javax.inject.Singleton
//
//@Module
//@InstallIn(SingletonComponent::class)
//object DataStoreModule {
//    @Provides
//    @Singleton
//    internal fun providesUserPreferencesDataStore(
//        @ApplicationContext context: Context,
//        @Dispatcher(CpDispatchers.IO) ioDispatcher: CoroutineDispatcher,
//        @ApplicationScope scope: CoroutineScope,
//        userPreferencesSerializer: UserPreferencesSerializer,
//    ): DataStore<UserPreferences> =
//        DataStoreFactory.create(
//            serializer = userPreferencesSerializer,
//            scope = CoroutineScope(scope.coroutineContext + ioDispatcher),
//        ) {
//            context.dataStoreFile("user_preferences.pb")
//        }
//}
