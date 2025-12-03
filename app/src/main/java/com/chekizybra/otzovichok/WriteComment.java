package com.chekizybra.otzovichok;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.chekizybra.otzovichok.database.BdConnect;
import com.chekizybra.otzovichok.database.Zaprosi;
import com.chekizybra.otzovichok.database.SessionData;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;

public class WriteComment extends AppCompatActivity {

    private TextView commentNameTF;
    private EditText commentTextTF;
    private RatingBar ratingBar;
    private Button toMainB, applyB;
    String productName;
    String apiKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6Imd4eGhodnB5bG5pdWl3Z2Vyc2x0Iiwicm9sZSI6ImFub24iLCJpYXQiOjE3NjM1NTgzNzQsImV4cCI6MjA3OTEzNDM3NH0.ywOjtXlQZP-llJUCYnm8RSl2AiN0Dh6zE6Dg6vzFm1Y";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_comment);

        commentNameTF = findViewById(R.id.commentNameTextField);
        commentTextTF = findViewById(R.id.commentTextTextField);
        ratingBar = findViewById(R.id.ratingBar);
        toMainB = findViewById(R.id.toMainButton);
        applyB = findViewById(R.id.applyButton);

        Intent intent = getIntent();
        productName = intent.getStringExtra("product_name");
        int categoryId = intent.getIntExtra("category_id", -1);

        commentNameTF.setText(productName);

        int CategoryId = categoryId;

        toMainB.setOnClickListener(v -> {
            startActivity(new Intent(WriteComment.this, MainTab.class));
            finish();
        });

        applyB.setOnClickListener(v -> {
            writeComment(CategoryId);
        });
    }

    private void writeComment(int categoryId) {
        String commentText = commentTextTF.getText().toString().trim();
        float ratingValue = ratingBar.getRating();

        if (commentText.isEmpty()) {
            Toast.makeText(this, "Введите текст отзыва", Toast.LENGTH_SHORT).show();
            return;
        }

        if (ratingValue == 0) {
            Toast.makeText(this, "Поставьте оценку", Toast.LENGTH_SHORT).show();
            return;
        }

        Zaprosi api = BdConnect.getInstance().create(Zaprosi.class);

        Map<String, Object> body = new HashMap<>();
        body.put("user_id", SessionData.currentUserId);
        body.put("category_id", categoryId);
        body.put("title", productName);
        body.put("comment", commentText);
        body.put("grade", (int)ratingValue);
        body.put("plus_grade", 0);
        body.put("minus_grade", 0);

        //дата
        SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String currentDate = date.format(new Date());
        body.put("post_date", currentDate);

        Call<Void> call = api.addComment("Bearer " + apiKey, apiKey, body);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, retrofit2.Response<Void> response) {
                if(response.isSuccessful()){
                    Toast.makeText(WriteComment.this, "Отзыв успешно добавлен", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(WriteComment.this, MainTab.class));
                    finish();
                } else {
                    Toast.makeText(WriteComment.this, "Ошибка добавления отзыва", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(WriteComment.this, "Сетевая ошибка", Toast.LENGTH_SHORT).show();
            }
        });
    }
}