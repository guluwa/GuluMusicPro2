package com.guluwa.gulumusicpro.data.repository.remote.retrofit

import android.util.Log
import com.guluwa.gulumusicpro.data.repository.remote.retrofit.exception.NoNetworkException
import com.guluwa.gulumusicpro.data.repository.remote.retrofit.exception.ServiceException
import com.guluwa.gulumusicpro.manage.Contacts
import com.guluwa.gulumusicpro.manage.MyApplication
import com.guluwa.gulumusicpro.utils.AppUtils
import com.readystatesoftware.chuck.ChuckInterceptor
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import okio.Buffer
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.io.UnsupportedEncodingException
import java.net.URLDecoder
import java.util.concurrent.TimeUnit

object RetrofitWorker {

    val retrofitWorker: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(Contacts.BASEURL)
            .client(OkHttpClient.Builder()
                .addInterceptor(ChuckInterceptor(MyApplication.context))//打印
                .addInterceptor(sLoggingInterceptor)
                .addInterceptor { chain ->
                    val connected = AppUtils.isNetConnected
                    if (connected) {
                        chain.proceed(chain.request())
                    } else {
                        throw NoNetworkException("没有网络哦~~~")
                    }
                }
                .addInterceptor { chain ->
                    val proceed = chain.proceed(chain.request())
                    if (proceed.code() == 404) {
                        throw ServiceException("服务器好像出了点小问题~~~")
                    } else {
                        proceed
                    }
                }
                .connectTimeout(15, TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true).build())
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build().create(ApiService::class.java)
    }

    /**
     * 打印返回的json数据拦截器
     */
    private val sLoggingInterceptor = Interceptor { chain ->
        val request = chain.request()
        val requestBuffer = Buffer()
        if (request.body() != null) {
            request.body()!!.writeTo(requestBuffer)
        } else {
            Log.d("LogTAG", "request.body() == null")
        }
        //打印url信息
        Log.w(
            "LogTAG",
            request.url().toString() + if (request.body() != null) "?" + parseParams(
                request.body(),
                requestBuffer
            ) else ""
        )
        chain.proceed(request)
    }

    @Throws(UnsupportedEncodingException::class)
    private fun parseParams(body: RequestBody?, requestBuffer: Buffer): String {
        return if (body!!.contentType() != null && !body.contentType()!!.toString().contains("multipart")) {
            URLDecoder.decode(requestBuffer.readUtf8(), "UTF-8")
        } else "null"
    }
}
