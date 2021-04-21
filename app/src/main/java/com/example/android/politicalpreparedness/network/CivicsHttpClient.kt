package com.example.android.politicalpreparedness.network

import okhttp3.OkHttpClient

class CivicsHttpClient: OkHttpClient() {

    companion object {
//  https://www.googleapis.com/civicinfo/v2/elections?key=AIzaSyBVi-yCWfTYpr2lqclNWtfawdpNmSxkVvc
        const val API_KEY = "AIzaSyBVi-yCWfTYpr2lqclNWtfawdpNmSxkVvc" //TODO: Place your API Key Here

        fun getClient(): OkHttpClient {
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