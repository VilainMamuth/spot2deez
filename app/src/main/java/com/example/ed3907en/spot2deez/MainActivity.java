package com.example.ed3907en.spot2deez;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent i = getIntent();

        Uri url = i.getData();

        TextView source = (TextView) findViewById(R.id.sourceLocation);
    source.setText(url.toString());



    }
}
