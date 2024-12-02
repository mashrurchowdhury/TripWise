package com.example.tripwise.ui.components

import BottomNavItem
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.navigation.NavHostController
import androidx.compose.ui.Modifier
import com.example.tripwise.ui.theme.TripWiseGreen

@Composable
fun BottomNavigationBar(
    modifier: Modifier = Modifier,
    navController: NavHostController
) {
    val navItems = listOf(BottomNavItem.Home, BottomNavItem.Maps, BottomNavItem.Settings)
    var selectedItem by rememberSaveable { mutableStateOf(0) }

    Box(modifier = modifier.fillMaxSize()) {
        Column(modifier = modifier.align(Alignment.BottomCenter)) {
            NavigationBar {
                navItems.forEachIndexed { index, item ->
                    NavigationBarItem(
                        alwaysShowLabel = true,
                        icon = { Icon(item.icon, contentDescription = item.label) },
                        label = { Text(item.label) },
                        selected = selectedItem == index,
                        colors = NavigationBarItemDefaults.colors(
                            indicatorColor = TripWiseGreen,
                        ),
                        onClick = {
                            selectedItem = index
                            navController.navigate(item.route) {
                                navController.graph.startDestinationRoute?.let { route ->
                                    popUpTo(route) { saveState = true }
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }
    }
}