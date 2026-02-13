package com.ys.composeplayground.ui.commerce

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ys.composeplayground.ui.theme.ComposePlaygroundTheme

/**
 * 상품 그리드
 * - LazyVerticalGrid, 2열
 * - 상품 카드 목록
 */
@Composable
fun ProductGrid(
    products: List<SaleProduct>,
    onProductClick: (SaleProduct) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = modifier.fillMaxWidth(),
        contentPadding = PaddingValues(16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        items(
            items = products,
            key = { product -> product.id }
        ) { product ->
            ProductCard(
                product = product,
                onProductClick = { onProductClick(product) }
            )
        }
    }
}

@Preview
@Composable
fun PreviewProductGrid() {
    ComposePlaygroundTheme {
        ProductGrid(
            products = saleProducts.take(4),
            onProductClick = {}
        )
    }
}
