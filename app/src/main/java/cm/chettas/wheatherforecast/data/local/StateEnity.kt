package cm.chettas.wheatherforecast.data.local

import android.os.Parcel
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "states")
data class StateEnity(

    @ColumnInfo(name = "id")
    val id: Long,

    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "state_name")
    val name: String = "",

    @ColumnInfo(name = "country")
    val country: String = "",

    @ColumnInfo(name = "lat")
    val lat: Double = Double.NEGATIVE_INFINITY,

    @ColumnInfo(name = "lng")
    val lng: Double = Double.NEGATIVE_INFINITY,

    @ColumnInfo(name = "zone_id")
    val zoneId: String
)
