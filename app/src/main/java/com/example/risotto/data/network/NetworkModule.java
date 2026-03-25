package com.example.risotto.data.network;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public final class NetworkModule {

    public static final String BASE_URL = "https://www.themealdb.com/api/json/v1/1/";

    private static volatile NetworkModule instance;
    private final Retrofit retrofit;

    private NetworkModule() {
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .build();
    }

    public static NetworkModule getInstance() {
        if (instance == null) {
            synchronized (NetworkModule.class) {
                if (instance == null) {
                    instance = new NetworkModule();
                }
            }
        }
        return instance;
    }

    public Retrofit getRetrofit() {
        return retrofit;
    }
}