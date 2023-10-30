package com.masterplus.trdictionary.features.app.presentation.app_navigations

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MenuOpen
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.NavigationRailItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.masterplus.trdictionary.R
import com.masterplus.trdictionary.features.app.domain.model.AppNavRoute
import com.masterplus.trdictionary.features.app.domain.model.kAppNavRoutes

@Composable
fun AppModalDrawerNavigationBar(
    currentDestination: AppNavRoute?,
    onDestinationChange: (AppNavRoute) -> Unit,
    modifier: Modifier = Modifier,
    onDrawerMenuClick: () -> Unit
) {
    val context = LocalContext.current

    ModalDrawerSheet(
        modifier = modifier,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .padding(start = 8.dp, top = 4.dp, end = 2.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = stringResource(id = R.string.app_name),
                style = MaterialTheme.typography.titleLarge
            )
            NavigationRailItem(
                selected = false,
                onClick = onDrawerMenuClick,
                icon = { Icon(imageVector = Icons.Default.MenuOpen, contentDescription = "Menu") },
            )
        }


        Spacer(modifier = Modifier.weight(1f))

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(kAppNavRoutes){ route ->
                val title = route.title.asString(context)
                val selected = currentDestination == route

                NavigationDrawerItem(
                    selected = selected,
                    onClick = {
                        onDestinationChange(route)
                        onDrawerMenuClick()
                    },
                    icon = { Icon(imageVector = route.getCurrentImageVector(selected), contentDescription = title) },
                    label = { Text(text = title) }
                )
            }
        }

        Spacer(modifier = Modifier.weight(1f))
    }
}