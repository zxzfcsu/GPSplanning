package com.zxz.locationtest;

import java.io.Serializable;

/**
 * Created by 亮剑 on 2017/3/13.
 * 该类用于存储YUMA文件中的主要内容，以数组的形式
 * 其中没有很多个set方法，只使用了一个setparameters方法来分类储存数据
 * 注意，由于使用的是YUMA数据，所以直接给数组赋了大小[31]
 */
public class SatelliteData implements Serializable {

    private int SWeek;
    private int[] SID;
    private int[] SHealth;
    private double[] SEccentricity;
    private double[] STimeOfApplicability;
    private double[] SOrbitalInclination;
    private double[] SRiteOfRightAscen;
    private double[] SSqrt;
    private double[] SRightAscenAtWeek;
    private double[] SArgumentOfPerigee;
    private double[] SMeanAnom;
    private double[] SAF0;
    private double[] SAF1;

    public int getSWeek() {
        return SWeek;
    }

    public int getSID(int SatelliteID) {
        return SID[SatelliteID];
    }

    public int[] getSID(){
        return SID;
    }

    public int getSHealth(int SatelliteID) {
        return SHealth[SatelliteID];
    }

    public double getSEccentricity(int SatelliteID) {
        return SEccentricity[SatelliteID];
    }

    public double getSTimeOfApplicability(int SatelliteID) {
        return STimeOfApplicability[SatelliteID];
    }

    public double getSOrbitalInclination(int SatelliteID) {
        return SOrbitalInclination[SatelliteID];
    }

    public double getSRiteOfRightAscen(int SatelliteID) {
        return SRiteOfRightAscen[SatelliteID];
    }

    public double getSSqrt(int SatelliteID) {
        return SSqrt[SatelliteID];
    }

    public double getSRightAscenAtWeek(int SatelliteID) {
        return SRightAscenAtWeek[SatelliteID];
    }

    public double getSArgumentOfPerigee(int SatelliteID) {
        return SArgumentOfPerigee[SatelliteID];
    }

    public double getSMeanAnom(int SatelliteID) {
        return SMeanAnom[SatelliteID];
    }

    public double getSAF0(int SatelliteID) {
        return SAF0[SatelliteID];
    }

    public double getSAF1(int SatelliteID) {
        return SAF1[SatelliteID];
    }

    public void setparameters(String[] strings){

        int[] MSID = new int[31];
        int[] MSHealth = new int[31];
        double[] MSEccentricity=new double[31];
        double[] MSTimeofApplicability=new double[31];
        double[] MSOrbitalInclination=new double[31];
        double[] MSRateofRightAscen=new double[31];
        double[] MSSqrt=new double[31];
        double[] MSRightAscenatWeek=new double[31];
        double[] MSArgumentofPerigee=new double[31];
        double[] MSMeanAnom=new double[31];
        double[] MSAf0=new double[31];
        double[] MSAf1=new double[31];

        String Middletransform=new String();

        String M1SWeek=strings[13].substring(29);
        int M2SWeek=Integer.valueOf(M1SWeek);
        this.SWeek=M2SWeek;

        for (int i=0;i<31;i++){
            Middletransform=strings[15*i+1].substring(28);
            MSID[i]=Integer.valueOf(Middletransform);
        }
        this.SID=MSID;
        for (int i=0;i<31;i++){
            Middletransform=strings[15*i+2].substring(28);
            MSHealth[i]=Integer.valueOf(Middletransform);
        }
        this.SHealth=MSHealth;
        for (int i=0;i<31;i++){
            Middletransform=strings[15*i+3].substring(25);
            MSEccentricity[i]=Double.valueOf(Middletransform);
        }
        this.SEccentricity=MSEccentricity;
        for (int i=0;i<31;i++){
            Middletransform=strings[15*i+4].substring(25);
            MSTimeofApplicability[i]=Double.valueOf(Middletransform);
        }
        this.STimeOfApplicability=MSTimeofApplicability;
        for (int i=0;i<31;i++){
            Middletransform=strings[15*i+5].substring(25);
            MSOrbitalInclination[i]=Double.valueOf(Middletransform);
        }
        this.SOrbitalInclination=MSOrbitalInclination;
        for (int i=0;i<31;i++){
            Middletransform=strings[15*i+6].substring(25);
            MSRateofRightAscen[i]=Double.valueOf(Middletransform);
        }
        this.SRiteOfRightAscen=MSRateofRightAscen;
        for (int i=0;i<31;i++){
            Middletransform=strings[15*i+7].substring(25);
            MSSqrt[i]=Double.valueOf(Middletransform);
        }
        this.SSqrt=MSSqrt;
        for (int i=0;i<31;i++){
            Middletransform=strings[15*i+8].substring(25);
            MSRightAscenatWeek[i]=Double.valueOf(Middletransform);
        }
        this.SRightAscenAtWeek=MSRightAscenatWeek;
        for (int i=0;i<31;i++){
            Middletransform=strings[15*i+9].substring(25);
            MSArgumentofPerigee[i]=Double.valueOf(Middletransform);
        }
        this.SArgumentOfPerigee=MSArgumentofPerigee;
        for (int i=0;i<31;i++){
            Middletransform=strings[15*i+10].substring(25);
            MSMeanAnom[i]=Double.valueOf(Middletransform);
        }
        this.SMeanAnom=MSMeanAnom;
        for (int i=0;i<31;i++){
            Middletransform=strings[15*i+11].substring(25);
            MSAf0[i]=Double.valueOf(Middletransform);
        }
        this.SAF0=MSAf0;
        for (int i=0;i<31;i++){
            Middletransform=strings[15*i+12].substring(25);
            MSAf1[i]=Double.valueOf(Middletransform);
        }
        this.SAF1=MSAf1;

    }
}
