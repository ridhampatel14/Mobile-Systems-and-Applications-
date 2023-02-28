package edu.stevens.cs522.hello;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    Button b1;
    EditText e1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        b1=(Button)findViewById(R.id.button);
        e1=(EditText)findViewById(R.id.textbox);
        b1.setOnClickListener(view -> {
            String str=e1.getText().toString();
            Intent in=new Intent(MainActivity.this,SecondActivity.class);
            in.putExtra("val",str);
            startActivity(in);
        });
    }
}