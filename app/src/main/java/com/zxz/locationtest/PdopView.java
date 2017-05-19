package com.zxz.locationtest;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.CornerPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.Log;
import android.view.View;

/**
 * Created by 亮剑 on 2017/5/18.
 */
public class PdopView extends View {
    private EiDraw eiDraw;

    public EiDraw getEiDraw() {
        return eiDraw;
    }

    public void setEiDraw(EiDraw eiDraw) {
        this.eiDraw = eiDraw;
    }

    public PdopView(Context context) {
        super(context);
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

        int Axis_PDOPX_length = ScreenWindth - 2 * GAP;
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

        //绘制PDOP图像
        double[][] Edata=this.eiDraw.getE();
        double[][] GDOP_xi=this.eiDraw.getXi();
        double[][] GDOP_yi=this.eiDraw.getYi();
        double[][] GDOP_zi=this.eiDraw.getZi();
        int timenum=Edata[0].length;
        double[] Pdop=new double[timenum];
        int length=Axis_tY_length/timenum;

        //对于每一段时间而言
        for (int i=0;i<timenum;i++){
            //寻找该时间内有多少颗卫星可被观测到
            int num1=0;
            for (int j=0;j<31;j++){
                if (Edata[j][i]>0){
                    num1++;
                }
            }
            int[] satnum = new int[num1];
            int num2=0;
            for (int j=0;j<31;j++){
                if (Edata[j][i]>0){
                    satnum[num2]=j;
                    num2++;
                }
            }

            double Pdop_sqrt=0;
            for (int j=0;j<num1;j++){
                int num= satnum[j];
                double r=Math.sqrt(GDOP_xi[num][i]*GDOP_xi[num][i]+GDOP_yi[num][i]*GDOP_yi[num][i]+GDOP_zi[num][i]*GDOP_zi[num][i]);
                double alpha=GDOP_xi[num][i]/r;
                double belta=GDOP_yi[num][i]/r;
                double gamma=GDOP_zi[num][i]/r;
                Pdop_sqrt=Pdop_sqrt+alpha*alpha+belta*belta+gamma*gamma;
            }
            Log.i("PdopView",i+"+" +String.valueOf(Pdop_sqrt));
            Pdop[i]=Math.sqrt(Pdop_sqrt);
        }

        double max=Pdop[0];
        for (int i=0;i<timenum;i++){
            if (Pdop[i]>max){
                max=Pdop[i];
            }
        }
        int maxPdop= (int) (max+1);
        int height=Axis_PDOPX_length/maxPdop;

        Paint paint_line=new Paint();
        paint_line.setAntiAlias(true);
        paint_line.setColor(Color.BLUE);
        paint_line.setStyle(Paint.Style.STROKE);
        paint_line.setStrokeWidth(3);
        paint_line.setPathEffect(new CornerPathEffect(50));

        Path path_line=new Path();
        path_line.moveTo((float) (Origin_X+Pdop[0]*height),Origin_Y);
        for (int i=1;i<timenum;i++){
            path_line.lineTo((float) (Origin_X+Pdop[i]*height),Origin_Y+i*length);
        }
        canvas.drawPath(path_line,paint_line);

        //绘制坐标轴部件
        Paint paint_text = new Paint();
        paint_text.setColor(Color.BLACK);
        paint_text.setTextSize(25);
        paint_text.setStrokeWidth(3);
        int starttime=this.eiDraw.getStarttime();
        for (int i=1;i<maxPdop;i++){
            canvas.drawLine(Origin_X+i*height,Origin_Y,Origin_X+i*height,Origin_Y+8,paint_Axis);
            drawText(canvas, String.valueOf(i),Origin_X+i*height-5,Origin_Y-20,paint_text,90);
        }
        for (int i=1;i<timenum;i++){
            canvas.drawLine(Origin_X,Origin_Y+i*length,Origin_X+8,Origin_Y+i*length,paint_Axis);
        }
        for (int i=0;i<timenum;i+=2){
            drawText(canvas, String.valueOf(starttime+i/2)+":00", Origin_X-25,Origin_Y+i*length-10, paint_text,90);
        }
        drawText(canvas,"Time/h",Origin_X+GAP, (float) (ScreenHeight - 3.5 * GAP),paint_text,90);
        drawText(canvas,"GDOP", (float) (ScreenWindth - 1.5 * GAP),Origin_Y,paint_text,90);

        paint_text.setTextSize(50);
        drawText(canvas,"PDOP值图像",(float) (ScreenWindth - 1.5 * GAP),ScreenHeight/2-150,paint_text,90);
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
