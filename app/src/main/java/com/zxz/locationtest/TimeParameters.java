package com.zxz.locationtest;

import java.io.Serializable;

/**
 * Created by 亮剑 on 2017/4/2.
 * 本对象用以描述观测时刻
 * <p/>
 * intervaltime用于描述可见间隔时间，如0:00,1:00--23:00
 * duration用于描述时间间隔，如6h，12h，24h
 * UTC用于描述观测位置所在的时区，如+8:00 Beijing
 * year,month,day用于描述观测时间的年月日
 * gpstimeofweek和weeknum是TimeCalculate方法计算出来的结果
 * weeknum指的是GPS周数，gpstimeofweek指的是时间参数t
 * 若weeknum==0，说明时间（year,month,day）选定有误
 * Discretization参数用于描述时间的离散程度，用在TimeDiscretization方法中
 * discretizedtime参数为TimeDiscretization方法的结果，其中时间均以秒为单位
 * <p/>
 * setYMD方法用于设定年月日参数
 * judgment方法用于判断类中各项时间参数是否为空，返回值TRUE为不为空，FALSE为空
 * TimeCalculate方法用于计算时间参数t(观测时刻的在此周中的GPS时间，以秒为单位)
 * 用于Calculation类中地心坐标的计算
 * weekno为自6 Jan 1980以来的GPS周数，也即yuma文件中的周数，应该进行判断两者是否相同
 * 但是需注意GPS周数每满1024即减去1024，所以应考虑上这一条
 * TimeDiscretization方法用于将时间t离散化，因为t应是一个持续的时间段
 */
public class TimeParameters implements Serializable{
    private int intervaltime;
    private int duration;
    private int UTC;
    private int year, month, day;
    private long gpstimeofweek;
    private int weeknum;
    private int discretization=30;
    private long[] discretizedtime;

    public int getIntervaltime() {
        return intervaltime;
    }

    public void setIntervaltime(int intervaltime) {
        this.intervaltime = intervaltime;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getUTC() {
        return UTC;
    }

    public void setUTC(int UTC) {
        this.UTC = UTC;
    }

    public int getYear() {
        return year;
    }

    public int getMonth() {
        return month;
    }

    public int getDay() {
        return day;
    }

    public long getGpstimeofweek() {
        return gpstimeofweek;
    }

    public int getWeeknum() {
        return weeknum;
    }

    public int getDiscretization() {
        return discretization;
    }

    public long getDiscretizedtime(int num) {
        return discretizedtime[num];
    }

    public long[] getDiscretizedtime(){return discretizedtime;}

    public void setDiscretization(int discretization) {
        this.discretization = discretization;
    }

    public void setYMD(int year, int month, int day) {
        this.year = year;
        this.month = month;
        this.day = day;
    }

//    public boolean judgment(int week) {
//        if (this.weeknum == week) {
//            if (this.duration == 0 || this.discretization == 0 || this.weeknum == 0) {
//                return false;
//            } else
//                return true;
//        } else
//            return false;
//        // TODO: 2017/4/3 弹出提示，选择的时间不在该GPS周内
//    }

    public void TimeCalculate() {

        //dinmth数组用来储存每个月有多少天
        int[] dinmth = new int[13];
        for (int k = 0; k <= 12; k++) {
            switch (k) {
                case 1:
                case 3:
                case 5:
                case 7:
                case 8:
                case 10:
                case 12:
                    dinmth[k] = 31;
                    break;
                case 2:
                    dinmth[k] = 28;
                    break;
                default:
                    dinmth[k] = 30;
                    break;
            }
        }

        int dayofw, dayofy, yr, ttlday, m, weekno;
        long GPStime;
        if (this.year < 1981 || this.month < 1 || this.month > 12 || this.day < 1 || this.day > 31)
            weekno = 0;
        if (this.month == 1)
            dayofy = this.day;
        else {
            dayofy = 0;
            for (m = 1; m <= (this.month - 1); m++) {
                dayofy += dinmth[m];
                if (m == 2) {
                    if (this.year % 4 == 0 && this.year % 100 != 0 || this.year % 400 == 0) {
                        dayofy += 1;
                    }
                }
            }
            dayofy += this.day;
        }

        ttlday = 360;
        for (yr = 1981; yr <= (this.year - 1); yr++) {
            ttlday += 365;
            if (yr % 4 == 0 && yr % 100 != 0 || yr % 400 == 0) {
                ttlday += 1;
            }
        }
        ttlday += dayofy;
        weekno = ttlday / 7;
        dayofw = ttlday - 7 * weekno;
        //TODO: 2017/4/3  -this.UTC*3600加还是减
        GPStime = (long) (this.intervaltime * 3600 - this.UTC * 3600 + dayofw * 86400);
        this.gpstimeofweek = GPStime;

        while (weekno > 1024) {
            weekno -= 1024;
        }
        this.weeknum = weekno;

    }

    public void TimeDiscretization() {

        int partnum = this.duration * 60 / this.discretization;
        int partdistance = this.discretization * 60;
        long[] dt = new long[partnum + 1];

        for (int i = 0; i <= partnum; i++) {
            dt[i] = this.gpstimeofweek + i * partdistance-this.UTC*3600;
        }
        this.discretizedtime = dt;

    }
}
