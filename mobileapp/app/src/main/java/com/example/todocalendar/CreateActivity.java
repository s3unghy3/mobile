package com.example.todocalendar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.regex.Pattern;

import android.os.Bundle;

public class CreateActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String ID = "_id";
    public static final String YEAR = "_year";
    public static final String MON = "_mon";
    public static final String DAY = "_day";

    DBHelper dbHelper = new DBHelper(this);

    TextView memoNum;
    EditText memo, todo;
    Button startTime, endTime, save, delete;
    Toolbar toolbar;

    int sh = 8;
    int sm = 0;
    int eh = 9;
    int em = 0;
    int tf = 0;
    String year = null;
    String mon = null;
    String day = null;

    // store the received data
    long _id = 0;
    String _title = null;
    int _sh = 0;
    int _sm = 0;
    int _eh = 0;
    int _em = 0;
    String _memo = null;
    String _year = null;
    String _mon = null;
    String _day = null;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);
        // 툴바 설정 (뒤로 가기)
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        memoNum = findViewById(R.id.memoNum);
        memo = findViewById(R.id.memo);
        todo = findViewById(R.id.todo);
        startTime = findViewById(R.id.startTime);
        endTime = findViewById(R.id.endTime);
        save = findViewById(R.id.save);
        delete = findViewById(R.id.delete);

        startTime.setOnClickListener(this);
        endTime.setOnClickListener(this);
        save.setOnClickListener(this);
        delete.setOnClickListener(this);

        // number of letters
        memo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }

            @Override
            public void afterTextChanged(Editable s) {
                String input = memo.getText().toString();
                memoNum.setText(input.length()+"/90");
            }
        });

        Intent intent = getIntent();
        long id = intent.getLongExtra(ID, 0);
        if(id != 0) {  // (list item then) move on to the intent,
            DBHelper helper = new DBHelper(this);
            SQLiteDatabase db = helper.getReadableDatabase();
            Cursor cursor = db.rawQuery("select * from tb_data where id=" + id, null);

            while (cursor.moveToNext()) {
                _id = cursor.getLong(cursor.getColumnIndexOrThrow("id"));
                _title = cursor.getString(cursor.getColumnIndexOrThrow("title"));
                _sh = cursor.getInt(cursor.getColumnIndexOrThrow("start_h"));
                _sm = cursor.getInt(cursor.getColumnIndexOrThrow("start_m"));
                _eh = cursor.getInt(cursor.getColumnIndexOrThrow("end_h"));
                _em = cursor.getInt(cursor.getColumnIndexOrThrow("end_m"));
                _memo = cursor.getString(cursor.getColumnIndexOrThrow("memo"));
                _year = cursor.getString(cursor.getColumnIndexOrThrow("year"));
                _mon = cursor.getString(cursor.getColumnIndexOrThrow("mon"));
                _day = cursor.getString(cursor.getColumnIndexOrThrow("day"));
            }

            sh = _sh;
            sm = _sm;
            eh = _eh;
            em = _em;
            tf = TodoAdapter.tff;
            year = _year;
            mon = _mon;
            day = _day;

            // start time
            if (_sm < 10) {
                startTime.setText(_sh + ":0" + _sm);
            } else {
                startTime.setText(_sh + ":" + _sm);
            }
            // end time
            if (_em < 10) {
                endTime.setText(_eh + ":0" + _em);
            } else {
                endTime.setText(_eh + ":" + _em);
            }
            todo.setText(_title);
            memo.setText(_memo);

            helper.close();
            db.close();
            cursor.close();
        }
    }

    @Override
    public void onClick(View v) {
        if(v == startTime) {
            TimePickerDialog timePicker = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    // start time > end time
                    if(hourOfDay > eh || (hourOfDay == eh && minute >= em)) {
                        eh = hourOfDay + 1;
                        em = minute;

                        // no 24
                        if(eh >= 24) {
                            eh = 23;
                            em = 59;
                        }

                        if(minute < 10) {
                            endTime.setText(eh + ":0" + minute);
                        } else {
                            endTime.setText(eh + ":" + minute);
                        }
                    }

                    sh = hourOfDay;
                    sm = minute;

                    if(minute < 10) {
                        startTime.setText(hourOfDay + ":0" + minute);
                    } else {
                        startTime.setText(hourOfDay + ":" + minute);
                    }
                }
            }, sh, sm, false);
            timePicker.show();
        } else if(v == endTime) {
            TimePickerDialog timePicker = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    // end time < start time
                    if(hourOfDay < sh || (hourOfDay == sh && minute <= sm)) {
                        sh = hourOfDay - 1;
                        sm = minute;

                        // no 0
                        if(sh <= 0) {
                            sh = 0;
                            sm = 0;
                        }

                        if(minute < 10) {
                            startTime.setText(sh + ":0" + minute);
                        } else {
                            startTime.setText(sh + ":" + minute);
                        }
                    }

                    eh = hourOfDay;
                    em = minute;

                    if(minute < 10) {
                        endTime.setText(hourOfDay + ":0" + minute);
                    } else {
                        endTime.setText(hourOfDay + ":" + minute);
                    }
                }
            }, eh, em, false);
            timePicker.show();
        } else if(v == save) {
            Intent intent = getIntent();

            if(year == null) {
                year = intent.getStringExtra(YEAR);
                mon = intent.getStringExtra(MON);
                day = intent.getStringExtra(DAY);
            }
            String title = todo.getText().toString();
            String _memo = memo.getText().toString();
            long id = System.currentTimeMillis();

            // title is a must
            if(title.length() == 0) {
                Toast.makeText(this, "Please enter at least one letter in the Things to do", Toast.LENGTH_LONG).show();
                return;
            }

            dbHelper = new DBHelper(this);

            if(_id != 0) {
                dbHelper.update(_id, title, sh, sm, eh, em, _memo, tf, year, mon, day);
            } else {
                dbHelper.insert(id, title, sh, sm, eh, em, _memo, tf, year, mon, day);
            }
            dbHelper.close();

            setResult(RESULT_OK, intent);
            finish();
        } else if(v == delete) {
            Intent intent = new Intent();

            dbHelper = new DBHelper(this);
            dbHelper.delete(_id);
            dbHelper.close();

            setResult(RESULT_OK, intent);
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        dbHelper.close();
        super.onDestroy();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:{ //back in toolbar
                finish();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

}