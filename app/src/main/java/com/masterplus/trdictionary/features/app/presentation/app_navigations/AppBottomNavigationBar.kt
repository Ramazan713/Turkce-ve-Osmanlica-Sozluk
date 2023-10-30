package com.masterplus.trdictionary.features.app.presentation.app_navigations

import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.masterplus.trdictionary.features.app.domain.model.AppNavRoute
import com.masterplus.trdictionary.features.app.domain.model.kAppNavRoutes

@Composable
fun AppBottomNavigationBar(
    currentDestination: AppNavRoute?,
    onDestinationChange: (AppNavRoute) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    BottomAppBar(
        modifier = modifier,
    ) {
        kAppNavRoutes.forEach { route->
            val title = route.title.asString(context)
            val selected = currentDestination == route

            NavigationBarItem(
                selected = selected,
                onClick = { onDestinationChange(route) },
                icon = { Icon(imageVector = route.getCurrentImageVector(selected), contentDescription = title) },
                label = { Text(text = title) }
            )
        }
    }

}