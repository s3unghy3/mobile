package com.example.todocalendar;


import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import android.content.SharedPreferences;
//https://johnnn.tech/q/how-to-synchronize-spinners-i-e-month-and-year-with-the-calendar-view-in-android/
public class CalendarActivity extends AppCompatActivity implements CalendarAdapter.OnItemListener {


    public static final String YEAR = "_year";
    public static final String MON = "_mon";
    public static final String DAY = "_day";

    private TextView monthYearText;
    private RecyclerView calendarRecyclerView;
    private LocalDate selectedDate;


    final int DEFAULT_VALUE = 0;
    final String STORED_BG_KEY = "backgroundImageID";
    int storedBackground;
    SharedPreferences sharedPreferences;
    private final String sharedPrefFileName = "com.example.todocalendar.StoredBackgroundPref";
    LinearLayout mainPageBackground;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        initWidgets(); //Calendar RV and MonthYear TV
        selectedDate = LocalDate.now();
        setMonthView();

        mainPageBackground = (LinearLayout)findViewById(R.id.linearLayout_background);
        sharedPreferences = getSharedPreferences(sharedPrefFileName,MODE_PRIVATE);
        storedBackground = sharedPreferences.getInt(STORED_BG_KEY,0);

        //setBackgroundImage(R.drawable.main_background);
        if (storedBackground == DEFAULT_VALUE){
            setBackgroundImage(R.drawable.background1);
        }else {
            setBackgroundImage(storedBackground);
        }
        //get Intent from change background
        Intent intent1 = getIntent();
        int bgImg = intent1.getIntExtra("backgroundImage",DEFAULT_VALUE);
        if(bgImg != DEFAULT_VALUE){
            setBackgroundImage(bgImg);
            storedBackground = bgImg;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(STORED_BG_KEY, storedBackground);
        editor.apply();
    }

    public void setBackgroundImage(int bgImg){
        mainPageBackground.setBackgroundResource(bgImg);
    }


    private void initWidgets() {
        calendarRecyclerView = findViewById(R.id.calendarRecyclerView);
        monthYearText = findViewById(R.id.monthYearTV);
    }

    private void setMonthView() {
        monthYearText.setText(monthYearFromData(selectedDate));
        ArrayList<String> daysInMonth = daysInMonth(selectedDate);

        CalendarAdapter calendarAdapter = new CalendarAdapter(daysInMonth, this);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 7); //no.of columns
        calendarRecyclerView.setLayoutManager(layoutManager);
        calendarRecyclerView.setAdapter(calendarAdapter);
    }

    private ArrayList<String> daysInMonth(LocalDate date) {
        ArrayList<String> daysInMonthArray = new ArrayList<>();
        YearMonth yearMonth = YearMonth.from(date); //date-time object that represents the combination of a year and month.

        int daysInMonth = yearMonth.lengthOfMonth(); // lengthOfMonth(): returns the length of the month in days.

        LocalDate firstOfMonth = selectedDate.withDayOfMonth(1);
        int dayOfWeek = firstOfMonth.getDayOfWeek().getValue();

        for(int i = 1; i <= 42; i++) { //for calendarview every month includes 42 days
            if(i <= dayOfWeek || i > daysInMonth + dayOfWeek) {
                daysInMonthArray.add("");
            }
            else {
                daysInMonthArray.add(String.valueOf(i - dayOfWeek));
            }
        }

        return daysInMonthArray;
    }


    private String monthYearFromData(LocalDate date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM yyyy");
        return date.format(formatter);
    }

    private String monthFromData(LocalDate date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM");
        return date.format(formatter);
    }

    private String yearFromDate(LocalDate date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy");
        return date.format(formatter);
    }

    public void previousMonthAction(View view) {
        selectedDate = selectedDate.minusMonths(1);
        setMonthView();
    }

    public void nextMonthAction(View view) {
        selectedDate =  selectedDate.plusMonths(1);
        setMonthView();
    }

    @Override
    public void onItemClick(int position, String dayText) {
        if(dayText.length() == 0) {
            return;
        }

        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra(YEAR, yearFromDate(selectedDate));
        intent.putExtra(MON, monthFromData(selectedDate));
        intent.putExtra(DAY, dayText);
        startActivity(intent);

    }

    public void setOnChangeBgBtnClick(View view){
        Intent intent = new Intent(this,ChangeBackground.class);
        startActivity(intent);
    }

}
