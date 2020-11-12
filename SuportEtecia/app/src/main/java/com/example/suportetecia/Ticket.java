package com.example.suportetecia;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

public class Ticket extends AppCompatActivity {

    String local;
    String sigla;
    String userEmail;

    Button btnSendTicket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket);

        local = getIntent().getStringExtra("MAQUINA_LOCAL");
        sigla = getIntent().getStringExtra("MAQUINA_SIGLA");
        userEmail = getIntent().getStringExtra("USER_EMAIL");

        TextView tvLocal = findViewById(R.id.txtLocal);
        tvLocal.setText(local);

        TextView tvSigla = findViewById(R.id.txtSigla);
        tvSigla.setText(sigla);

        btnSendTicket = findViewById(R.id.btnSendTicket);
//        btnSendTicket.setOnClickListener();
    }
}