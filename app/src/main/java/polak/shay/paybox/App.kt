package polak.shay.paybox

import android.app.Application
import polak.shay.paybox.data.Data

class App : Application()
{
    override fun onCreate() {
        super.onCreate()
        Data.getInstance().initLocalDataBase(this.applicationContext)
    }
}