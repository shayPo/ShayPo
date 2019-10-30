package polak.shay.paybox.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import polak.shay.paybox.model.Search

@Database(entities = arrayOf(Search::class), version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun searchDao(): SearchDao
}