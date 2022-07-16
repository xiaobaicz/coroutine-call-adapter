package cc.xiaobaicz.adapter.retrofit.call

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.flow.Flow
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

/**
 * Use:
 * interface ServiceTest {
 *  @GET("/")
 *  fun index(): CoroutineCall<ResponseBody>
 * }
 * @see CoroutineCallAdapterFactory
 */
interface CoroutineCall<T> {

    /**
     * start network request
     */
    suspend fun call(): Result<T>

    /**
     * start network request
     */
    suspend fun async(
        scope: CoroutineScope,
        context: CoroutineContext = EmptyCoroutineContext,
        start: CoroutineStart = CoroutineStart.DEFAULT,
    ): Deferred<Result<T>>

    /**
     * start network request
     */
    fun flow(): Flow<T>

    /**
     * network request result
     * @param data network data
     */
    data class Result<Data>(
        val data: Data? = null,
        val throwable: Throwable? = null,
    )

}