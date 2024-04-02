package com.ys.composeplayground.ui.sample.product

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import com.ys.composeplayground.ui.sample.product.detail.ProductDetailScreen
import com.ys.composeplayground.ui.theme.ComposePlaygroundTheme

class ProductDetailMainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            ComposePlaygroundTheme {
                ProductDetailScreen()
            }
        }
    }
}