package com.geg.greencar.adcar

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.geg.greencar.R
import com.geg.greencar.common.CacheCar
import com.geg.greencar.common.LogGreenCar
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.appopen.AppOpenAd
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdView

/**
 * Date：2022/9/14
 * Describe:
 */
object AdUtilsCar {
    private val impl by lazy { GoogleAdImpl() }

    fun loadAd(space: SpaceCar, loadFailed: () -> Unit = {}) {
        if (CacheCar.isAdUpperLimit()) {
            return
        }
        if (space.isLoading) {
            return
        }
        if (space.adWrapCar.ad != null) {
            addLogi("have ad cache==  ${space.carName}")
            return
        }
        val iterator = CacheCar.getSpaceAdcConfigureList(space).iterator()
        if (iterator.hasNext()) {
            space.isLoading = true
            load(space, iterator, loadFailed)
        } else {
            addLoge("no find ad configure ${space.carName}")
        }
    }

    private fun load(
        space: SpaceCar,
        iterator: Iterator<AdConfigureBeanCar>,
        loadFailed: () -> Unit
    ) {
        val con = iterator.next()
        addLogi("load ad ${space.carName} --${con.green_c_pro}---${con.green_c_id}")
        impl.loadAd(con.green_c_t, con.green_c_id, {
            addLoge("load ad failed ${space.carName} --${con.green_c_pro}---${con.green_c_id}")
            if (iterator.hasNext()) {
                load(space, iterator, loadFailed)
            } else {
                space.isLoading = false
                loadFailed.invoke()

            }
        }, {
            addLogi("load ad success ${space.carName} --${con.green_c_pro}---${con.green_c_id}")
            space.isLoading = false
            space.adWrapCar.ad = it
        })
    }

    private fun addLogi(msg: String) {
        LogGreenCar.googleI(msg)
    }

    private fun addLoge(msg: String) {
        LogGreenCar.googleE(msg)
    }

    fun showAd(space: SpaceCar, activity: Activity, adClose: () -> Unit): Boolean {
        val call = object : FullScreenContentCallback() {
            override fun onAdDismissedFullScreenContent() {
                super.onAdDismissedFullScreenContent()
                adClose.invoke()
            }

            override fun onAdFailedToShowFullScreenContent(p0: AdError) {
                super.onAdFailedToShowFullScreenContent(p0)
                adClose.invoke()
            }

            override fun onAdClicked() {
                super.onAdClicked()
                CacheCar.addClickNum()
            }

            override fun onAdShowedFullScreenContent() {
                super.onAdShowedFullScreenContent()
                CacheCar.addShowNum()
            }
        }
        when (val ad = space.adWrapCar.ad) {
            is AppOpenAd -> {
                ad.fullScreenContentCallback = call
                ad.show(activity)
            }
            is InterstitialAd -> {
                ad.fullScreenContentCallback = call
                ad.show(activity)
            }
            else -> return false
        }
        space.adWrapCar.ad = null
        addLogi("show ad--> ${space.carName}")
        return true
    }

    fun getNativeView(
        space: SpaceCar,
        layoutInflater: LayoutInflater,
        layoutId: Int
    ): NativeAdView? {
        val nativeAd = space.adWrapCar.ad
        if (nativeAd is NativeAd) {
            val adView = layoutInflater.inflate(layoutId, null) as NativeAdView
            adView.mediaView = adView.findViewById(R.id.ad_media)
            adView.headlineView = adView.findViewById(R.id.ad_headline)
            adView.callToActionView = adView.findViewById(R.id.ad_call_to_action)
            adView.iconView = adView.findViewById(R.id.ad_app_icon)
            adView.bodyView = adView.findViewById(R.id.ad_body)
            (adView.headlineView as TextView).text = nativeAd.headline
            adView.mediaView.setMediaContent(nativeAd.mediaContent)
            if (nativeAd.body == null) {
                adView.bodyView.visibility = View.INVISIBLE
            } else {
                adView.bodyView.visibility = View.VISIBLE
                (adView.bodyView as TextView).text = nativeAd.body
            }
            if (nativeAd.callToAction == null) {
                adView.callToActionView.visibility = View.INVISIBLE
            } else {
                adView.callToActionView.visibility = View.VISIBLE
                (adView.callToActionView as TextView).text = nativeAd.callToAction
            }
            if (nativeAd.icon == null) {
                adView.iconView.visibility = View.INVISIBLE
            } else {
                (adView.iconView as ImageView).setImageDrawable(
                    nativeAd.icon.drawable
                )
                adView.iconView.visibility = View.VISIBLE
            }
            adView.setNativeAd(nativeAd)
            addLogi("show ad--> ${space.carName}")
            space.adWrapCar.ad = null
            CacheCar.addShowNum()
            return adView
        } else {
            return null
        }
    }
}