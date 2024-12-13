package com.example.appbansach.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.appbansach.R;

public class PaymentNotify extends AppCompatActivity {
    TextView tvNotify;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_notify);
        tvNotify = findViewById(R.id.tvNotify);

        Intent intent = getIntent();
        tvNotify.setText(intent.getStringExtra("result"));
    }
}