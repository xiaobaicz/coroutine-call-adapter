package cc.xiaobaicz.adapter.retrofit.call

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import retrofit2.Call
import kotlinx.coroutines.flow.flow as ktFlow

internal class CoroutineCallImpl<T>(private val call: Call<T>) : CoroutineCall<T> {

    override val realCall: Call<T> get() = call.clone()

    override suspend fun call(): CoroutineCall.Result<T> {
        return withContext(Dispatchers.IO) {
            getResult(realCall)
        }
    }

    override fun flow(): Flow<T> = ktFlow<T> {
        val (d, t) = getResult(realCall)
        if (t != null) throw t
        d ?: return@ktFlow
        emit(d)
    }.flowOn(Dispatchers.IO)

}