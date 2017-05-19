package com.zxz.locationtest;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CalendarView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

/**
 * Created by 亮剑 on 2017/5/8.
 */
public class SecondActivity extends AppCompatActivity {

    private SatelliteData satelliteData;

    private TextView tvWeek;
    private Spinner spTimeSeparate;
    private Spinner spTimeDuration;
    private Spinner spTimeZone;
    private Spinner spEAngle;

    private CalendarView calendarView;

    private String Longtitude;
    private String Latitude;

    private String IntervalTime;
    private String Duration;
    private String UTC;
    private int Year, Month, Day;
    private String EAngle;

    private TimeParameters timeParameters;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time);

        setcontrol();
        setTimeSeparate();
        setTimeDuration();
        setTimeZone();
        setCalendar();
        setEAngle();
    }

    public void setcontrol() {
        satelliteData = (SatelliteData) getIntent().getSerializableExtra("data");
        Longtitude = getIntent().getStringExtra("Longtitude");
        Latitude = getIntent().getStringExtra("Latitude");

        tvWeek = (TextView) findViewById(R.id.tvWeek);
        tvWeek.setText(String.valueOf(satelliteData.getSWeek()));

        spTimeSeparate = (Spinner) findViewById(R.id.spTimeSeparate);
        spTimeDuration = (Spinner) findViewById(R.id.spTimeDuration);
        spTimeZone = (Spinner) findViewById(R.id.spTimeZone);
        spEAngle= (Spinner) findViewById(R.id.spEAngle);

        calendarView = (CalendarView) findViewById(R.id.calendarView);
    }

    public void setTimeSeparate() {
        String[] mItems = getResources().getStringArray(R.array.TimeSeparate);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, mItems);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //绑定 Adapter到控件
        spTimeSeparate.setAdapter(adapter);
        spTimeSeparate.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String[] TS = getResources().getStringArray(R.array.TimeSeparate);
                Log.i("spListener", "你点击的是:" + TS[position]);
                IntervalTime = TS[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Log.i("spListener", "nothing changged");
                IntervalTime = null;
            }
        });

    }

    public void setTimeDuration() {
        String[] mItems = getResources().getStringArray(R.array.TimeDuration);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, mItems);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //绑定 Adapter到控件
        spTimeDuration.setAdapter(adapter);
        spTimeDuration.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String[] TD = getResources().getStringArray(R.array.TimeDuration);
                Log.i("spListener", "你点击的是:" + TD[position]);
                Duration = TD[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Log.i("spListener", "nothing changged");
                Duration = null;
            }
        });
    }

    public void setTimeZone() {
        String[] mItems = getResources().getStringArray(R.array.TimeZone);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, mItems);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //绑定 Adapter到控件
        spTimeZone.setAdapter(adapter);
        spTimeZone.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String[] TZ = getResources().getStringArray(R.array.TimeZone);
                Log.i("spListener", "你点击的是:" + TZ[position]);
                UTC = TZ[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Log.i("spListener", "nothing changged");
                UTC = null;
            }
        });
    }

    public void setEAngle(){
        String[] mItems=getResources().getStringArray(R.array.EAngle);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, mItems);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //绑定 Adapter到控件
        spEAngle.setAdapter(adapter);
        spEAngle.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String[] TB = getResources().getStringArray(R.array.EAngle);
                Log.i("spListener", "你点击的是:" + TB[position]);
                EAngle = TB[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Log.i("spListener", "nothing changged");
                EAngle = null;
            }
        });
    }

    public void setCalendar() {

        Calendar timenow = Calendar.getInstance();
        int year1=timenow.get(Calendar.YEAR);
        int month1=timenow.get(Calendar.MONTH)+1;
        int day1=timenow.get(Calendar.DAY_OF_MONTH);
        Year = year1;
        Month = month1;
        Day = day1;

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                month = month + 1;
                Log.i("SecondActivity", year + "-" + month + "-" + dayOfMonth);
                Year = year;
                Month = month;
                Day = dayOfMonth;
            }
        });
    }

    public void Back2first(View view) {
        this.finish();
    }

    public void Forward(View view) {
        if (IntervalTime.equals(null) || Duration.equals(null) || UTC.equals(null)) {
            Toast.makeText(this, "请重新设置时间参数", Toast.LENGTH_LONG).show();
        } else {
            timeParameters=new TimeParameters();

            timeParameters.setYMD(Year,Month,Day);
            String utc=UTC.substring(4,6);
            String duration=Duration.substring(0,2);
            String intervaltime=IntervalTime.substring(0,2);
            int Discretization=30;
            timeParameters.setUTC(Integer.valueOf(utc));
            timeParameters.setDuration(Integer.valueOf(duration));
            timeParameters.setIntervaltime(Integer.valueOf(intervaltime));
            timeParameters.setDiscretization(Discretization);
            timeParameters.TimeCalculate();
            timeParameters.TimeDiscretization();

            Intent intent = new Intent(this, ThirdActivity.class);
            intent.putExtra("timeParameters",timeParameters);
            intent.putExtra("satelliteData",satelliteData);
            intent.putExtra("Latitude",Latitude);
            intent.putExtra("Longtitude",Longtitude);
            intent.putExtra("EAngle",EAngle);
            startActivity(intent);
        }
    }
}
