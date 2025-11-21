package com.chekizybra.otzovichok;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.chekizybra.otzovichok.database.BdConnect;
import com.chekizybra.otzovichok.database.Zaprosi;

public class Reg extends AppCompatActivity {

    private TextView fioTF, emailTF, passwordTF, passwordReplyTF;
    private Button regB, toLogB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        fioTF = findViewById(R.id.fioTextField);
        emailTF = findViewById(R.id.emailTextField);
        passwordTF = findViewById(R.id.passwordRegTextField);
        passwordReplyTF = findViewById(R.id.passwordReplayTextField);
    }


    public void register(){
        String apiKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6Imd4eGhodnB5bG5pdWl3Z2Vyc2x0Iiwicm9sZSI6ImFub24iLCJpYXQiOjE3NjM1NTgzNzQsImV4cCI6MjA3OTEzNDM3NH0.ywOjtXlQZP-llJUCYnm8RSl2AiN0Dh6zE6Dg6vzFm1Y";

        String fio = fioTF.toString();
        String email = emailTF.toString();
        String password = passwordTF.toString();
        String passwordRep = passwordReplyTF.toString();

        Zaprosi api = BdConnect.getInstance().create(Zaprosi.class);
        if (fio == null || email == null || password == null || passwordRep == null) {
            if (passwordRep == password) {
                try {
                    api.register(fio, email, password, apiKey);

                }

            }
            else {
                passwordTF.setTextColor(Integer.parseInt("#FF0000"));
                passwordReplyTF.setTextColor(Integer.parseInt("#FF0000"));
            }
        }
    }
}