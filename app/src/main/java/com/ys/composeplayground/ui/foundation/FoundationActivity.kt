package com.ys.composeplayground.ui.foundation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import com.ys.composeplayground.ui.theme.ComposePlaygroundTheme

class FoundationActivity : AppCompatActivity() {

    private val index by lazy { intent.getIntExtra(INTENT_KEY_INDEX, 0) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposePlaygroundTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    when (index) {
                        0 -> BaseTextField()
                        1 -> CanvasDrawExample()
                        2 -> ImageResourceDemo()
                        3 -> LazyColumnDemo()
                        4 -> LazyRowDemo()
                        5 -> LazyVerticalGridDemo()
                        6 -> LazyVerticalGridDemo()
                    }
                }
            }
        }
    }

    companion object {
        private val INTENT_KEY_INDEX = "intent_key_index"

        fun newIntent(context: Context, index: Int) = Intent(context, FoundationActivity::class.java).apply {
            putExtra(INTENT_KEY_INDEX, index)
        }
    }
}