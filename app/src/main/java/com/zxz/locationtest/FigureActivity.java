package com.zxz.locationtest;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by 亮剑 on 2017/5/12.
 */
public class FigureActivity extends AppCompatActivity {

    private MyView myView;

    private EiDraw eiDraw;

    private int Eangle;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getdrawparas();

        myView=new MyView(this);
        myView.setEiDraw(eiDraw);
        myView.setEangle(Eangle);

        setContentView(myView);
    }

    public void getdrawparas(){
        eiDraw=new EiDraw();
        eiDraw= (EiDraw) getIntent().getSerializableExtra("eiDraw");
        Eangle=getIntent().getIntExtra("Eangle",0);
    }
}
