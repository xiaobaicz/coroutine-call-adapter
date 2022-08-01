> Retrofit 协程适配器

支持三种调用方式
- suspend 调用无 try catch
- async await 并发无 try catch
- flow 流式调用

### 引入方式
``` Gradle
// root build.gradle
allprojects {
	repositories {
		...
		maven { url 'https://jitpack.io' }
	}
}
```
``` Gradle
// module build.gradle
dependencies {
        implementation 'com.github.XiaoBaiCZ:CoroutineCallAdapter:2.9.0'
}
```

### Service Interface
``` Kotlin
interface Service {
    // CoroutineCall<T>
    @GET("/")
    fun index(): CoroutineCall<ResponseBody>
}
```
``` Kotlin
object ServiceHelper {
    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl("https://www.google.com/")
        // add call adapter
        .addCallAdapterFactory(CoroutineCallAdapterFactory)
        .build()

    val service get() = retrofit.create<Service>()
}
```
### Coroutine Suspend
``` Kotlin
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
```
### Coroutine Async
``` Kotlin
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
```
### Coroutine Flow
``` Kotlin
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
                println(content)
            }
        }
    }

}
```
