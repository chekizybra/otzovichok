package com.chekizybra.otzovichok;

import android.os.Bundle;
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

    private TextView allCommTextField;
    private String apiKey = "YOUR_ANON_KEY_HERE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        allCommTextField = findViewById(R.id.allCommTextField);

        loadUserComments();
    }

    private void loadUserComments() {
        Zaprosi api = BdConnect.getInstance().create(Zaprosi.class);

        // фильтруем по текущему пользователю
        Call<List<Comment>> call = api.getComment("Bearer " + apiKey, apiKey, SessionData.currentUserId)
        );

        call.enqueue(new Callback<List<Comment>>() {
            @Override
            public void onResponse(Call<List<Comment>> call, Response<List<Comment>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    StringBuilder sb = new StringBuilder();
                    for (Comment com : response.body()) {
                        sb.append(com.product_name + "\n").append(com.comment).append("\n\n");
                    }
                    allCommTextField.setText(sb.toString());
                } else {
                    Toast.makeText(Account.this, "Не удалось загрузить отзывы: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Comment>> call, Throwable t) {
                Toast.makeText(Account.this, "Сетевая ошибка: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
