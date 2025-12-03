package com.chekizybra.otzovichok;

import static com.chekizybra.otzovichok.R.*;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.chekizybra.otzovichok.database.BdConnect;
import com.chekizybra.otzovichok.database.Category;
import com.chekizybra.otzovichok.database.Comment;
import com.chekizybra.otzovichok.database.Zaprosi;

import java.util.List;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainTab extends AppCompatActivity {
    private Button toAccount, newComm;
    private LinearLayout commentsContainer;

    String apiKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6Imd4eGhodnB5bG5pdWl3Z2Vyc2x0Iiwicm9sZSI6ImFub24iLCJpYXQiOjE3NjM1NTgzNzQsImV4cCI6MjA3OTEzNDM3NH0.ywOjtXlQZP-llJUCYnm8RSl2AiN0Dh6zE6Dg6vzFm1Y";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_tab);

        commentsContainer = findViewById(R.id.commentsContainer);
        toAccount = findViewById(R.id.to_profile_button);
        newComm = findViewById(R.id.toWriteButton);

        toAccount.setOnClickListener(v -> {
            Intent intent = new Intent(MainTab.this, Account.class);
            startActivity(intent);
        });

        newComm.setOnClickListener(v -> {
            Intent intent = new Intent(MainTab.this, CategoryChose.class);
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
                    commentsContainer.removeAllViews();

                    List<Comment> comments = response.body();
                    int limit = Math.min(10, comments.size());

                    for (int i = 0; i < limit; i++) {
                        addCommentBlock(comments.get(i));
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Comment>> call, Throwable t) {
            }
        });
    }

    private void addCommentBlock(Comment comment) {
        View view = getLayoutInflater().inflate(R.layout.comment_item, commentsContainer, false);

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.setMargins(8,8,8,8);
        view.setLayoutParams(lp);

        // Находим все элементы
        TextView tvProductName = view.findViewById(R.id.tvProductName);
        TextView tvComment = view.findViewById(R.id.tvComment);
        TextView tvPlusGrade = view.findViewById(R.id.tvPlusGrade);
        TextView tvMinusGrade = view.findViewById(R.id.tvMinusGrade);
        TextView tvDate = view.findViewById(R.id.tvDate);
        Button btnPlus = view.findViewById(R.id.btnPlus);
        Button btnMinus = view.findViewById(R.id.btnMinus);

        // Заполняем данными
        tvProductName.setText(comment.title != null ? comment.title : "Без названия");
        tvComment.setText(comment.comment != null ? comment.comment : "");
        tvPlusGrade.setText("+" + (comment.plus_grade != null ? comment.plus_grade : 0));
        tvMinusGrade.setText("-" + (comment.minus_grade != null ? comment.minus_grade : 0));
        tvDate.setText(comment.post_date != null ? comment.post_date : "");

        // Кнопка +
        btnPlus.setOnClickListener(v -> {
            int newValue = (comment.plus_grade != null ? comment.plus_grade : 0) + 1;
            comment.plus_grade = newValue;
            tvPlusGrade.setText("+" + newValue);
            btnPlus.setVisibility(View.GONE);
            btnMinus.setVisibility(View.GONE);
            updateCommentGrade(comment.id, newValue, null);
        });

        // Кнопка -
        btnMinus.setOnClickListener(v -> {
            int newValue = (comment.minus_grade != null ? comment.minus_grade : 0) + 1;
            comment.minus_grade = newValue;
            tvMinusGrade.setText("-" + newValue);
            btnPlus.setVisibility(View.GONE);
            btnMinus.setVisibility(View.GONE);
            updateCommentGrade(comment.id, null, newValue);
        });

        commentsContainer.addView(view);
    }

    private void updateCommentGrade(int commentId, Integer plus, Integer minus) {
        Zaprosi api = BdConnect.getInstance().create(Zaprosi.class);
        Map<String, Object> body = new HashMap<>();
        body.put("plus_grade", plus);
        body.put("minus_grade", minus);

        api.updateComment("Bearer " + apiKey, apiKey, commentId, body)
                .enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if (!response.isSuccessful()) {
                            Log.e("API", "Ошибка обновления: " + response.code());
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                    }
                });
    }
}
