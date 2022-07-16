package cc.xiaobaicz.adapter

import cc.xiaobaicz.adapter.retrofit.call.CoroutineCall
import okhttp3.ResponseBody
import retrofit2.http.GET

interface ServiceTest {

    @GET("/")
    fun index(): CoroutineCall<ResponseBody>

}