package com.example.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class SecondActivity extends AppCompatActivity {

    EditText money_a;
    EditText money_b;
    EditText money_c;
    EditText money_d;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aaa);

        Intent intent = getIntent();
        double rate_a = intent.getDoubleExtra("rate_a",0.0);
        double rate_b = intent.getDoubleExtra("rate_b",0.0);
        double rate_c = intent.getDoubleExtra("rate_c",0.0);
        double rate_d = intent.getDoubleExtra("rate_d",0.0);

        money_a = (EditText)findViewById(R.id.a);
        money_b = (EditText)findViewById(R.id.b);
        money_c = (EditText)findViewById(R.id.c);
        money_d = (EditText)findViewById(R.id.d);

    }


    public void onclick(View btn) {





    }

}
