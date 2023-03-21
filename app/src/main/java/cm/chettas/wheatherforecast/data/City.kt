package cm.chettas.wheatherforecast.data


import com.google.gson.annotations.SerializedName

data class City(
    val id: Long? = null,
    val name: String? = null,
    val coord: Coord? = null,
    val country: String? = null,
    val population: Int? = null,
    val timezone: Int? = null,
    val sunrise: Int? = null,
    val sunset: Int? = null
)