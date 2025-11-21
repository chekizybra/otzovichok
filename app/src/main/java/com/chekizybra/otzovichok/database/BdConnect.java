package com.chekizybra.otzovichok.database;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
public class BdConnect {
    private static final String baseurl = "https://gxxhhvpylniuiwgerslt.supabase.co/";
    private static Retrofit retrofit;

    public static Retrofit getInstance() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(baseurl)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}




