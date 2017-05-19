package com.zxz.locationtest;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by 亮剑 on 2017/5/18.
 */
public class PdopFigureActivity extends AppCompatActivity {
    private EiDraw eiDraw;
    private PdopView pdopView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        eiDraw=new EiDraw();
        eiDraw= (EiDraw) getIntent().getSerializableExtra("eiDraw");

        pdopView=new PdopView(this);
        pdopView.setEiDraw(eiDraw);
        setContentView(pdopView);
    }
}
