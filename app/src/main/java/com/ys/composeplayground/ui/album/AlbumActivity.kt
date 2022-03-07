package com.ys.composeplayground.ui.album

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.net.Uri
import android.os.Bundle
import android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.ys.composeplayground.ui.album.data.MediaStoreImage
import com.ys.composeplayground.ui.album.data.ProviderMediaStoreImage
import com.ys.composeplayground.ui.album.data.queryImages
import com.ys.composeplayground.ui.theme.ComposePlaygroundTheme
import kotlinx.coroutines.*

class AlbumActivity : ComponentActivity() {

    private val mediaList = mutableStateListOf<MediaStoreImage>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            ComposePlaygroundTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    AlbumScreen(mediaList)
                }
            }
        }

        openMediaStore()
    }

    private fun openMediaStore() {
        if (haveStoragePermission()) {
            showImages()
        } else {
            requestPermission()
        }
    }

    private fun goToSettings() {
        Intent(ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:$packageName")).apply {
            addCategory(Intent.CATEGORY_DEFAULT)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }.also { intent ->
            startActivity(intent)
        }
    }

    private fun haveStoragePermission() =
        ContextCompat.checkSelfPermission(
            this, Manifest.permission.READ_EXTERNAL_STORAGE
        ) == PERMISSION_GRANTED

    private fun requestPermission() {
        if (!haveStoragePermission()) {
            val permissions = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
            ActivityCompat.requestPermissions(this, permissions, READ_EXTERNAL_STORAGE_REQUEST)
        }
    }

    private fun showImages() {
        GlobalScope.launch {
            val images = async(Dispatchers.Default) {
                queryImages(contentResolver = contentResolver)
            }.await()

            mediaList.addAll(images)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            READ_EXTERNAL_STORAGE_REQUEST -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PERMISSION_GRANTED) {
                    showImages()
                } else {
                    val showRationale =
                        ActivityCompat.shouldShowRequestPermissionRationale(
                            this,
                            Manifest.permission.READ_EXTERNAL_STORAGE
                        )

                    if (!showRationale) {
                        goToSettings()
                    }
                }
                return
            }
        }
    }

    companion object {
        private const val READ_EXTERNAL_STORAGE_REQUEST = 0x1045

        fun newIntent(context: Context) = Intent(context, AlbumActivity::class.java)
    }
}

