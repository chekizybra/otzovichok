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
    //запрос для входа
    @GET("rest/v1/users")
    Call<List<ClientInfo>> getClientInfo(
            @Header("Authorization") String bearer,
            @Header("apiKey") String apiKey,
            @Query("email") String email
    );
    //запрос для регистрации
    @POST("rest/v1/users")
    Call<Void> register(
            @Header("Authorization") String bearer,
            @Header("apiKey") String apiKey,
            @Body Map<String, String> body
    );
    //запрос для добавления комментария
    @POST("rest/v1/comments")
    Call<Void> addComment(
            @Header("Authorization") String bearer,
            @Header("apiKey") String apiKey,
            @Body Map<String, String> body
    );
    //запрос для получения комментария по user_id
    @GET("rest/v1/comments")
    Call<List<Comment>> getComment(
            @Header("Authorization") String bearer,
            @Header("apiKey") String apiKey,
            @Query("user_id") String userId
    );
    //запрос для получения всех комментариев
    @GET("rest/v1/comments")
    Call<List<Comment>> getComments(
            @Header("Authorization") String bearer,
            @Header("apiKey") String apiKey
    );
}
