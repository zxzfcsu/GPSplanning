package com.zxz.locationtest;

import android.app.Notification;
import android.os.Message;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Array;
import java.util.Arrays;
import java.util.logging.Handler;

/**
 * Created by 亮剑 on 2017/4/22.
 */
public class CurrentDownloadThread extends Thread {
    private URL url;
    private int threadID;
    private int block;
    private File file;

    public CurrentDownloadThread(URL url, int threadID, int block, File file) {
        this.url = url;
        this.threadID = threadID;
        this.block = block;
        this.file = file;
    }

    @Override
    public void run() {
        Log.i("DownloadURL", this.threadID+"号线程"+"下载开始");

        int start = this.threadID * this.block;
        int end = (this.threadID + 1) * this.block;

        Log.i("DownloadURL",this.threadID+"号线程"+" start"+start+" end"+end);

        try {
            HttpURLConnection con = (HttpURLConnection) this.url.openConnection();
            con.setRequestMethod("GET");
            con.setConnectTimeout(5 * 1000);
            con.setRequestProperty("range", "bytes" + start + "-" + end);
            con.connect();

            if (con.getResponseCode() == 200) {
                InputStream is = con.getInputStream();
                RandomAccessFile raf = new RandomAccessFile(this.file, "rwd");
                raf.seek(start);
                byte[] buffer = new byte[1024];
                int len = is.read(buffer, 0, 1024);

                while (len != -1) {
                    raf.write(buffer, 0, len);
                    len = is.read(buffer, 0, 1024);
                }
                raf.close();
                is.close();
                Log.i("DownloadURL",this.threadID+"号线程"+ "下载结束");

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
