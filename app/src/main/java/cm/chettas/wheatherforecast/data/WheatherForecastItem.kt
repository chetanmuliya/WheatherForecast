package cm.chettas.wheatherforecast.data


import com.google.gson.annotations.SerializedName

data class WheatherForecastItem(
    val dt: Long? = null,
    val main: Main,
    @SerializedName("dt_txt")
    val dtTxt: String
)