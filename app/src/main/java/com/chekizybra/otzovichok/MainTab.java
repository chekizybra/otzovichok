package com.chekizybra.otzovichok;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.chekizybra.otzovichok.database.BdConnect;
import com.chekizybra.otzovichok.database.Comment;
import com.chekizybra.otzovichok.database.Zaprosi;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainTab extends AppCompatActivity {
    private TextView commentsTF;
    private Button toAccount, newComm;
    String apiKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6Imd4eGhodnB5bG5pdWl3Z2Vyc2x0Iiwicm9sZSI6ImFub24iLCJpYXQiOjE3NjM1NTgzNzQsImV4cCI6MjA3OTEzNDM3NH0.ywOjtXlQZP-llJUCYnm8RSl2AiN0Dh6zE6Dg6vzFm1Y";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_tab);

        commentsTF = findViewById(R.id.allComments);
        toAccount = findViewById(R.id.to_profile_button);
        newComm = findViewById(R.id.toWriteButton);

        toAccount.setOnClickListener(v -> {
            Intent intent = new Intent(MainTab.this, Account.class);
            startActivity(intent);
        });

        newComm.setOnClickListener(v -> {
            Intent intent = new Intent(MainTab.this, WriteComment.class);
            startActivity(intent);
        });

        loadComments();
    }

    private void loadComments() {
        Zaprosi api = BdConnect.getInstance().create(Zaprosi.class);

        Call<List<Comment>> call = api.getComments("Bearer " + apiKey, apiKey);
        call.enqueue(new Callback<List<Comment>>() {
            @Override
            public void onResponse(Call<List<Comment>> call, Response<List<Comment>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    StringBuilder sb = new StringBuilder();
                    System.out.println(response.toString());
                    for (Comment com : response.body()) {
                        sb.append(com.product_name+"\n").append(com.comment).append("\n\n");
                    }
                    commentsTF.setText(sb.toString());
                }
            }

            @Override
            public void onFailure(Call<List<Comment>> call, Throwable t) {}
        });
    }
}