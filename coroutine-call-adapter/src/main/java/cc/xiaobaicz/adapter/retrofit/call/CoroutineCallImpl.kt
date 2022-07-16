package cc.xiaobaicz.adapter.retrofit.call

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.resume
import kotlinx.coroutines.flow.flow as ktFlow

internal class CoroutineCallImpl<T>(private val call: Call<T>) : CoroutineCall<T> {

    override suspend fun call(): CoroutineCall.Result<T> = suspendCancellableCoroutine {
        /**
         * The request uses the OkHttp Client thread pool.
         * The coroutine environment only affects the resulting response.
         */
        call.enqueue(object : Callback<T> {
            override fun onResponse(call: Call<T>, response: Response<T>) {
                it.resume(CoroutineCall.Result(response.body()))
            }

            override fun onFailure(call: Call<T>, t: Throwable) {
                it.resume(CoroutineCall.Result(throwable = t))
            }
        })
    }

    override suspend fun async(
        scope: CoroutineScope,
        context: CoroutineContext,
        start: CoroutineStart,
    ): Deferred<CoroutineCall.Result<T>> {
        return scope.async(context, start) { call() }
    }

    override fun flow(): Flow<T> {
        return ktFlow {
            val (data, throwable) = call()
            throwable?.apply { throw throwable }
            emit(data ?: throw NullPointerException("data & throwable is null"))
        }
    }

}