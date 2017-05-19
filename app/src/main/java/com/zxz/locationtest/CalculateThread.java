package com.zxz.locationtest;

import android.util.Log;

/**
 * Created by 亮剑 on 2017/5/11.
 */
public class CalculateThread extends Thread {
    private SatelliteData satelliteData;
    private TimeParameters timeParameters;
    private double Longtitude, Latitude;

    public EiDraw eiDraw;

    private double X0, Y0, Z0;

    private double GM = 3.9860047E14;
    private double omega_e = 7.292115e-5;

    private double[][] GDOP_xi;
    private double[][] GDOP_yi;
    private double[][] GDOP_zi;

    public SatelliteData getSatelliteData() {
        return satelliteData;
    }

    public void setSatelliteData(SatelliteData satelliteData) {
        this.satelliteData = satelliteData;
    }

    public double getLongtitude() {
        return Longtitude;
    }

    public void setLongtitude(double longtitude) {
        Longtitude = longtitude;
    }

    public double getLatitude() {
        return Latitude;
    }

    public void setLatitude(double latitude) {
        Latitude = latitude;
    }

    public TimeParameters getTimeParameters() {
        return timeParameters;
    }

    public void setTimeParameters(TimeParameters timeParameters) {
        this.timeParameters = timeParameters;
    }

    public EiDraw getEiDraw() {
        return eiDraw;
    }

    public void setEiDraw(EiDraw eiDraw) {
        this.eiDraw = eiDraw;
    }

    @Override
    public void run() {
        //// TODO: 2017/5/11

        double B0 = this.Longtitude * Math.PI / 180;
        double L0 = this.Latitude * Math.PI / 180;
        int a = 6378137;
        double b = 6356752;
        double e1 = b / a;
        double e = e1 * e1;
        double e2 = 1 - e;
        double N = a / Math.sqrt(1 - e2 * Math.sin(B0) * Math.sin(B0));
        this.X0 = N * Math.cos(B0) * Math.cos(L0);
        this.Y0 = N * Math.cos(B0) * Math.sin(L0);
        this.Z0 = (1 - e2) * N * Math.sin(B0);

        int t_num = this.timeParameters.getDiscretizedtime().length;

        double[][] testE = new double[31][];
        GDOP_xi=new double[31][t_num];
        GDOP_yi=new double[31][t_num];
        GDOP_zi=new double[31][t_num];
        //31颗卫星的循环
        for (int i = 0; i < 31; i++) {
            double[] E = new double[t_num];

            int ID = this.satelliteData.getSID(i);
            int Health = this.satelliteData.getSHealth(i);
            int Week = this.satelliteData.getSWeek();
            double Eccentricity = this.satelliteData.getSEccentricity(i);
            double TimeOfApplicability = this.satelliteData.getSTimeOfApplicability(i);
            double OrbitalInclination = this.satelliteData.getSOrbitalInclination(i);
            double RiteOfRightAscen = this.satelliteData.getSRiteOfRightAscen(i);
            double Sqrt = this.satelliteData.getSSqrt(i);
            double RightAscenAtWeek = this.satelliteData.getSRightAscenAtWeek(i);
            double ArgumentOfPerigee = this.satelliteData.getSArgumentOfPerigee(i);
            double MeanAnom = this.satelliteData.getSMeanAnom(i);
            double AF0 = this.satelliteData.getSAF0(i);
            double AF1 = this.satelliteData.getSAF1(i);

//            每一个时间的循环
            for (int j = 0; j < t_num; j++) {

                double n = Math.sqrt(this.GM) / (Math.pow(Sqrt, 3));
                int tk = (int) (this.timeParameters.getDiscretizedtime(j) - TimeOfApplicability);
                double Mk = MeanAnom + n * tk;

                double Ek = Mk;
                double delta_ek = 1;
                while (delta_ek > 1e-10) {
                    double ek = Ek;
                    Ek = Mk + Eccentricity * Ek;
                    delta_ek = Math.abs(Ek - ek);
                }

                double tanfk = (Math.sin(Ek) * Math.sqrt(1 - Eccentricity * Eccentricity)) / (Math.cos(Ek) - Eccentricity);
                double fk = Math.atan(tanfk);
                double uk = fk + ArgumentOfPerigee;
                double rk = Sqrt * Sqrt * (1 - Eccentricity * Math.cos(Ek));
                double Lk = RightAscenAtWeek + (RiteOfRightAscen - omega_e) * tk - RiteOfRightAscen * TimeOfApplicability;

                double xk = rk * Math.cos(uk);
                double yk = rk * Math.sin(uk);

                double Xk = xk * Math.cos(Lk) - yk * Math.sin(Lk) * Math.cos(OrbitalInclination);
                double Yk = xk * Math.sin(Lk) + yk * Math.cos(Lk) * Math.cos(OrbitalInclination);
                double Zk = yk * Math.sin(OrbitalInclination);

                double delta_X = Xk - this.X0;
                double delta_Y = Yk - this.Y0;
                double delta_Z = Zk - this.Z0;

                double xi = -delta_X * Math.sin(B0) * Math.cos(L0) - delta_Y * Math.sin(B0) * Math.sin(L0) + delta_Z * Math.cos(B0);
                double yi = -delta_X * Math.sin(L0) + delta_Y * Math.cos(L0);
                double zi = delta_X * Math.cos(B0) * Math.cos(L0) + delta_Y * Math.cos(B0) * Math.sin(L0) + delta_Z * Math.sin(B0);

                double Ei = Math.atan(zi / Math.sqrt(xi * xi + yi * yi)) * 180 / Math.PI;
                double Ai = Math.atan(yi / xi);

                E[j] = Ei;
                Log.i("CalculateThread", String.valueOf(Ei));
                GDOP_xi[i][j]=xi;
                GDOP_yi[i][j]=yi;
                GDOP_zi[i][j]=zi;
            }
            testE[i] = E;
        }

        int[] id=this.satelliteData.getSID();
        this.eiDraw.setSid(id);
        this.eiDraw.read(testE);
        this.eiDraw.SearchEnum();
        this.eiDraw.setXi(GDOP_xi);
        this.eiDraw.setYi(GDOP_yi);
        this.eiDraw.setZi(GDOP_zi);
    }


}
