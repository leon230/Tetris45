package com.example.leon.tetris;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity implements View.OnClickListener{

    Intent intent;
    Button btnPlay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnPlay = (Button)findViewById(R.id.btnPlay);
        btnPlay.setText(getResources().getString(R.string.btnPlay));
        btnPlay.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        intent = new Intent(this,GameActivity.class);
        startActivity(intent);
    }
}
