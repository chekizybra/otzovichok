package com.chekizybra.otzovichok.database;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface Zaprosi {
    @GET("rest/v1/users")
    Call<List<ClientInfo>> getClientInfo(
            @Query("email") String email,
            @Query("apiKey") String apiKey
    );
    @POST("rest/v1/users")
    Call<Void> register(
            @Query("fio") String fio,
            @Query("email") String email,
            @Query("password") String password,
            @Query("apiKey") String apiKey
    );
}
