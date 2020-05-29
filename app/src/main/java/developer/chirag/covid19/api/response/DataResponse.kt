package developer.chirag.covid19.api.response


/**
 * Created by Chirag Sidhiwala on 29/5/20.
 */
sealed class DataResponse<T> {
    class Loading<T> : DataResponse<T>()
    data class Success<T>(val data: T) : DataResponse<T>()
    data class Error<T>(val message: String) : DataResponse<T>()

    companion object {
        fun <T> loading() = Loading<T>()
        fun <T> success(data: T) = Success<T>(data)
        fun <T> error(message: String) = Error<T>(message)
    }
}