package com.masterplus.trdictionary.core.shared_features.premium

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.masterplus.trdictionary.core.domain.model.premium.PremiumProduct
import com.masterplus.trdictionary.core.presentation.dialog_body.CustomDialog
import com.masterplus.trdictionary.R
import com.masterplus.trdictionary.core.presentation.components.DialogHeader


@Composable
fun ShowPremiumProductDialog(
    premiumProduct: PremiumProduct?,
    onClosed: () -> Unit,
    windowWidthSizeClass: WindowWidthSizeClass,
    onProductClicked: (PremiumProduct, String) -> Unit
){
    CustomDialog(
        onClosed = onClosed,
        adaptiveWidthSizeClass = windowWidthSizeClass
    ){
        Column(
            modifier = Modifier
                .padding(vertical = 5.dp)
                .padding(bottom = 17.dp)
        ) {
            DialogHeader(
                title = stringResource(R.string.premium),
                onIconClick = onClosed,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            )
            LazyColumn(
                modifier = Modifier
                    .padding(horizontal = 9.dp)
            ) {

                item {
                    Text(
                        stringResource(R.string.features),
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.W600),
                        modifier = Modifier.padding(vertical = 5.dp)
                    )
                    PremiumFeature(
                        title = stringResource(R.string.ad_free)
                    )
                    Spacer(Modifier.height(24.dp))
                }

                if (premiumProduct != null) {
                    items(premiumProduct.subProducts) { subsProduct ->
                        PremiumItem(
                            subsProduct,
                            onClicked = { subsOffer ->
                                onProductClicked(premiumProduct, subsOffer.offerToken)
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 3.dp),
                        )
                    }
                }

                item {
                    Spacer(Modifier.height(32.dp))

                    Column(
                        verticalArrangement = Arrangement.spacedBy(5.dp)
                    ) {
                        Text(
                            stringResource(R.string.premium_warning_dia_1),
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Text(
                            stringResource(R.string.premium_warning_dia_2),
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Text(
                            stringResource(R.string.premium_warning_dia_3),
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Text(
                            stringResource(R.string.premium_warning_dia_4),
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }
        }
    }
}