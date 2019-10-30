package polak.shay.paybox.data.network

import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class Retrofit
{
    private val mUrl = "https://pixabay.com/api/"

    fun getRetrofit(): Retrofit {
        val gson = GsonBuilder().setLenient().create()

        return Retrofit.Builder()
            .baseUrl(mUrl)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }
}