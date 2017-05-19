package com.zxz.locationtest;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

/**
 * Created by 亮剑 on 2017/5/10.
 */
public class ThirdActivity extends AppCompatActivity {

    private SatelliteData satelliteData;

    private double Longtitude;
    private double Latitude;

    private TimeParameters timeParameters;

    private CalculateThread calculateThread;

    private EiDraw eiDraw;

    private int Eangle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_draw);

        serparas();

        calculateThread=new CalculateThread();
        calculateThread.setLatitude(Latitude);
        calculateThread.setLongtitude(Longtitude);
        calculateThread.setEiDraw(eiDraw);
        calculateThread.setTimeParameters(timeParameters);
        calculateThread.setSatelliteData(satelliteData);

        calculateThread.start();
        Log.i("ThirdActivity","thread run");
    }

    public void serparas(){
        satelliteData=new SatelliteData();
        timeParameters=new TimeParameters();
        satelliteData = (SatelliteData) getIntent().getSerializableExtra("satelliteData");
        String Longtitude1 = getIntent().getStringExtra("Longtitude");
        String Latitude1 = getIntent().getStringExtra("Latitude");
        Latitude=Double.valueOf(Latitude1);
        Longtitude=Double.valueOf(Longtitude1);
        timeParameters= (TimeParameters) getIntent().getSerializableExtra("timeParameters");
        String EAngle=getIntent().getStringExtra("EAngle");
        String e=EAngle.substring(0,2);
        Eangle=Integer.valueOf(e);

        eiDraw=new EiDraw();
    }

    public void Back2second(View view){
        this.finish();
    }

    public void drawfigure(View view){
        eiDraw=calculateThread.getEiDraw();
        DisplayMetrics displayMetrics=new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int windth=displayMetrics.widthPixels;
        int height=displayMetrics.heightPixels;
        int starttime=calculateThread.getTimeParameters().getIntervaltime();
        eiDraw.setWidth(windth);
        eiDraw.setHeight(height);
        eiDraw.setStarttime(starttime);

        Intent intent=new Intent(this,FigureActivity.class);
        intent.putExtra("eiDraw",eiDraw);
        intent.putExtra("Eangle",Eangle);
        startActivity(intent);
    }

    public void drawafigure(View view){
        eiDraw=calculateThread.getEiDraw();
        DisplayMetrics displayMetrics=new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int windth=displayMetrics.widthPixels;
        int height=displayMetrics.heightPixels;
        int starttime=calculateThread.getTimeParameters().getIntervaltime();
        eiDraw.setWidth(windth);
        eiDraw.setHeight(height);
        eiDraw.setStarttime(starttime);

        Intent intent=new Intent(this,AnotherFiguerActivity.class);
        intent.putExtra("eiDraw",eiDraw);
        intent.putExtra("Eangle",Eangle);
        startActivity(intent);
    }

    public void drawPDOP(View view){
        eiDraw=calculateThread.getEiDraw();
        DisplayMetrics displayMetrics=new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int windth=displayMetrics.widthPixels;
        int height=displayMetrics.heightPixels;
        int starttime=calculateThread.getTimeParameters().getIntervaltime();
        eiDraw.setWidth(windth);
        eiDraw.setHeight(height);
        eiDraw.setStarttime(starttime);

        Intent intent=new Intent(this,PdopFigureActivity.class);
        intent.putExtra("eiDraw",eiDraw);
        startActivity(intent);
    }

}
