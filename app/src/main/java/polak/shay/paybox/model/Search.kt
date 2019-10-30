package polak.shay.paybox.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Search(
    @PrimaryKey val mSearchWord : String,
    val mResults: String
    )