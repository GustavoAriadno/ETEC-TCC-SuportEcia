package com.example.suportetecia;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
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
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class InsertPassCode extends AppCompatActivity {

    EditText editText;
    Button scanBtn;
    TextView textView;
    private RequestQueue mQueue;

    String fullHash;
    String userEmail;
    String otp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_pass_code);

        mQueue = Volley.newRequestQueue(getApplicationContext());
        fullHash = getIntent().getStringExtra("FULL_HASH");
        userEmail = getIntent().getStringExtra("USER_EMAIL");

        textView = findViewById(R.id.tvShow);
        editText = findViewById(R.id.txtPassCode);
        scanBtn = findViewById(R.id.btnScan);
        scanBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                textView.setText(fullHash);
                otp = editText.getText().toString().trim();
//              Check OTP
                checkOtp(otp, userEmail, fullHash);
            }
        });
    }

    private void checkOtp(final String otp, final String userEmail, final String fullHash) {
        String url = "https://suportecia.herokuapp.com/public/verifyotp";
//      Define request body
        Map<String, String> reqBody = new HashMap<String, String>();
        reqBody.put("otp", otp);
        reqBody.put("email", userEmail);
        reqBody.put("fullHash", fullHash);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(reqBody),
            new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        preStartScan(response.getInt("status"));
                    } catch (JSONException e) {
                        textView.append("n funfou ");
                        Toast.makeText(getApplicationContext(), "n funfo", Toast.LENGTH_SHORT).show();
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

    private void preStartScan(int status) {
        if (status == 1) {
            scanCode();
        } else if (status == 0) {
            editText.setError("Sua senha passe expirou");
            editText.requestFocus();
        } else {
            editText.setError("Sua senha passe esta incorreta");
            editText.requestFocus();
        }
    }

    private void scanCode() {
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setCaptureActivity(CaptureAct.class);
        integrator.setOrientationLocked(false);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
        integrator.setPrompt("Scaneie o codigo de barras");
        integrator.initiateScan();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() != null) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage(result.getContents());
                builder.setTitle("Titulo");
                builder.setPositiveButton("Scan Again", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        scanCode();
                    }
                }).setNegativeButton("Finish", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            } else {
                Toast.makeText(this, "No Results", Toast.LENGTH_LONG).show();
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}