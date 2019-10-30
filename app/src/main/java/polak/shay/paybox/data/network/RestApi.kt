package polak.shay.paybox.data.network


import polak.shay.paybox.model.Results
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RestApi
{
//    @GET("?key={key}&q={search}&image_type=photo")//&lang
//    @GET("?key=@Query.key&q=@Query.search&image_type=photo")
//    @Query("key")
//    fun getImages(key : String, search : String) : Call<Results>

    @GET("?")
    fun getImages(@Query("key") key : String,
                  @Query("q") search : String,
                  @Query("page") pageNum : Int) : Call<Results>

}