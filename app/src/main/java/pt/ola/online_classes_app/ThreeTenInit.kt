package pt.ola.online_classes_app

import android.app.Application
import com.jakewharton.threetenabp.AndroidThreeTen

class ThreeTenInit : Application() {
    override fun onCreate() {
        super.onCreate()
        AndroidThreeTen.init(this)
    }
}