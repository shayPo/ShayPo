package polak.shay.paybox.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import polak.shay.paybox.model.Search

@Dao
interface SearchDao
{
    @Query("SELECT * FROM Search where mSearchWord LIKE :searchWord")
    fun searchLocalDataBase(searchWord : String) : Search

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertResult(search : Search)
}