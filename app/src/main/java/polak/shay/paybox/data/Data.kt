package polak.shay.paybox.data

import android.content.Context
import android.os.Handler
import android.os.HandlerThread
import android.os.Looper
import androidx.room.Room
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
import polak.shay.paybox.model.Hit


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
            AppDatabase::class.java, name
        ).fallbackToDestructiveMigration().build()
    }

    fun search(find: String, page: Int, observer: Observer) {
        addObserver(observer)
        mSearching = find
        if (page == 1 && mLocalDataBase != null) {
            mHandler.post {
                val data = mLocalDataBase?.searchDao()?.searchLocalDataBase(find)
                if (data != null) {
                    val gson = Gson()
                    var results: Results? = null
                    val resultsList = data.mResults.split(Search.NEXT_RESULT)
                    resultsList.forEach {
                        val json = gson.fromJson<Results>(it, Results::class.java)
                        if (results == null) {
                            results = json
                        } else {
                            results!!.hits.addAll(json.hits)
                        }
                    }
                    mMainHandler.post {
                        setChanged()
                        notifyObservers(results)
                    }
                } else {
                    mMainHandler.post {
                        val networkFind = find.replace(" ", "+")
                        mRetrofitInterface.getImages(key, networkFind, page).enqueue(this)

                    }
                }
            }
        } else {
            val networkFind = find.replace(" ", "+")
            mRetrofitInterface.getImages(key, networkFind, page).enqueue(this)
        }
    }

    override fun onFailure(call: Call<Results>, t: Throwable) {
        setChanged()
        notifyObservers(null)
    }

    override fun onResponse(call: Call<Results>, response: Response<Results>) {
        if (response.body() != null) {
            mHandler.post {
                val gson = Gson()
                val json = gson.toJson(response.body())
                var data = mLocalDataBase?.searchDao()?.searchLocalDataBase(mSearching)
                if (data == null) {
                    data = Search(mSearching, json.toString())
                } else {
                    val builder = StringBuilder(data.mResults)
                    builder.append(Search.NEXT_RESULT)
                    builder.append(json.toString())
                    data.mResults = builder.toString()
                }
                mLocalDataBase?.searchDao()?.insertResult(data)
                mSearching = ""
            }
            setChanged()
            notifyObservers(response.body())
        } else {
            setChanged()
            notifyObservers(null)
        }
    }

    fun updateCurrentData(displayData: Results) {
        mCurrentData = displayData
    }

    fun getCurrentData(): Results? {
        return mCurrentData
    }

    companion object {
        private val mInstance: Data by lazy { Data() }

        fun getInstance(): Data {
            return mInstance
        }
    }
}