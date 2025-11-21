package com.chekizybra.otzovichok;

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

    private TextView commentNameTextField, commentTextTextField;
    private Button toWriteButton;
    private String apiKey = "YOUR_ANON_KEY_HERE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_comment);

        commentNameTextField = findViewById(R.id.commentNameTextField);
        commentTextTextField = findViewById(R.id.commentTextTextField);
        toWriteButton = findViewById(R.id.to_write_button);

        toWriteButton.setOnClickListener(v -> submitComment());
    }

    private void submitComment() {
        String name = commentNameTextField.getText().toString();
        String text = commentTextTextField.getText().toString();

        Zaprosi api = BdConnect.getInstance().create(Zaprosi.class);

        Map<String, String> body = new HashMap<>();
        body.put("user_id", String.valueOf(SessionData.currentUserId));
        body.put("name", name);
        body.put("text", text);

        Call<Void> call = api.addComment("Bearer " + apiKey, apiKey, body);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(response.isSuccessful()){
                    Toast.makeText(WriteComment.this, "Комментарий добавлен", Toast.LENGTH_SHORT).show();
                    commentNameTextField.setText("");
                    commentTextTextField.setText("");
                } else {
                    Toast.makeText(WriteComment.this, "Ошибка: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(WriteComment.this, "Сетевая ошибка: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
