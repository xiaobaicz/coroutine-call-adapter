package cc.xiaobaicz.adapter.retrofit.call

import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.Retrofit
import retrofit2.SkipCallbackExecutor
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

/**
 * Use:
 * Retrofit.Builder()
 *  .baseUrl("https://www.google.com/")
 *  .addCallAdapterFactory(SuspendCallAdapterFactory)
 *  .build()
 * @see CoroutineCall
 */
object CoroutineCallAdapterFactory : CallAdapter.Factory() {

    override fun get(returnType: Type, annotations: Array<out Annotation>, retrofit: Retrofit): CallAdapter<*, *>? {
        if (hasSkipCallbackExecutor(annotations))
            return null
        if (isSuspendCall(returnType))
            return null
        return SuspendCallAdapter(getParameterUpperBound(0, returnType as ParameterizedType))
    }

    private fun hasSkipCallbackExecutor(annotations: Array<out Annotation>): Boolean {
        for (annotation in annotations) {
            if (annotation is SkipCallbackExecutor)
                return true
        }
        return false
    }

    private fun isSuspendCall(returnType: Type): Boolean = getRawType(returnType) != CoroutineCall::class.java

    private class SuspendCallAdapter(private val type: Type) : CallAdapter<Any, CoroutineCall<Any>> {
        override fun responseType(): Type {
            return type
        }

        override fun adapt(call: Call<Any>): CoroutineCall<Any> {
            return CoroutineCallImpl(call)
        }
    }

}