package com.ys.composeplayground.ui.navigation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Create
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.unit.dp
import com.ys.composeplayground.ui.theme.ComposePlaygroundTheme

/**
 * Bottom Navigation Animation in Jetpack Compose
 * https://medium.com/@sinasamaki/bottom-navigation-animation-in-jetpack-compose-3639a89f099e
 *
 * jetpack compose 라이브러리에서 제공되는 애니메이션 구성 요소만 사용하여 보다 맞춤화된 하단 탐색 UI를 구현할 수 있습니다.
 */
class BottomNavigationAnimationActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposePlaygroundTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    BottomNavNoAnimation(
                        screens = listOf(Screen.Home, Screen.Create, Screen.Profile, Screen.Settings)
                    )
                }
            }
        }
    }
    
    @OptIn(ExperimentalAnimationApi::class)
    @Composable
    fun BottomNavNoAnimation(
        screens: List<Screen>
    ) {
        var selectedScreen by remember { mutableStateOf(0) }

        Box(
            Modifier
                .shadow(5.dp)
                .background(color = MaterialTheme.colors.surface)
                .height(64.dp)
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            Row(
                Modifier.fillMaxSize(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                for (screen in screens) {
                    val isSelected = screen == screens[selectedScreen]
                    
                    Box(
                        modifier = Modifier.weight(if (isSelected) 1.5f else 1f),
                        contentAlignment = Alignment.Center
                    ) {
                        val interactionSource = remember { MutableInteractionSource() }
                        BottomNavItem(
                            modifier = Modifier.clickable(
                                interactionSource = interactionSource,
                                indication = null
                            ) {
                                selectedScreen = screens.indexOf(screen)
                            },
                            screen = screen,
                            isSelected = isSelected
                        )
                    }
                }
            }
        }
    }

    @ExperimentalAnimationApi
    @Composable
    fun BottomNavItem(
        modifier: Modifier,
        screen: Screen,
        isSelected: Boolean
    ) {
        val animatedHeight by animateDpAsState(targetValue = if (isSelected) 36.dp else 26.dp)
        val animatedElevation by animateDpAsState(targetValue = if (isSelected) 15.dp else 0.dp)
        val animatedAlpha by animateFloatAsState(targetValue = if (isSelected) 1f else .5f)
        val animatedIconSize by animateDpAsState(
            targetValue = if (isSelected) 26.dp else 20.dp,
            animationSpec = spring(
                stiffness = Spring.StiffnessLow,
                dampingRatio = Spring.DampingRatioMediumBouncy
            )
        )

        Box(
            modifier = modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Row(
                modifier = Modifier
                    .height(animatedHeight)
                    .shadow(
                        elevation = animatedElevation,
                        shape = RoundedCornerShape(20.dp)
                    ),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {

                FlipIcon(
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .fillMaxHeight()
                        .padding(start = 11.dp)
                        .alpha(animatedAlpha)
                        .size(animatedIconSize),
                    isActive = isSelected,
                    activeIcon = screen.activeIcon,
                    inactiveIcon = screen.inactiveIcon,
                    contentDescription = screen.title
                )

                AnimatedVisibility(visible = isSelected) {
                    Text(
                        text = screen.title,
                        modifier = Modifier.padding(start = 8.dp, end = 10.dp),
                        maxLines = 1
                    )
                }
            }
        }
    }

    @Composable
    fun FlipIcon(
        modifier: Modifier = Modifier,
        isActive: Boolean,
        activeIcon: ImageVector,
        inactiveIcon: ImageVector,
        contentDescription: String,
    ) {
        val animationRotation by animateFloatAsState(
            targetValue = if (isActive) 180f else 0f,
            animationSpec = spring(
                stiffness = Spring.StiffnessLow,
                dampingRatio = Spring.DampingRatioMediumBouncy
            )
        )
        
        Box(
            modifier = modifier.graphicsLayer { rotationY = animationRotation },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                rememberVectorPainter(image = if (animationRotation > 90f) activeIcon else inactiveIcon),
                contentDescription = contentDescription,
            )
        }
    }

    sealed class Screen(
        val title: String,
        val activeIcon: ImageVector,
        val inactiveIcon: ImageVector
    ) {
        object Home: Screen("Home", Icons.Filled.Home, Icons.Outlined.Home)
        object Create: Screen("Create", Icons.Filled.Create, Icons.Outlined.Create)
        object Profile: Screen("Profile", Icons.Filled.Person, Icons.Outlined.Person)
        object Settings: Screen("Settings", Icons.Filled.Settings, Icons.Outlined.Settings)
    }

    companion object {
        fun newIntent(context: Context) = Intent(context, BottomNavigationAnimationActivity::class.java)
    }
}