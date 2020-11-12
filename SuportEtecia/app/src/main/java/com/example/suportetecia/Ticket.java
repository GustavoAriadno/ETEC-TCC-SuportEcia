package com.example.suportetecia;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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

public class Ticket extends AppCompatActivity {


    private RequestQueue mQueue;

    String local;
    String sigla;
    String userEmail;

    EditText etProblema;
    Button btnSendTicket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket);

        mQueue = Volley.newRequestQueue(getApplicationContext());

        local = getIntent().getStringExtra("MAQUINA_LOCAL");
        sigla = getIntent().getStringExtra("MAQUINA_SIGLA");
        userEmail = getIntent().getStringExtra("USER_EMAIL");

        TextView tvLocal = findViewById(R.id.txtLocal);
        tvLocal.setText(local);

        TextView tvSigla = findViewById(R.id.txtSigla);
        tvSigla.setText(sigla);

        etProblema = findViewById(R.id.txtProblema);
        btnSendTicket = findViewById(R.id.btnSendTicket);
        btnSendTicket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String problema = etProblema.getText().toString().trim();
                if (problema.compareTo("") == 0) {
                    etProblema.setError("Insira algum problema!");
                    etProblema.requestFocus();
                } else {
                    createTicket(problema, userEmail, sigla);
                }
            }
        });
    }

    private void createTicket(String problema, String userEmailM, String siglaM) {
        String url = "http://suportecia.herokuapp.com/public/chamado";
        //      Define request body
        Map<String, String> reqBody = new HashMap<String, String>();
        reqBody.put("problema", problema);
        reqBody.put("email", userEmailM);
        reqBody.put("sigla", siglaM);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(reqBody),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            closeApplication(response.getInt("status"));
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

    private void closeApplication(int st) {
        Toast.makeText(getApplicationContext(), "Chamado "+st+" enviado :D", Toast.LENGTH_SHORT).show();
    }
}