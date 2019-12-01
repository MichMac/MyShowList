package com.example.myshowlist;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toast.makeText(this,"firebase connection success",Toast.LENGTH_LONG).show();

        Button AddButton = (Button) findViewById(R.id.add_custom_button);

        AddButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                startActivity(new Intent(MainActivity.this, AddShow.class));
            }
        });
    }
    /*public void sendMessage(View view)
    {
        Intent intent = new Intent(this, AddShow.class);
        startActivity(intent);
    }
    */
}


