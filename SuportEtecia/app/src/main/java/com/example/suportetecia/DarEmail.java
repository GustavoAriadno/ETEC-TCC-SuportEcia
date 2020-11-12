package com.example.suportetecia;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DarEmail extends AppCompatActivity {
    private EditText txtEmail;
    private Button btnContinuar;
    private RequestQueue mQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dar_email);

        mQueue = Volley.newRequestQueue(getApplicationContext());
        txtEmail = findViewById(R.id.txtEmail);
        btnContinuar = findViewById(R.id.btnContinuar);
//                            Toast.makeText(getApplicationContext(), stringR, Toast.LENGTH_SHORT).show();

        btnContinuar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userEmail = txtEmail.getText().toString().trim();
                if (isEmailInvalid(userEmail)) {
                    txtEmail.setError("Insira um email valido!");
                    txtEmail.requestFocus();
                } else {
//                  Send Pass Code to user email
                    sendEmail(userEmail);
                }
            }
        });
    }

    private void changeAct(String fullHash, String userEmail) {
        Intent intent = new Intent(getApplicationContext(), InsertPassCode.class);
        intent.putExtra("FULL_HASH", fullHash);
        intent.putExtra("USER_EMAIL", userEmail);
        startActivity(intent);
    }

    private void sendEmail(final String userEmail) {
        String url = "https://suportecia.herokuapp.com/public/sendemail";
        //      Define request body
        Map<String, String> reqBody = new HashMap<String, String>();
        reqBody.put("email", userEmail);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(reqBody),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            changeAct(response.getString("fullHash"), userEmail);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }

        });
        mQueue.add(request);
    }

    private boolean isEmailInvalid(String email) {
        if (email.isEmpty())
            return true;
        int init = email.length() - "@etec.sp.gov.br".length();
        if (init < 0)
            return true;
        if ("@etec.sp.gov.br".compareTo(email.substring(init)) != 0) {
            return true;
        }
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return !(matcher.matches());
    }
}