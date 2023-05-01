package com.masterplus.trdictionary.features.app.presentation.ad

import android.app.Activity
import android.content.Context
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.masterplus.trdictionary.BuildConfig

class AdMobInterstitial constructor(
    private val context: Context
) {
    private var mInterstitialAd: InterstitialAd? = null

    fun showAd(){
        if(mInterstitialAd!=null){
            mInterstitialAd?.show(context as Activity)
        }
    }

    fun loadAd(onAdShowed: ()->Unit){
        if(mInterstitialAd!=null) return

        val adRequest = AdRequest.Builder().build()

        InterstitialAd.load(context,BuildConfig.INTERSTITIAL_AD_ID,
            adRequest, object : InterstitialAdLoadCallback() {
                override fun onAdFailedToLoad(adError: LoadAdError) {
                    mInterstitialAd = null
                }

                override fun onAdLoaded(interstitialAd: InterstitialAd) {
                    mInterstitialAd = interstitialAd
                    adCallback {
                        onAdShowed()
                    }
                }
            })
    }

    private fun adCallback(onAdShowed: ()->Unit){
        mInterstitialAd?.fullScreenContentCallback = object: FullScreenContentCallback() {
            override fun onAdDismissedFullScreenContent() {
                mInterstitialAd = null
            }

            override fun onAdFailedToShowFullScreenContent(p0: AdError) {
                mInterstitialAd = null
            }
            override fun onAdShowedFullScreenContent() {
                onAdShowed()
            }
        }
    }
}