package com.masterplus.trdictionary.core.presentation.components

import android.os.Parcelable
import android.util.Log
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import com.masterplus.trdictionary.R
import com.masterplus.trdictionary.core.domain.enums.IMenuItemEnum
import com.masterplus.trdictionary.core.presentation.selectors.CustomDropdownBarMenu
import com.masterplus.trdictionary.core.presentation.selectors.SelectMenuItemBottomContent
import kotlinx.parcelize.Parcelize

@Parcelize
data class AdaptiveSelectMenuData<T> (
    val items: List<T>,
    val onItemChange: ((T)->Unit)?,
    val sheetTitle: String?
): Parcelable where T : IMenuItemEnum, T: Enum<T>



@Composable
fun <T> rememberAdaptiveSelectMenuState(
    windowWidthSizeClass: WindowWidthSizeClass,
    hideSheetAfterMenuClick: Boolean = true
): AdaptiveSelectMenuState<T> where T : IMenuItemEnum, T: Enum<T>{

    var visibleSheet by rememberSaveable {
        mutableStateOf(false)
    }

    val isExpanded by remember(windowWidthSizeClass) {
        derivedStateOf {
            windowWidthSizeClass == WindowWidthSizeClass.Expanded
        }
    }

    var data by rememberSaveable {
        mutableStateOf<AdaptiveSelectMenuData<T>?>(null)
    }

    val handler = remember(isExpanded,visibleSheet,data,hideSheetAfterMenuClick) {
        AdaptiveSelectMenuState<T>(
            isExpanded = isExpanded,
            visibleSheet = visibleSheet,
            onVisibleSheet = { visibleSheet = it },
            data = data,
            setData = { data = it },
            hideSheetAfterMenuClick = hideSheetAfterMenuClick
        )
    }
    return handler
}

data class AdaptiveSelectMenuState<T>(
    private val hideSheetAfterMenuClick: Boolean,
    val isExpanded: Boolean,
    private val visibleSheet: Boolean,
    private val onVisibleSheet: (Boolean) -> Unit,
    private val data: AdaptiveSelectMenuData<T>?,
    private val setData: (AdaptiveSelectMenuData<T>?) -> Unit
) where T : IMenuItemEnum, T: Enum<T>{

    fun setSheetData(
        data: AdaptiveSelectMenuData<T>,
    ){
        onVisibleSheet(true)
        setData(data)
    }

    @OptIn(ExperimentalMaterial3Api::class)
    val showSheet: @Composable() () -> Unit  @Composable get() {
        return {
            if(visibleSheet){
                data?.let { data->
                    CustomModalBottomSheet(
                        onDismissRequest = { onVisibleSheet(false) },
                        skipHalfExpanded = true
                    ){
                        SelectMenuItemBottomContent<T>(
                            title = data.sheetTitle,
                            items = data.items,
                            onClose = { onVisibleSheet(false) },
                            onClickItem = {selected->
                                data.onItemChange?.let {onItemChange->
                                    onItemChange(selected)
                                    if(hideSheetAfterMenuClick){
                                        onVisibleSheet(false)
                                    }
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}





@Composable
fun <T> AdaptiveSelectSheetMenu(
    modifier: Modifier = Modifier,
    state: AdaptiveSelectMenuState<T>,
    onItemChange: ((T)->Unit)? = null,
    items: List<T>,
    sheetTitle: String? = null,
    icon: ImageVector = Icons.Default.MoreVert,
    contentDescription: String? = stringResource(id = R.string.menu),
    tooltip: String? = contentDescription
) where T : IMenuItemEnum, T: Enum<T> {

    var visibleDropdown by rememberSaveable {
        mutableStateOf(false)
    }

    LaunchedEffect(state.isExpanded){
        if(!state.isExpanded && visibleDropdown){
            visibleDropdown = false
        }
    }

    if(state.isExpanded){
        CustomDropdownBarMenu(
            tooltip = tooltip,
            modifier = modifier,
            items = items,
            onItemChange = onItemChange,
            expanded = visibleDropdown,
            onExpandedChange = { visibleDropdown = it },
            icon = icon,
            contentDescription = contentDescription
        )
    }else{
        DefaultToolTip(tooltip = tooltip) {
            IconButton(
                modifier = modifier,
                onClick = {
                    state.setSheetData(AdaptiveSelectMenuData(items,onItemChange, sheetTitle))
                }
            ){
                Icon(icon, contentDescription = contentDescription)
            }
        }
    }
}