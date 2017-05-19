package com.zxz.locationtest;

import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Calendar;

/**
 * Created by 亮剑 on 2017/4/22.
 */
public class downloadURLThread extends Thread {
    private String SURL="https://www.navcen.uscg.gov/?pageName=currentAlmanac&format=yuma-txt";
    private String filedir;

    public void setFiledir(String filedir) {
        this.filedir = filedir;
    }

    public String getSURL() {
        return SURL;
    }

    public void setSURL(String SURL) {
        this.SURL = SURL;
    }

    @Override
    public void run() {
        Calendar timenow = Calendar.getInstance();
        int year=timenow.get(Calendar.YEAR);
        int month=timenow.get(Calendar.MONTH)+1;
        int day=timenow.get(Calendar.DAY_OF_MONTH);
        String filename="YUMA Almanac "+year+""+month+""+day+".txt";
        String dir=this.filedir+File.separator+filename;

        File file=new File(dir);
        if (file.exists()==true){
            Log.i("DownloadURL","The file exsist");
        }
        else {
            try {
                URL url=new URL(this.SURL);
                HttpURLConnection con=(HttpURLConnection)url.openConnection();
                con.setRequestMethod("GET");
                con.setDoInput(true);
                con.setConnectTimeout(5*1000);
                con.connect();
                RandomAccessFile raf=new RandomAccessFile(file,"rwd");
                int length=con.getContentLength();
                raf.setLength(length);
                raf.close();
                int block=length/3;
                if (length%3>0){
                    block++;
                }
                Log.i("DownloadURL","参数设置完毕");
                for (int i=0;i<3;i++){
                    new CurrentDownloadThread(url,i,block,file).start();
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
