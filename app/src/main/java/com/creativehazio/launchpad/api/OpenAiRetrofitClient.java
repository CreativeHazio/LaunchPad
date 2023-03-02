package com.creativehazio.launchpad.api;

import androidx.annotation.NonNull;

import com.creativehazio.launchpad.R;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class OpenAiRetrofitClient {

    private static final String BASE_URL = "https://api.openai.com/v1/";
    private static Retrofit retrofit;

    public static Retrofit getInstance(String API_KEY) {
        if (retrofit == null){
            HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
            httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .addInterceptor(new Interceptor() {
                        @NonNull
                        @Override
                        public okhttp3.Response intercept(@NonNull Chain chain) throws IOException {
                            Request originalRequest = chain.request();

                            Request newRequest = originalRequest.newBuilder()
                                    .header("Authorization","Bearer "+ API_KEY)
                                    .build();
                            return chain.proceed(newRequest);
                        }
                    })
                    .addInterceptor(httpLoggingInterceptor)
                    .readTimeout(30, TimeUnit.SECONDS)
                    .writeTimeout(30, TimeUnit.SECONDS)
                    .build();

            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(okHttpClient)
                    .build();
        }
        return retrofit;
    }
}
