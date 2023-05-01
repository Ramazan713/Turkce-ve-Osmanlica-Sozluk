package com.masterplus.trdictionary.core.presentation.features.premium

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.masterplus.trdictionary.core.domain.model.premium.SubsOffer
import com.masterplus.trdictionary.core.domain.model.premium.SubsProduct
import com.masterplus.trdictionary.R

@Composable
fun PremiumItem(
    subsProduct: SubsProduct,
    onClicked: (SubsOffer)->Unit,
    modifier: Modifier = Modifier
){
    val shape = MaterialTheme.shapes.medium
    val mainPriceInfo = subsProduct.mainSubsOffer.firstNonFree!!
    val freeSubsOffer = subsProduct.freeTrialSubsOffer
    val context = LocalContext.current

    val priceLabel by remember {
        derivedStateOf { freeSubsOffer?.firstNonFree?.priceLabel ?: mainPriceInfo.priceLabel }
    }

    val freeLabel by remember {
        derivedStateOf {
            freeSubsOffer?.free?.period?.let { period->
                context.getString(R.string.n_free_trial_premium,period.period.toString(),
                    period.shortName.asString(context))
            }
        }
    }

    Column (
        modifier = modifier
            .padding(1.dp)
            .clip(shape)
            .background(MaterialTheme.colorScheme.primaryContainer,shape)
            .border(Dp.Hairline,MaterialTheme.colorScheme.outline,shape)
            .clickable { onClicked(freeSubsOffer ?: subsProduct.mainSubsOffer) }
            .padding(bottom = 7.dp, top = 5.dp)
        ,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            mainPriceInfo.period.title.asString(),
            style = MaterialTheme.typography.titleLarge,
        )
        Spacer(Modifier.height(5.dp))
        Text(
            priceLabel,
            style = MaterialTheme.typography.bodyLarge,
        )
        if(freeLabel!=null){
            Spacer(Modifier.height(5.dp))
            Text(
                freeLabel!!,
                style = MaterialTheme.typography.bodyMedium,
            )
        }

    }
}

