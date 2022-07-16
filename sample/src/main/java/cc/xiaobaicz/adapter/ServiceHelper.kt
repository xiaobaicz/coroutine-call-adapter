package cc.xiaobaicz.adapter

import cc.xiaobaicz.adapter.retrofit.call.CoroutineCallAdapterFactory
import retrofit2.Retrofit
import retrofit2.create

object ServiceHelper {

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl("https://www.google.com/")
        .addCallAdapterFactory(CoroutineCallAdapterFactory)
        .build()

    val service get() = retrofit.create<Service>()

}