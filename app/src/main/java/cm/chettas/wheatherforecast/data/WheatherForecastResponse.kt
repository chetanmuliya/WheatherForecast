package cm.chettas.wheatherforecast.data


data class WheatherForecastResponse(
    val cod: String,
    val message: Int,
    val cnt: Int,
    val list: List<WheatherForecastItem>? = null,
    val city: City? = null
)