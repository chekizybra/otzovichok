package com.chekizybra.otzovichok;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.chekizybra.otzovichok.database.BdConnect;
import com.chekizybra.otzovichok.database.Comment;
import com.chekizybra.otzovichok.database.SessionData;
import com.chekizybra.otzovichok.database.Zaprosi;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Account extends AppCompatActivity {

    private TextView allCommTF, fioTF;

    private Button toMainB;
    private String apiKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6Imd4eGhodnB5bG5pdWl3Z2Vyc2x0Iiwicm9sZSI6ImFub24iLCJpYXQiOjE3NjM1NTgzNzQsImV4cCI6MjA3OTEzNDM3NH0.ywOjtXlQZP-llJUCYnm8RSl2AiN0Dh6zE6Dg6vzFm1Y";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        fioTF = findViewById(R.id.nameTextField);
        allCommTF = findViewById(R.id.allCommTextField);
        toMainB = findViewById(R.id.to_main_button);

        fioTF.setText(SessionData.currentUserFio);

        toMainB.setOnClickListener(v -> {
            Intent intent = new Intent(Account.this, MainTab.class);
            startActivity(intent);
        });

        loadUserComments();
    }

    private void loadUserComments() {
        Zaprosi api = BdConnect.getInstance().create(Zaprosi.class);

        //фильтр по текущему пользователю
        Call<List<Comment>> call = api.getComment("Bearer " + apiKey, apiKey, "eq." + String.valueOf(SessionData.currentUserId));


        call.enqueue(new Callback<List<Comment>>() {
            @Override
            public void onResponse(Call<List<Comment>> call, Response<List<Comment>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    StringBuilder sb = new StringBuilder();
                    for (Comment com : response.body()) {
                        sb.append(com.product_name + "\n").append(com.comment).append("\n\n");
                    }
                    allCommTF.setText(sb.toString());
                }
            }

            @Override
            public void onFailure(Call<List<Comment>> call, Throwable t) {}
        });
    }
}
