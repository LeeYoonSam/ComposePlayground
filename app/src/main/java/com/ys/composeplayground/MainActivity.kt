package com.ys.composeplayground

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ys.composeplayground.extensions.toast
import com.ys.composeplayground.ui.album.AlbumActivity
import com.ys.composeplayground.ui.foundation.FoundationActivity
import com.ys.composeplayground.ui.navigation.BottomNavigationAnimationActivity
import com.ys.composeplayground.ui.theme.ComposePlaygroundTheme

class MainActivity : ComponentActivity() {

    private var backPressed = 0L

    private val finish: () -> Unit = {
        if (backPressed + 1500 > System.currentTimeMillis()) {
            finishAndRemoveTask()
        } else {
            toast(getString(R.string.app_exit_label))
        }
        backPressed = System.currentTimeMillis()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposePlaygroundTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    ComposeList(finish) {
                        routeToActivity(it)
                    }
                }
            }
        }
    }

    private fun routeToActivity(position: Int) {
        when (position) {
            0 -> startActivity(AlbumActivity.newIntent(this))
            1 -> {}
            2 -> startActivity(FoundationActivity.newIntent(this, 0))
            3 -> startActivity(FoundationActivity.newIntent(this, 1))
            4 -> startActivity(FoundationActivity.newIntent(this, 2))
            5 -> startActivity(FoundationActivity.newIntent(this, 3))
            6 -> startActivity(FoundationActivity.newIntent(this, 4))
            7 -> startActivity(FoundationActivity.newIntent(this, 5))
            8 -> startActivity(FoundationActivity.newIntent(this, 6))
            9 -> startActivity(FoundationActivity.newIntent(this, 7))
            10 -> startActivity(BottomNavigationAnimationActivity.newIntent(this))
            else -> {

            }
        }
    }
    @OptIn(ExperimentalMaterialApi::class)
    @Composable
    fun ComposeList(
        finish: () -> Unit,
        navigateToActivity: (Int) -> Unit
    ) {
        val sampleList = getComposeSampleList()

        BackHandler {
            finish()
        }

        LazyColumn(
            modifier = Modifier.fillMaxHeight()
        ) {
            items(
                items = sampleList,
                itemContent = { item ->
                    Card(
                        onClick = { navigateToActivity(item.position) },
                        elevation = 4.dp,
                        modifier = Modifier.padding(8.dp)
                    ) {
                        Text(
                            modifier = Modifier.padding(4.dp),
                            text = item.menuName,
                            style = TextStyle(fontSize = 24.sp)
                        )
                    }
                }
            )
        }
    }

    fun getComposeSampleList() = listOf(
        Menu(0, "Album"),
        Menu(1, "Crossfade"),
        Menu(2, "BaseTextField"),
        Menu(3, "Canvas"),
        Menu(4, "Image"),
        Menu(5, "LazyColumn"),
        Menu(6, "LazyRow"),
        Menu(7, "LazyVerticalGrid"),
        Menu(8, "Shape"),
        Menu(9, "Text"),
        Menu(10, "BottomNavigationAnimation"),
    )

    data class Menu(
        val position: Int,
        val menuName: String
    )

    @Preview(showBackground = true)
    @Composable
    fun DefaultPreview() {
        ComposePlaygroundTheme {
            ComposeList(finish) {}
        }
    }
}