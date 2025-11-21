package com.chekizybra.otzovichok;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.chekizybra.otzovichok.database.BdConnect;
import com.chekizybra.otzovichok.database.ClientInfo;
import com.chekizybra.otzovichok.database.SessionData;
import com.chekizybra.otzovichok.database.Zaprosi;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Log extends AppCompatActivity {
    private TextView emailTF, passwordTF;
    private Button logB, toRegB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_log);

        emailTF = findViewById(R.id.emailTextField);
        passwordTF = findViewById(R.id.passwordTextField);
        logB = findViewById(R.id.toLogButton);
        toRegB = findViewById(R.id.regButton);

        logB.setOnClickListener(v ->{
            login();
        });

        toRegB.setOnClickListener(v -> {
            Intent intent = new Intent(Log.this, Reg.class);
            startActivity(intent);
        });
    }


    private void login() {

        String apiKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6Imd4eGhodnB5bG5pdWl3Z2Vyc2x0Iiwicm9sZSI6ImFub24iLCJpYXQiOjE3NjM1NTgzNzQsImV4cCI6MjA3OTEzNDM3NH0.ywOjtXlQZP-llJUCYnm8RSl2AiN0Dh6zE6Dg6vzFm1Y";

        Zaprosi api = BdConnect.getInstance().create(Zaprosi.class);

        Call<List<ClientInfo>> call = api.getClientInfo("Bearer " + apiKey, apiKey, "eq." + emailTF.getText().toString());

        call.enqueue(new Callback<List<ClientInfo>>() {
            @Override
            public void onResponse(Call<List<ClientInfo>> call, Response<List<ClientInfo>> response) {
                if (response.isSuccessful()) {
                    List<ClientInfo> list = response.body();
                    if (list != null && !list.isEmpty()) {
                        ClientInfo ci = list.get(0);

                        int id = ci.id;
                        String fio = ci.fio;
                        String password = ci.password;
                        System.out.println(passwordTF.getText().toString());
                        if(passwordTF.getText().toString().equals(password)){
                            SessionData.currentUserId = id;
                            SessionData.currentUserFio = fio;

                            Intent intent = new Intent(Log.this, MainTab.class);
                            startActivity(intent);
                        }
                        else {
                            Toast.makeText(getApplicationContext(), "не тот пороль", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<List<ClientInfo>> call, Throwable t) {
                System.out.println("ошибка");
            }
        });
    }
}