package cm.chettas.wheatherforecast.data.local

import androidx.room.*
import androidx.room.ForeignKey.Companion.CASCADE
import java.util.*


@Entity(
    tableName = "forecast",
    foreignKeys = [
        ForeignKey(
            entity = StateEnity::class,
            onDelete = CASCADE,
            onUpdate = CASCADE,
            parentColumns = ["state_name"],
            childColumns = ["state_name"]
        )
    ],
    indices = [Index(value = ["state_name"])]
)
data class ForecastEntity(
    @PrimaryKey(autoGenerate = false)
    val id: String = UUID.randomUUID().toString(),
    @ColumnInfo(name = "state_name")
    val stateName: String,
    @ColumnInfo(name = "data_time")
    val timeOfDataForecasted: Date,
    @ColumnInfo(name = "temperature")
    val temperature: Double
)