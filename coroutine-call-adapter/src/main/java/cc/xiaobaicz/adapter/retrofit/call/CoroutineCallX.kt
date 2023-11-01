package cc.xiaobaicz.adapter.retrofit.call

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import retrofit2.Call
import retrofit2.HttpException
import kotlin.coroutines.CoroutineContext

internal fun <T> getResult(call: Call<T>): CoroutineCall.Result<T> = try {
    val response = call.execute()
    if (response.isSuccessful)
        CoroutineCall.Result(response.body())
    else
        CoroutineCall.Result(throwable = HttpException(response))
} catch (t: Throwable) {
    CoroutineCall.Result(throwable = t)
}

fun <T> CoroutineScope.async(
    call: CoroutineCall<T>,
    context: CoroutineContext = Dispatchers.IO,
    start: CoroutineStart = CoroutineStart.DEFAULT,
): Deferred<CoroutineCall.Result<T>> = async(context, start) {
    getResult(call.realCall)
}