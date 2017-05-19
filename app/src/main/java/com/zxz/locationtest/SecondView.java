package com.zxz.locationtest;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.Log;
import android.view.View;

/**
 * Created by 亮剑 on 2017/5/18.
 */
public class SecondView extends View {

    private EiDraw eiDraw;

    private int Eangle;

    public EiDraw getEiDraw() {
        return eiDraw;
    }

    public void setEiDraw(EiDraw eiDraw) {
        this.eiDraw = eiDraw;
    }

    public int getEangle() {
        return Eangle;
    }

    public void setEangle(int eangle) {
        Eangle = eangle;
    }

    public SecondView(Context context) {
        super(context);
    }

    protected void onDraw(Canvas canvas){
        super.onDraw(canvas);
        canvas.drawColor(Color.WHITE);

        int ScreenWindth = this.eiDraw.getWidth();
        int ScreenHeight = this.eiDraw.getHeight();

        int GAP = this.eiDraw.getGAP();

        int Origin_X = GAP;
        int Origin_Y = GAP;

        int Axis_IDX_length = ScreenWindth - 2 * GAP;
        int Axis_tY_length = ScreenHeight - 2 * GAP;

        //绘制坐标轴
        Paint paint_Axis = new Paint();
        paint_Axis.setAntiAlias(true);
        paint_Axis.setColor(Color.BLACK);
        paint_Axis.setStyle(Paint.Style.STROKE);
        paint_Axis.setStrokeWidth(3);

        Path path_Axis_EX = new Path();
        path_Axis_EX.moveTo(Origin_X, Origin_Y);
        path_Axis_EX.lineTo(ScreenWindth -   GAP, Origin_Y);
        path_Axis_EX.lineTo((float) (ScreenWindth - 1.5 * GAP), Origin_Y - GAP / 4);
        path_Axis_EX.moveTo(ScreenWindth -  GAP, Origin_Y);
        path_Axis_EX.lineTo((float) (ScreenWindth - 1.5* GAP), Origin_Y + GAP / 4);
        path_Axis_EX.close();
        canvas.drawPath(path_Axis_EX, paint_Axis);

        Path path_Axis_tY = new Path();
        path_Axis_tY.moveTo(Origin_X, Origin_Y);
        path_Axis_tY.lineTo(Origin_X, ScreenHeight - 2 * GAP);
        path_Axis_tY.lineTo(Origin_X + GAP / 2, ScreenHeight - 3 * GAP);
        path_Axis_tY.moveTo(Origin_X, ScreenHeight - 2 * GAP);
        path_Axis_tY.lineTo(Origin_X - GAP / 2, ScreenHeight - 3 * GAP);
        path_Axis_tY.close();
        canvas.drawPath(path_Axis_tY, paint_Axis);

        //VisibleAmount可见卫星数量，AllData所有的高度角数据，SID卫星ID
        // TimeNum时段数也就是分割的个数,length每一段的长度
        int VisibleAmount = this.eiDraw.getSearchnum().length;
        double[][] AllData = this.eiDraw.getE();
        int[] SID = this.eiDraw.getSid();
        int TimeNum = AllData[0].length;
        int length=Axis_tY_length/TimeNum;
        int height=Axis_IDX_length/(VisibleAmount+1);

        //对于每一个可见卫星
        for (int i=0;i<VisibleAmount;i++){
            Paint paint_line=new Paint();
            paint_line.setColor(Color.BLACK);
            paint_line.setAntiAlias(true);
            paint_line.setStyle(Paint.Style.STROKE);
            paint_line.setStrokeWidth(8);

            int satellite=this.eiDraw.getSearchnum(i);
            double[] Data=AllData[satellite];
            //对于每一个时段
            for (int j=0;j<TimeNum-1;j++){
                double data_now=Data[j];
                double data_next=Data[j+1];
                if (data_now>0 && data_next>0){
                    canvas.drawLine(Origin_X+height*(i+1),Origin_Y+j*length,Origin_X+height*(i+1),Origin_Y+(j+1)*length,paint_line);
                }
                else if (data_now>0 && data_next<0){
                    double loc=length*((data_now)/(data_now-data_next));
                    canvas.drawLine(Origin_X+height*(i+1),Origin_Y+j*length,Origin_X+height*(i+1), (float) (Origin_Y+j*length+loc),paint_line);
                }
                else if (data_now<0 && data_next>0){
                    double loc=length*((-data_now)/(data_next-data_now));
                    canvas.drawLine(Origin_X+height*(i+1), (float) (Origin_Y+j*length+loc),Origin_X+height*(i+1),Origin_Y+(j+1)*length,paint_line);
                }
                else {}
            }

        }

        //绘制坐标轴零件
        for (int i=0;i<TimeNum;i++){
            canvas.drawLine(Origin_X,Origin_Y+length*i,Origin_X+8,Origin_Y+length*i,paint_Axis);
        }
        for (int i=0;i<VisibleAmount;i++){
            canvas.drawLine(Origin_X+(i+1)*height,Origin_Y,Origin_X+(i+1)*height,Origin_Y-8,paint_Axis);
        }

        Paint paint_text = new Paint();
        paint_text.setColor(Color.BLACK);
        paint_text.setTextSize(25);
        paint_text.setStrokeWidth(3);
        int starttime=this.eiDraw.getStarttime();
        for (int i=0;i<TimeNum;i+=2){
            drawText(canvas,String.valueOf(starttime+i/2)+":00",Origin_X-20,Origin_Y+i*length-25,paint_text,90);
        }
        for (int i=0;i<VisibleAmount;i++){
            drawText(canvas, String.valueOf(SID[i]),Origin_X+(i+1)*height-10,Origin_Y-35,paint_text,90);
        }

        drawText(canvas,"时间/h",Origin_X+GAP, (float) (ScreenHeight - 3.5 * GAP),paint_text,90);
        drawText(canvas,"卫星号", (float) (ScreenWindth - 1.5 * GAP),Origin_Y+20,paint_text,90);
        paint_text.setTextSize(50);
        drawText(canvas,"卫星可视性图",(float) (ScreenWindth - 1.5 * GAP),ScreenHeight/2-150,paint_text,90);
    }

    void drawText(Canvas canvas ,String text , float x ,float y,Paint paint ,float angle){
        if(angle != 0){
            canvas.rotate(angle, x, y);
        }
        canvas.drawText(text, x, y, paint);
        if(angle != 0){
            canvas.rotate(-angle, x, y);
        }
    }
}
