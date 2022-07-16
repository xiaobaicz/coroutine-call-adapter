package cc.xiaobaicz.adapter

import cc.xiaobaicz.adapter.retrofit.call.CoroutineCallAdapterFactory
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.create

class CoroutineCallAdapterTest {

    @Test
    fun testCallAdapterSync() = runBlocking<Unit> {
        val service = createRetrofit().create<ServiceTest>()
        val (data, throwable) = service.index().call()
        throwable?.apply {
            // todo error flow
            println(this)
        }
        data?.apply {
            // todo success flow
            println(this.string())
        }
    }

    @Test
    fun testCallAdapterAsync() = runBlocking<Unit> {
        val service = createRetrofit().create<ServiceTest>()
        val index = service.index().async(this)
        val (data, throwable) = index.await()
        throwable?.apply { printStackTrace() }
        data?.apply {
            println(this.string())
        }
    }

    @Test
    fun testCallAdapterFlow() = runBlocking<Unit> {
        val service = createRetrofit().create<ServiceTest>()
        service.index().flow().map { body ->
            body.string()
        }.catch { t ->
            t.printStackTrace()
        }.collect { content ->
            println(content)
        }
    }

    private fun createRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://www.google.com/")
            .addCallAdapterFactory(CoroutineCallAdapterFactory)
            .build()
    }

}