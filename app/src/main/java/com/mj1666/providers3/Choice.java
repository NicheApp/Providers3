package com.mj1666.providers3;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class Choice extends AppCompatActivity {

    Button hire,work;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.choice);
        hire = findViewById(R.id.wh);
        work = findViewById(R.id.ww);
        hire.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Choice.this, MainActivity.class);
                startActivity(intent);
            }
        });
        work.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Choice.this, Form.class);
                startActivity(intent);
            }
        });

    }
}
