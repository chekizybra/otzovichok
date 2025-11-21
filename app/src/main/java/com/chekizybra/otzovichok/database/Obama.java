package com.chekizybra.otzovichok.database;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Obama {
    public static void main(String[] args) {

        String apiKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6Imd4eGhodnB5bG5pdWl3Z2Vyc2x0Iiwicm9sZSI6ImFub24iLCJpYXQiOjE3NjM1NTgzNzQsImV4cCI6MjA3OTEzNDM3NH0.ywOjtXlQZP-llJUCYnm8RSl2AiN0Dh6zE6Dg6vzFm1Y";
        Zaprosi test = BdConnect.getInstance().create(Zaprosi.class);
        String response = test.getClientInfo("test@mail.ru",apiKey).toString();

        try {


            // Выводим полученный JSON в консоль
            System.out.println(response);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
