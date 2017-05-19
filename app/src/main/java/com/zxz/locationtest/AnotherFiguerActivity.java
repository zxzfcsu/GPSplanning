package com.zxz.locationtest;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by 亮剑 on 2017/5/18.
 */
public class AnotherFiguerActivity extends AppCompatActivity {
    private SecondView secondView;
    private EiDraw eiDraw;
    private int Eangle;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        eiDraw=new EiDraw();
        eiDraw= (EiDraw) getIntent().getSerializableExtra("eiDraw");
        Eangle=getIntent().getIntExtra("Eangle",0);
        secondView=new SecondView(this);
        secondView.setEiDraw(eiDraw);
        secondView.setEangle(Eangle);

        setContentView(secondView);
    }
}
