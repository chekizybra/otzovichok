package com.chekizybra.otzovichok;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.chekizybra.otzovichok.database.BdConnect;
import com.chekizybra.otzovichok.database.Zaprosi;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Reg extends AppCompatActivity {

    private EditText fioTF, emailTF, passwordTF, passwordReplyTF;
    private Button regB, toLogB;
    private String apiKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6Imd4eGhodnB5bG5pdWl3Z2Vyc2x0Iiwicm9sZSI6ImFub24iLCJpYXQiOjE3NjM1NTgzNzQsImV4cCI6MjA3OTEzNDM3NH0.ywOjtXlQZP-llJUCYnm8RSl2AiN0Dh6zE6Dg6vzFm1Y";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg);

        fioTF = findViewById(R.id.fioTextField);
        emailTF = findViewById(R.id.emailTextField);
        passwordTF = findViewById(R.id.passwordRegTextField);
        passwordReplyTF = findViewById(R.id.passwordReplayTextField);

        regB = findViewById(R.id.regButton);
        toLogB = findViewById(R.id.toLogButton);

        regB.setOnClickListener(v -> register());

        toLogB.setOnClickListener(v -> {
            startActivity(new Intent(Reg.this, Log.class));
        });
    }

    private void register() {
        String fio = fioTF.getText().toString().trim();
        String email = emailTF.getText().toString().trim();
        String password = passwordTF.getText().toString();
        String passwordRep = passwordReplyTF.getText().toString();

        if (fio.isEmpty() || email.isEmpty() || password.isEmpty() || passwordRep.isEmpty()) {
            Toast.makeText(this, "Все поля должны быть заполнены", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!password.equals(passwordRep)) {
            Toast.makeText(this, "Пароли не совпадают", Toast.LENGTH_SHORT).show();
            return;
        }

        // подготовка запроса
        Map<String, Object> body = new HashMap<>();
        body.put("fio", fio);
        body.put("mail", email);
        body.put("pasword", password);
        body.put("role_id", 1);

        Zaprosi api = BdConnect.getInstance().create(Zaprosi.class);
        Call<Void> call = api.register("Bearer " + apiKey, apiKey, body);


        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                System.out.println(response);
                if (response.isSuccessful()) {
                    Toast.makeText(Reg.this, "Регистрация успешна", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(Reg.this, Log.class));
                    finish();
                } else {
                    Toast.makeText(Reg.this, "Ошибка регистрации: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(Reg.this, "Сетевая ошибка: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
