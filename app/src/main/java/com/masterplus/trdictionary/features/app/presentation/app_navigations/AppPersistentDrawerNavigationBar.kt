package com.masterplus.trdictionary.features.app.presentation.app_navigations

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MenuOpen
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.NavigationRailItemDefaults
import androidx.compose.material3.PermanentDrawerSheet
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.masterplus.trdictionary.R
import com.masterplus.trdictionary.features.app.domain.model.AppNavRoute
import com.masterplus.trdictionary.features.app.domain.model.kAppNavRoutes
import com.masterplus.trdictionary.features.list.presentation.show_list.ShowListDialogEvent
import com.masterplus.trdictionary.features.list.presentation.show_list.ShowListEvent
import com.masterplus.trdictionary.features.list.presentation.show_list.ShowListViewModel

@Composable
fun AppPersistentDrawerNavigationBar(
    currentDestination: AppNavRoute?,
    onDestinationChange: (AppNavRoute) -> Unit,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current

    PermanentDrawerSheet(
        modifier = modifier
            .padding(horizontal = 4.dp),
    ) {
         Row(
            modifier = Modifier
                .padding(start = 8.dp, top = 16.dp, end = 2.dp)
                .fillMaxWidth(),
             horizontalArrangement = Arrangement.spacedBy(8.dp),
             verticalAlignment = Alignment.CenterVertically
        ) {
             Image(
                 painter = painterResource(id = R.drawable.ic_launcher),
                 contentDescription = "app_image",
                 modifier = Modifier
                     .clip(CircleShape)
                     .size(75.dp),
                 contentScale = ContentScale.Crop
             )

             Text(
                text = stringResource(id = R.string.app_name),
                style = MaterialTheme.typography.titleLarge
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
                    onClick = { onDestinationChange(route) },
                    icon = { Icon(imageVector = route.getCurrentImageVector(selected), contentDescription = title) },
                    label = { Text(text = title) }
                )
            }
        }

        Spacer(modifier = Modifier.weight(1f))
    }
}