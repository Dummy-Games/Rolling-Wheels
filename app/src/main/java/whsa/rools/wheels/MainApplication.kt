package whsa.rools.wheels

import android.app.Application
import com.onesignal.OneSignal

class MainApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        OneSignal.initWithContext(applicationContext)
        OneSignal.setAppId("34c58004-3b12-4339-8e2c-64f170e6c577")
    }
}