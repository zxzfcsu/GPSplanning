package com.zxz.locationtest;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.CornerPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.view.View;

/**
 * Created by 亮剑 on 2017/5/12.
 */
public class MyView extends View {
    private EiDraw eiDraw;

    private int Eangle;

    public int getEangle() {
        return Eangle;
    }

    public void setEangle(int eangle) {
        Eangle = eangle;
    }

    public MyView(Context context) {
        super(context);
    }

    public EiDraw getEiDraw() {
        return eiDraw;
    }

    public void setEiDraw(EiDraw eiDraw) {
        this.eiDraw = eiDraw;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(Color.WHITE);

        int ScreenWindth = this.eiDraw.getWidth();
        int ScreenHeight = this.eiDraw.getHeight();

        int GAP = this.eiDraw.getGAP();

        int Origin_X = GAP;
        int Origin_Y = GAP;

        int Axis_EX_length = ScreenWindth - 2 * GAP;
        int Axis_tY_length = ScreenHeight - 2 * GAP;

        //绘制坐标轴
        Paint paint_Axis = new Paint();
        paint_Axis.setAntiAlias(true);
        paint_Axis.setColor(Color.BLACK);
        paint_Axis.setStyle(Paint.Style.STROKE);
        paint_Axis.setStrokeWidth(3);

        Path path_Axis_EX = new Path();
        path_Axis_EX.moveTo(Origin_X, Origin_Y);
        path_Axis_EX.lineTo(ScreenWindth - 2 * GAP, Origin_Y);
        path_Axis_EX.lineTo(ScreenWindth - 3 * GAP, Origin_Y - GAP / 2);
        path_Axis_EX.moveTo(ScreenWindth - 2 * GAP, Origin_Y);
        path_Axis_EX.lineTo(ScreenWindth - 3 * GAP, Origin_Y + GAP / 2);
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

        //绘制图像
        int LineAmount = this.eiDraw.getSearchnum().length;
        double[][] AllPoint = this.eiDraw.getE();
        int[] SID = this.eiDraw.getSid();
        int PointNum = AllPoint[0].length;

        int Each_EX = Axis_EX_length / 90;
        int Each_tY = Axis_tY_length / PointNum;


        for (int i = 0; i < LineAmount; i++) {

            Paint paint_Line=new Paint();
            int t=20;
            int color=0;
            if (i*t>255){
                color=Color.argb(255,0,i*t-255,i*5);
            }
            else {
                color=Color.argb(120,i*10,0,i*5);
            }
            paint_Line.setColor(color);
            paint_Line.setAntiAlias(true);
            paint_Line.setStyle(Paint.Style.STROKE);
            paint_Line.setStrokeWidth(3);
            paint_Line.setPathEffect(new CornerPathEffect(50));

            int row = this.eiDraw.getSearchnum(i);

            double[] Point = AllPoint[row];
            int ID = SID[row];
            Path path = new Path();

            int StartNum = 0;

            for (int h = 0; h < PointNum; h++) {
                if (Point[h] > 0) {
                    path.moveTo((float) (Point[h] * Each_EX + Origin_X), h * Each_tY + Origin_Y);
                    StartNum = h;
                    break;
                }
            }

            for (int k = StartNum + 1; k < PointNum; k++) {
                if (Point[k]>0){
                    path.lineTo((float) (Point[k]*Each_EX+Origin_X),k*Each_tY+Origin_Y);
                }
                else
                    break;
            }

            canvas.drawPath(path,paint_Line);
        }

        if (this.Eangle!=0){
            Paint paint_rectangle=new Paint();
            paint_rectangle.setColor(Color.WHITE);
            canvas.drawRect(Origin_X+3,Origin_Y+3,Origin_X+this.Eangle*Each_EX,ScreenHeight-3*GAP,paint_rectangle);
        }

        //绘制坐标轴上的事物
        int length=8;
        for (int i=1;i<18;i++){
            canvas.drawLine(i*5*Each_EX+Origin_X,Origin_Y,i*5*Each_EX+Origin_X,Origin_Y-length,paint_Axis);
        }
        for (int i=1;i<PointNum;i++){
            canvas.drawLine(Origin_X,Origin_Y+i*Each_tY,Origin_X+length,Origin_Y+i*Each_tY,paint_Axis);
        }

        //绘制文字
        Paint paint_text = new Paint();
        paint_text.setColor(Color.BLACK);
        paint_text.setTextSize(25);
        paint_text.setStrokeWidth(3);
        int starttime=this.eiDraw.getStarttime();
        for (int i=0;i<PointNum;i+=2){
            drawText(canvas, String.valueOf(starttime+i/2)+":00", Origin_X-20, Origin_Y+i*Each_tY-20, paint_text,90);
        }
        for (int i=0;i<18;i+=2){
            drawText(canvas, String.valueOf(i*5), i*5*Each_EX+Origin_X,Origin_Y-30, paint_text,90);
        }

        drawText(canvas,"Time/h",Origin_X+GAP, (float) (ScreenHeight - 3.5 * GAP),paint_text,90);
        drawText(canvas,"E/°", (float) (ScreenWindth - 1.5 * GAP),Origin_Y,paint_text,90);

        paint_text.setTextSize(50);
        drawText(canvas,"卫星高度角图像",(float) (ScreenWindth - 1.5 * GAP),ScreenHeight/2-150,paint_text,90);

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
