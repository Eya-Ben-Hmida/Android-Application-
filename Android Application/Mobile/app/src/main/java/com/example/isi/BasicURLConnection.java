package com.example.isi;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class BasicURLConnection {
    public Retrofit getConnection(){
        Retrofit.Builder builder=new Retrofit.Builder()
                .baseUrl("http://digitalisi.tn:8080/engine-rest/")
                .addConverterFactory(GsonConverterFactory.create());
        Retrofit retrofit =builder.build();
        return retrofit;
    }
}
