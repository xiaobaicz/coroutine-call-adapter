package cc.xiaobaicz.adapter

import android.os.Bundle
import android.text.Html
import androidx.appcompat.app.AppCompatActivity
import cc.xiaobaicz.adapter.databinding.ActivityMainBinding
import cc.xiaobaicz.adapter.retrofit.call.async
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private val bind by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private val service by lazy {
        ServiceHelper.service
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(bind.root)
        bind.lifecycleOwner = this

        MainScope().launch {
            val (data, throwable) = service.index().call()
            throwable?.apply { printStackTrace() }
            data?.apply {
                bind.text = Html.fromHtml(string())
            }
        }

        MainScope().launch {
            val index = async(service.index())
            val (data, throwable) = index.await()
            throwable?.apply { printStackTrace() }
            data?.apply {
                bind.text = Html.fromHtml(string())
            }
        }

        MainScope().launch {
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