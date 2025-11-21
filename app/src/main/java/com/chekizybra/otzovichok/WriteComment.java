package com.chekizybra.otzovichok;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.chekizybra.otzovichok.database.BdConnect;
import com.chekizybra.otzovichok.database.Zaprosi;
import com.chekizybra.otzovichok.database.SessionData;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WriteComment extends AppCompatActivity {

    private TextView commentNameTF, commentTextTF;
    private Button toMainB, applyB;
    String apiKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6Imd4eGhodnB5bG5pdWl3Z2Vyc2x0Iiwicm9sZSI6ImFub24iLCJpYXQiOjE3NjM1NTgzNzQsImV4cCI6MjA3OTEzNDM3NH0.ywOjtXlQZP-llJUCYnm8RSl2AiN0Dh6zE6Dg6vzFm1Y";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_comment);

        commentNameTF = findViewById(R.id.commentNameTextField);
        commentTextTF = findViewById(R.id.commentTextTextField);
        toMainB = findViewById(R.id.toMainButton);
        applyB = findViewById(R.id.applyButton);

        toMainB.setOnClickListener(v -> {
            startActivity(new Intent(WriteComment.this, MainTab.class));
            finish();
        });

        applyB.setOnClickListener(v ->
                writeComment());
    }

    private void writeComment() {
        String name = commentNameTF.getText().toString();
        String text = commentTextTF.getText().toString();

        Zaprosi api = BdConnect.getInstance().create(Zaprosi.class);

        Map<String, String> body = new HashMap<>();
        body.put("user_id", String.valueOf(SessionData.currentUserId));
        body.put("product_name", name);
        body.put("comment", text);

        Call<Void> call = api.addComment("Bearer " + apiKey, apiKey, body);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(response.isSuccessful()){
                    startActivity(new Intent(WriteComment.this, MainTab.class));
                    finish();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {}
        });
    }
}
