package com.zxz.locationtest;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Calendar;
import java.util.List;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

public class MainActivity extends AppCompatActivity {

    private LocationManager locationManager;
    private String locationProvider;

    private Folder folder;
    private TextView tvFolder;

    private TextView tvURL;
    private TextView tvName;

    private EditText etLongtitude;
    private EditText etLatitude;

    private String[] eachlinedata=new String[464];
    private SatelliteData satelliteData;

    private TextView textView13;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_locfold);

        folder = new Folder();
        String sdcardPath = Environment.getExternalStorageDirectory().getAbsolutePath();
        String dirname = "/GPSPlanning/";
        File floderpath = new File(sdcardPath + dirname);
        String retruninfo = folder.setFolder(floderpath);
        Toast.makeText(this, retruninfo, Toast.LENGTH_LONG).show();
        folder.setDirfilename();

        satelliteData=new SatelliteData();
        setControl();

        SuffixFilter FirstStep = new SuffixFilter();
        FirstStep.setSuffixfiltered(folder.getFolder());

        SizeFilter SecondStep = new SizeFilter();
        SecondStep.setSizefiltered(folder.getFolder(), FirstStep.getSuffixfiltered());

        if (SecondStep.getSizefiltered().length==0){
            tvName.setText("无符合要求的文件，请下载");
        }
        else {
        ModifytimeFilter ThirdStep=new ModifytimeFilter();
        ThirdStep.setModifytimefiltered(SecondStep.getSizefiltered());

        String s= String.valueOf(ThirdStep.getModifytimefiltered());
        if (s!=null){
            String result=s.replaceAll(String.valueOf(floderpath)+"/","");
            tvName.setText(result);
        }
        }

    }

    public void setControl(){
        tvFolder = (TextView) findViewById(R.id.tvFolder);
        tvFolder.setText(folder.getFolder().getAbsolutePath());

        tvURL=(TextView)findViewById(R.id.tvURL);
        tvURL.setText("https://www.navcen.uscg.gov/?pageName=currentAlmanac&format=yuma-txt");
        tvName=(TextView)findViewById(R.id.tvName);

        etLongtitude= (EditText) findViewById(R.id.etLongtitude);
        etLatitude= (EditText) findViewById(R.id.etLatitude);

        textView13= (TextView) findViewById(R.id.textView13);

        progressBar= (ProgressBar) findViewById(R.id.progressBar);
    }

    public void AutoPosition(View view) {
        //获取地理位置管理器
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        //获取所有可用的位置提供器
        List<String> providers = locationManager.getProviders(true);
        if (providers.contains(LocationManager.GPS_PROVIDER)) {
            //如果是GPS
            locationProvider = LocationManager.GPS_PROVIDER;
        } else if (providers.contains(LocationManager.NETWORK_PROVIDER)) {
            //如果是Network
            locationProvider = LocationManager.NETWORK_PROVIDER;
        } else {
            Toast.makeText(this, "没有可用的位置提供器", Toast.LENGTH_SHORT).show();
            return;
        }

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding这里要求丢失的权限，然后覆盖
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation处理用户授予权限的情况。 请参阅文档
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Location location = locationManager.getLastKnownLocation(locationProvider);
        if (location != null) {
            //不为空,显示地理位置经纬度
            showLocation(location);
        }
        //监视地理位置变化
        locationManager.requestLocationUpdates(locationProvider, 3000, 1, locationListener);
    }

    public void download(View view){
        String s= (String) tvFolder.getText();

        Calendar timenow = Calendar.getInstance();
        int year = timenow.get(Calendar.YEAR);
        int month = timenow.get(Calendar.MONTH) + 1;
        int day = timenow.get(Calendar.DAY_OF_MONTH);
        String filename = "YUMA Almanac " + year + "" + month + "" + day + ".txt";
        tvName.setText(filename);
        textView13.setText("正在下载，请稍候");

        DownloadAsync downloadAsync=new DownloadAsync();
        downloadAsync.setFiledir(s);
        downloadAsync.execute();
    }

    public void newfile() throws IOException {
        String filedir = (String) tvFolder.getText();
        String filename = (String) tvName.getText();
        String dir = filedir + File.separator+filename;
        File file = new File(dir);
        int week=TimeCalculate();


        if (file.exists()) {

            BufferedReader bufferedReader = new BufferedReader(
                    new InputStreamReader(new FileInputStream(dir)));
            String data = "start";

            String s="\t<pre>******** Week "+week+" almanac for PRN-01 ********";
            while (!data.equals(s)){
                data = bufferedReader.readLine();
            }

            Log.i("readfile", "******** Week "+week+" almanac for PRN-01 ********");
            String s1="******** Week "+week+" almanac for PRN-01 ********";
            eachlinedata[0]=s1;

            for (int j = 0; j < 463; j++) {
                data = bufferedReader.readLine();
                Log.i("readfile", data);
                eachlinedata[j+1]=data;
            }
        }
        else {
            Toast.makeText(this,"file doesn't exist",Toast.LENGTH_LONG);
        }

    }

    public void Finish(View view) throws IOException {
        newfile();

        satelliteData.setparameters(eachlinedata);
        Log.i("readfile","Finsh");

        String Longtitude= String.valueOf(etLongtitude.getText());
        String Latithde= String.valueOf(etLatitude.getText());
        if (Longtitude.equals("")||Latithde.equals("")){
            Toast.makeText(this, "请输入坐标或定位", Toast.LENGTH_LONG).show();
        }
        else {
            Intent intent=new Intent(this,SecondActivity.class);
            intent.putExtra("Longtitude",Longtitude);
            intent.putExtra("Latitude",Latithde);
            intent.putExtra("data",satelliteData);
            startActivity(intent);
        }

//        File file=new File(tvFolder.getText()+File.separator+tvName.getText());
//        boolean b=file.exists();
//        FileInputStream fis = null;
//        fis = new FileInputStream(file);
//        long size = fis.available();
//        Log.i("MainActivity", String.valueOf(size));
    }

    private void showLocation(Location location) {
        etLongtitude.setText(String.valueOf(location.getLongitude()));
        etLatitude.setText(String.valueOf(location.getLatitude()));
    }

    /**
     * LocationListern监听器
     * 参数：地理位置提供器、监听位置变化的时间间隔、位置变化的距离间隔、LocationListener监听器
     */

    LocationListener locationListener = new LocationListener() {

        @Override
        public void onStatusChanged(String provider, int status, Bundle arg2) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }

        @Override
        public void onLocationChanged(Location location) {
            //如果位置发生变化,重新显示
            showLocation(location);

        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (locationManager != null) {
            //移除监听器

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            locationManager.removeUpdates(locationListener);
        }
    }

    public int TimeCalculate() {

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

        Calendar timenow = Calendar.getInstance();
        int year=timenow.get(Calendar.YEAR);
        int month=timenow.get(Calendar.MONTH)+1;
        int day=timenow.get(Calendar.DAY_OF_MONTH);

        int dayofw, dayofy, yr, ttlday, m, weekno;
        long GPStime;
        if (year < 1981 || month < 1 || month > 12 || day < 1 || day > 31)
            weekno = 0;
        if (month == 1)
            dayofy = day;
        else {
            dayofy = 0;
            for (m = 1; m <= (month - 1); m++) {
                dayofy += dinmth[m];
                if (m == 2) {
                    if (year % 4 == 0 && year % 100 != 0 || year % 400 == 0) {
                        dayofy += 1;
                    }
                }
            }
            dayofy += day;
        }

        ttlday = 360;
        for (yr = 1981; yr <= (year - 1); yr++) {
            ttlday += 365;
            if (yr % 4 == 0 && yr % 100 != 0 || yr % 400 == 0) {
                ttlday += 1;
            }
        }
        ttlday += dayofy;
        weekno = ttlday / 7;
//        dayofw = ttlday - 7 * weekno;
        //TODO: 2017/4/3  -this.UTC*3600加还是减
//        GPStime = (long) (this.intervaltime * 3600 - this.UTC * 3600 + dayofw * 86400);
//        this.gpstimeofweek = GPStime;

        while (weekno > 1024) {
            weekno -= 1024;
        }
        return weekno;
    }

    public void folderhelp(View view){
        Toast.makeText(this, "请授予存储权限并下载文件", Toast.LENGTH_SHORT).show();
    }

    public void LocationHelp(View view){
        Toast.makeText(this, "请授予定位权限", Toast.LENGTH_SHORT).show();
    }

    public class DownloadAsync extends AsyncTask<Void,Integer,Void>{

        private String SURL = "https://www.navcen.uscg.gov/?pageName=currentAlmanac&format=yuma-txt";
        private String filedir;


        public String getSURL() {
            return SURL;
        }

        public void setSURL(String SURL) {
            this.SURL = SURL;
        }

        public String getFiledir() {
            return filedir;
        }

        public void setFiledir(String filedir) {
            this.filedir = filedir;
        }

        @Override
        protected Void doInBackground(Void... params) {
            Calendar timenow = Calendar.getInstance();
            int year = timenow.get(Calendar.YEAR);
            int month = timenow.get(Calendar.MONTH) + 1;
            int day = timenow.get(Calendar.DAY_OF_MONTH);
            String filename = "YUMA Almanac " + year + "" + month + "" + day + ".txt";
            String dir = this.filedir + File.separator + filename;

            File file = new File(dir);
            if (file.exists() == true) {
                Log.i("DownloadURL", "The file exsist");
            } else {
                try {
                    file.createNewFile();
                    URL url = new URL(this.SURL);
                    HttpURLConnection con=(HttpURLConnection)url.openConnection();
                    con.setConnectTimeout(5000);
                    int length=con.getContentLength();

                    InputStream inputStream=con.getInputStream();


                    OutputStream outputStream=new FileOutputStream(file);
                    byte[] buffer=new byte[4*1024];
                    int temp;
                    while ((temp=inputStream.read(buffer))!=-1){
                        outputStream.write(buffer,0,temp);

                        FileInputStream fis=new FileInputStream(file);
                        int size=fis.available();
                        int size_scale=size*100/length;
                        double test=size*100/length;
                        Log.i("MainActivity", String.valueOf(test));
                        publishProgress(size_scale);

                    }
                    outputStream.flush();
                    outputStream.close();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            int size=values[0];
            progressBar.setProgress(size);
            textView13.setText("正在下载，请稍候");
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            progressBar.setProgress(100);
            textView13.setText("下载完成");
            super.onPostExecute(aVoid);
        }
    }
}
