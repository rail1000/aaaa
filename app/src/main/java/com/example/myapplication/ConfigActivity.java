package com.example.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class ConfigActivity extends AppCompatActivity {
    EditText money_a;
    EditText money_b;
    EditText money_c;
    EditText money_d;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config);
        Intent intent = getIntent();
        double rate_a = intent.getDoubleExtra("rate_a",0.0);
        double rate_b = intent.getDoubleExtra("rate_b",0.0);
        double rate_c = intent.getDoubleExtra("rate_c",0.0);
        double rate_d = intent.getDoubleExtra("rate_d",0.0);

        money_a = (EditText)findViewById(R.id.a);
        money_b = (EditText)findViewById(R.id.b);
        money_c = (EditText)findViewById(R.id.c);
        money_d = (EditText)findViewById(R.id.d);

        money_a.setText(""+rate_a);
        money_b.setText(""+rate_b);
        money_c.setText(""+rate_c);

    }


    public void save(View btn){
        //获取新的值
Double new_a=Double.parseDouble(money_a.getText().toString());
        Double new_b=Double.parseDouble(money_b.getText().toString());
        Double new_c=Double.parseDouble(money_c.getText().toString());
//
        Intent intent=new Intent();
        Bundle bul=new Bundle();
        bul.putDouble("rate_a",new_a);
        bul.putDouble("rate_b",new_b);
        bul.putDouble("rate_c",new_c);

        intent.putExtras(bul);

        setResult(2,intent);

        finish();

    }
}
