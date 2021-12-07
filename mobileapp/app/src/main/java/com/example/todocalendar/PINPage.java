package com.example.todocalendar;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class PINPage extends AppCompatActivity {


    EditText editPassword;
    Button startBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pinpage);

        editPassword = findViewById(R.id.editPassword);
        startBtn = findViewById(R.id.start);

    }


    public void setStartBtnOnClick(View view){

        String password = editPassword.getText().toString();

        if (password.equals("mypw")){

          Intent intent = new Intent(PINPage.this,CalendarActivity.class);
          startActivity(intent);

        } else if(password.isEmpty()){

            Toast.makeText(PINPage.this, "Please enter your PIN", Toast.LENGTH_SHORT).show();

        } else

            Toast.makeText(PINPage.this, "Incorrect PIN provided", Toast.LENGTH_SHORT).show();

    }

}

