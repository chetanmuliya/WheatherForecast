package cm.chettas.wheatherforecast.data.local

import androidx.room.*

@Dao
interface StateDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertForecast(weathers: StateEnity)

    @Query("SELECT * FROM states WHERE state_name = :stateName")
    fun getState(stateName: String): StateEnity?

    @Query("SELECT * FROM states")
    fun getAllState(): List<StateEnity>

}