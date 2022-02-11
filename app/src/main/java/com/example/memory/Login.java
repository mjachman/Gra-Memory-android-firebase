package com.example.memory;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;


public class Login extends AppCompatActivity {
    private FirebaseAuth mAuth;




    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final EditText playerName= findViewById(R.id.playerName);
        final Button startButton=findViewById(R.id.startGame);

        startButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                final String getPlayerName=playerName.getText().toString();

                if(getPlayerName.isEmpty())
                {
                    Toast.makeText(Login.this,"Podaj nazwę",Toast.LENGTH_SHORT).show();
                }
                else
                    {
                    Intent intent=new Intent(Login.this,MainActivity.class);

                    intent.putExtra("playerName",getPlayerName);
                    startActivity(intent);
                    finish();
                }
            }
        });

    }

}