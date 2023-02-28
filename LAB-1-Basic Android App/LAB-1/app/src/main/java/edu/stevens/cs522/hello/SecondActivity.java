package edu.stevens.cs522.hello;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class SecondActivity extends AppCompatActivity {

    TextView t1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        t1=(TextView)findViewById(R.id.textview);
        Intent in=getIntent();
        String res=in.getStringExtra("val");
        t1.setText(res);
    }
}