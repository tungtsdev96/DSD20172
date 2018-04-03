package com.hust.edu.dsd.api;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.hust.edu.dsd.AccountUtil;
import com.hust.edu.dsd.utils.Constants;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by tungts on 7/25/2017.
 */

public final class ApiFactory {

    private static Retrofit retrofitNodeJs;

    private ApiFactory(){}

    @NonNull
    public static ApiHust getApiHust() {
        return getRetrofitApiNodeJs().create(ApiHust.class);
    }

    @NonNull
    private static Retrofit getRetrofitApiNodeJs() {
        if(retrofitNodeJs == null ) {
            synchronized(ApiFactory.class) {
                retrofitNodeJs = new Retrofit.Builder()
                        .baseUrl(ConfigApi.URL_NODEJS)
                        .addConverterFactory(GsonConverterFactory.create())
                        .client(okHttpClient())
                        .build();
            }
        }
        return retrofitNodeJs;
    }
    @NonNull
    private static OkHttpClient okHttpClient() {

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.HEADERS);
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request original = chain.request();
                Request request;

                if (TextUtils.isEmpty(AccountUtil.getAuthorization()))
                    request = original.newBuilder()
                            .method(original.method(), original.body())
                            .build();
                else
                    request = original.newBuilder()
                            .addHeader(Constants.AUTHORIZATION_DATA,AccountUtil.getAuthorization())
                            .addHeader(Constants.CONTENT_TYPE, "application/json")
                            .method(original.method(), original.body())
                            .build();

                return chain.proceed(request);
            }
        });
        httpClient.addInterceptor(logging);

        OkHttpClient client = httpClient
                .connectTimeout(ConfigApi.CONNECT_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(ConfigApi.READ_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(ConfigApi.WRITE_TIMEOUT, TimeUnit.SECONDS)
                .build();

        return client;
    }

}
