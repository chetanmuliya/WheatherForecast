package cm.chettas.wheatherforecast.util

sealed class ResultEvent<T>(
    val data:T?,
    val message: String?
) {
    class Success<T>(data: T? = null): ResultEvent<T>(data,null)
    class Error<T>(message: String): ResultEvent<T>(null,message)

    fun toData(): T? = if(this is Success) this.data else null
}
