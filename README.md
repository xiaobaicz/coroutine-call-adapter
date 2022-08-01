# Coroutine Call Adapter

## Service Interface
~~~ Kotlin
interface Service {
    // CoroutineCall<T>
    @GET("/")
    fun index(): CoroutineCall<ResponseBody>
}
~~~
~~~ Kotlin
object ServiceHelper {
    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl("https://www.google.com/")
        // add call adapter
        .addCallAdapterFactory(CoroutineCallAdapterFactory)
        .build()

    val service get() = retrofit.create<Service>()
}
~~~
### Coroutine Suspend
~~~ Kotlin
class MainActivity : AppCompatActivity() {

    private val service by lazy {
        ServiceHelper.service
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MainScope().launch {
            // call service
            val (data, throwable) = service.index().call()
            // handle throwable?
            throwable?.apply { printStackTrace() }
            // handle success
            data?.apply {
                println(this)
            }
        }
    }

}
~~~
### Coroutine Async
~~~ Kotlin
class MainActivity : AppCompatActivity() {

    private val service by lazy {
        ServiceHelper.service
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MainScope().launch {
            // call service
            val index = service.index().async(this)
            // await response
            val (data, throwable) = index.await()
            // handle throwable?
            throwable?.apply { printStackTrace() }
            // handle success
            data?.apply {
                println(this)
            }
        }
    }

}
~~~
### Coroutine Flow
~~~ Kotlin
class MainActivity : AppCompatActivity() {

    private val service by lazy {
        ServiceHelper.service
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MainScope().launch {
            // 1.call service
            // 2.map to string
            // 3.collect(handle throwable / success)
            service.index().flow().map { body ->
                body.string()
            }.catch { t ->
                t.printStackTrace()
            }.collect { content ->
                bind.text = Html.fromHtml(content)
            }
        }
    }

}
~~~
