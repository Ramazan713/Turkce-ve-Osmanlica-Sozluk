package com.masterplus.trdictionary.features.app.presentation.app_navigations

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.masterplus.trdictionary.features.app.domain.model.AppNavRoute
import com.masterplus.trdictionary.features.app.domain.model.kAppNavRoutes

@Composable
fun AppRailNavigationBar(
    currentDestination: AppNavRoute?,
    onDestinationChange: (AppNavRoute) -> Unit,
    modifier: Modifier = Modifier,
    onDrawerMenuClick: () -> Unit
) {
    val context = LocalContext.current

    NavigationRail(
        modifier = modifier,
    ) {
        NavigationRailItem(
            selected = false,
            onClick = onDrawerMenuClick,
            icon = { Icon(imageVector = Icons.Default.Menu, contentDescription = "Menu") },
        )

        Spacer(modifier = Modifier.weight(1f))

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            items(kAppNavRoutes){route ->
                val title = route.title.asString(context)
                val selected = currentDestination == route

                NavigationRailItem(
                    selected = selected,
                    onClick = { onDestinationChange(route) },
                    icon = { Icon(imageVector = route.getCurrentImageVector(selected), contentDescription = title) },
                    label = { Text(text = title) }
                )
            }
        }

        Spacer(modifier = Modifier.weight(1f))
    }
}