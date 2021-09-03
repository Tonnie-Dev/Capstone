package com.androidshowtime.network

import okhttp3.OkHttpClient

class CivicsHttpClient: OkHttpClient() {

    companion object {
//  https://www.googleapis.com/civicinfo/v2/elections?key=AIzaSyBVi-yCWfTYpr2lqclNWtfawdpNmSxkVvc
//  https://www.googleapis.com/civicinfo/v2/voterinfo?key=AIzaSyBVi-yCWfTYpr2lqclNWtfawdpNmSxkVvc
//  https://www.googleapis.com/civicinfo/v2/representatives?key=AIzaSyBVi-yCWfTYpr2lqclNWtfawdpNmSxkVvc

        const val API_KEY = "AIzaSyBVi-yCWfTYpr2lqclNWtfawdpNmSxkVvc" //TODO: Place your API Key Here
       /* We need to prepare a custom OkHttp client because need to use
       our custom call interceptor to be able to authenticate our requests.*/
        fun getClient(): OkHttpClient {

            //Interceptors are a powerful way to customize your requests.We add
           // the interceptor to OkHttpClient.It will add authentication headers to every call we make.
            return Builder()
                    .addInterceptor { chain ->
                        val original = chain.request()
                        val url = original
                                .url()
                                .newBuilder()
                                .addQueryParameter("key", API_KEY)
                                .build()
                        val request = original
                                .newBuilder()
                                .url(url)
                                .build()
                        chain.proceed(request)
                    }
                    .build()
        }

    }

}