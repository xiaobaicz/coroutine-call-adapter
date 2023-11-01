package cc.xiaobaicz.adapter.retrofit.call

import kotlinx.coroutines.flow.Flow
import retrofit2.Call

/**
 * Use:
 * interface ServiceTest {
 *  @GET("/")
 *  fun index(): CoroutineCall<ResponseBody>
 * }
 * @see CoroutineCallAdapterFactory
 */
interface CoroutineCall<T> {

    val realCall: Call<T>

    /**
     * start network request
     */
    suspend fun call(): Result<T>

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