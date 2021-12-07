package com.example.todocalendar;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;

public class ChangeBackground extends AppCompatActivity {



    ListView listView;
    ArrayAdapter<String> adapter;
    ArrayList<String> backgrounds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_background);

        String[] backgroundOptions = getResources().
                getStringArray(R.array.Background_array);
        backgrounds = new ArrayList<>(Arrays.asList(backgroundOptions));

        listView = findViewById(R.id.listView);
        adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, backgrounds);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Intent intent = new Intent(ChangeBackground.this,CalendarActivity.class);
                switch (i){
                    case 0:
                        intent.putExtra("backgroundImage", R.drawable.background1);
                        break;
                    case 1:
                        intent.putExtra("backgroundImage",R.drawable.background2);
                        break;
                    case 2:
                        intent.putExtra("backgroundImage",R.drawable.background3);
                        break;
                    case 3:
                        intent.putExtra("backgroundImage",R.drawable.background4);
                        break;
                }
                startActivity(intent);
            }
        });
    }

}