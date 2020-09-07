package com.example.suportetecia;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DarEmail extends AppCompatActivity /*implements View.OnClickListener*/ {

    EditText txtEmail;
    Button btnContinuar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dar_email);

        txtEmail = findViewById(R.id.txtEmail);
        btnContinuar = findViewById(R.id.btnContinuar);

        findViewById(R.id.btnContinuar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                  if (isEmailInvalid(txtEmail.getText().toString().trim())) {
                    txtEmail.setError("Insira um email valido!");
                    txtEmail.requestFocus();
                } else {
//                      Send Pass Code to user email
                      startActivity(new Intent(getApplicationContext(), InsertPassCode.class));
                }
            }
        });
    }

    private boolean isEmailInvalid(String email) {
        if (email.isEmpty())
            return true;
        Integer init = email.length() - "@etec.sp.gov.br".length();
        if (init < 0)
            return true;
        if ("@etec.sp.gov.br".compareTo(email.substring(init, "@etec.sp.gov.br".length())) != 0) {
            return true;
        }
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return !(matcher.matches());
    }
}