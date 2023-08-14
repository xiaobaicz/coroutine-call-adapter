package cc.xiaobaicz.adapter.retrofit.call

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.HttpException
import kotlin.coroutines.CoroutineContext
import kotlinx.coroutines.flow.flow as ktFlow

internal class CoroutineCallImpl<T>(private val call: Call<T>) : CoroutineCall<T> {

    override suspend fun call(): CoroutineCall.Result<T> {
        return withContext(Dispatchers.IO) {
            val response = call.execute()
            try {
                if (response.isSuccessful)
                    CoroutineCall.Result(response.body())
                else
                    CoroutineCall.Result(throwable = HttpException(response))
            } catch (t: Throwable) {
                CoroutineCall.Result(throwable = t)
            }
        }
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