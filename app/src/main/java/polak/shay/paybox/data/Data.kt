package polak.shay.paybox.data

import android.content.Context
import android.os.Handler
import android.os.HandlerThread
import android.os.Looper
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import polak.shay.paybox.data.local.AppDatabase
import polak.shay.paybox.data.network.RestApi
import polak.shay.paybox.data.network.Retrofit
import polak.shay.paybox.model.Results
import polak.shay.paybox.model.Search
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import com.google.gson.Gson
import polak.shay.paybox.R


class Data private constructor() : Callback<Results>, Observable() {
    private var mCurrentData: Results? = null
    private val key = "14098484-210ea4577baa93b103c7ac17b"
    private var mSearching = ""
    private val mRetrofitInterface by lazy {
        Retrofit().getRetrofit().create(RestApi::class.java)
    }
    private val mMainHandler by lazy { Handler(Looper.getMainLooper()) }
    private val mHandler by lazy {
        val worker = HandlerThread("data_base")
        worker.start()
        Handler(worker.looper)
    }
    private var mLocalDataBase: AppDatabase? = null

    fun initLocalDataBase(context: Context) {
        val name = context.getString(R.string.local_data_base_name)
        mLocalDataBase = Room.databaseBuilder(
            context,
            AppDatabase::class.java, "$name"
        ).build()
    }

    fun search(find: String, page: Int, observer: Observer) {
        addObserver(observer)
        mSearching = find
        if (page == 1 && mLocalDataBase != null) {
            if (mLocalDataBase!!.isOpen) {
                mHandler.post {
                    val data = mLocalDataBase?.searchDao()?.searchLocalDataBase(find)
                    mMainHandler.post {
                        if (data == null) {
                            val networkFind = find.replace(" ", "+")
                            mRetrofitInterface.getImages(key, networkFind, page).enqueue(this)
                        } else {
                            val gson = Gson()
                            val json = gson.fromJson<Results>(data.mResults, Results::class.java)
                            hasChanged()
                            notifyObservers(json)
                        }
                    }
                }
            } else {
                val networkFind = find.replace(" ", "+")
                mRetrofitInterface.getImages(key, networkFind, page).enqueue(this)
            }
        } else {
            val networkFind = find.replace(" ", "+")
            mRetrofitInterface.getImages(key, networkFind, page).enqueue(this)
        }
    }

    override fun onFailure(call: Call<Results>, t: Throwable) {

    }

    override fun onResponse(call: Call<Results>, response: Response<Results>) {
        if (response.body() != null) {
            mHandler.post {
                if(mLocalDataBase!!.isOpen) {
                    val gson = Gson()
                    val json = gson.toJson(response.body())
                    val tep = Search(mSearching, json.toString())
                    mLocalDataBase?.searchDao()?.insertResult(tep)
                }
                mSearching = ""
            }
            setChanged()
            notifyObservers(response.body())
        }
    }

    override fun notifyObservers(arg: Any?) {
        super.notifyObservers(arg)
    }

    fun updateCurrentData(displayData : Results) { mCurrentData = displayData }

    fun getCurrentData(): Results? { return mCurrentData }

    companion object {
        private val mInstance: Data by lazy { Data() }

        fun getInstance(): Data {
            return mInstance
        }
    }
}