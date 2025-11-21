package com.chekizybra.otzovichok.database;


import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface Zaprosi {
    @GET("rest/v1/users")
    Call<List<ClientInfo>> getClientInfo(
            @Header("Authorization") String bearer,
            @Header("apiKey") String apiKey,
            @Query("email") String email
    );
    @POST("rest/v1/users")
    Call<Void> register(
            @Header("Authorization") String bearer,
            @Header("apiKey") String apiKey,
            @Body Map<String, String> body
    );

    @POST("rest/v1/comments")
    Call<Void> addComment(
            @Header("Authorization") String bearer,
            @Header("apiKey") String apiKey,
            @Body Map<String, String> body
    );

    @GET("rest/v1/comments")
    Call<List<ClientInfo>> getComment(
            @Header("Authorization") String bearer,
            @Header("apiKey") String apiKey,
            @Query("user_id") String userId
    );
    @GET("rest/v1/getMainComment") // заменяй на твою таблицу с комментариями
    Call<List<Comment>> getComments(
            @Header("Authorization") String bearer,
            @Header("apiKey") String apiKey
    );
}
