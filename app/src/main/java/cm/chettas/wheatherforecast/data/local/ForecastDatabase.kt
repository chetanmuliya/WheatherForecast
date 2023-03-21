package cm.chettas.wheatherforecast.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import java.util.*

object Converters {
    @JvmStatic
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? = value?.let(::Date)

    @JvmStatic
    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? = date?.time
}

@Database(entities = [ForecastEntity::class, StateEnity::class], version = 7, exportSchema = false)
@TypeConverters(value = [Converters::class])
abstract class ForecastDatabase: RoomDatabase() {
    abstract fun forecastDao(): ForecastDao
    abstract fun stateDao(): StateDao
}
