package com.zxz.locationtest;

import java.io.Serializable;

/**
 * Created by 亮剑 on 2017/5/12.
 */
public class EiDraw implements Serializable{
    private double[][] E;
    private int[] Sid;

    private int[] Searchnum;

    private int width,height;

    private int GAP=50;

    private int starttime;

    private double[][] xi;
    private double[][] yi;
    private double[][] zi;

    public double[][] getXi() {
        return xi;
    }

    public void setXi(double[][] xi) {
        this.xi = xi;
    }

    public double[][] getYi() {
        return yi;
    }

    public void setYi(double[][] yi) {
        this.yi = yi;
    }

    public double[][] getZi() {
        return zi;
    }

    public void setZi(double[][] zi) {
        this.zi = zi;
    }

    public int getStarttime() {
        return starttime;
    }

    public void setStarttime(int starttime) {
        this.starttime = starttime;
    }

    public void setSid(int[] Sid) {
        this.Sid = Sid;
    }

    public void read2class(int i, double[] Ei) {
        this.E[i] = Ei;
    }

    public void read(double[][] E) {
        this.E = E;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public double[][] getE() {
        return E;
    }

    public int[] getSid() {
        return Sid;
    }

    public int getGAP() {
        return GAP;
    }

    public int[] getSearchnum() {
        return Searchnum;
    }

    public int getSearchnum(int num){
        return Searchnum[num];
    }

    public void SearchEnum() {
        int[] temporary_matrics1=new int[31];
        int row = 0;
        for (int i = 0; i < 31; i++) {
            for (int j = 0; j < this.E[i].length; j++) {
                if (this.E[i][j] > 0) {
                    temporary_matrics1[row] = i;
                    row++;
                    break;
                }
            }
        }

        for (int i=1;i<31;i++){
            if(temporary_matrics1[i]==0){
                row=i;
                break;
            }
        }

        int[] temporary_matrics2=new int[row];
        for (int i=0;i<row;i++){
            temporary_matrics2[i]=temporary_matrics1[i];
        }
        this.Searchnum=temporary_matrics2;
    }

}
