package cm.chettas.wheatherforecast.util

sealed class Resource<T>(
    val data:T?,
    val message: String?
) {
    class Success<T>(data: T? = null): Resource<T>(data,message = "SUCCESS")
    class Error<T>(message: String): Resource<T>(null,message)
    class Loading<T>: Resource<T>(null,null)

    fun toData(): T? = if(this is Success) this.data else null
}
