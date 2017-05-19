package com.zxz.locationtest;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Created by 亮剑 on 2017/3/12.
 * 该类用以文件大小的方式筛选出所需的文件
 * sizefiltered储存筛选后的文件路径及文件名
 * size用以控制文件的大小
 * setSizefiltered方法为筛选方法
 * 其中参数foldername是文件夹路径，sizefiltered是待筛选的文件名
 */
public class SizeFilter {
    private String[] sizefiltered;
    private int size = 18410;

    public String[] getSizefiltered() {
        return sizefiltered;
    }

    public void setSizefiltered(File foldername, String[] sizefiltered) {
        int length = sizefiltered.length;
        int j = 0;
        String[] M1sizefiltered = new String[length];

        for (int i = 0; i < length; i++) {
            File sizefilterfile = new File(foldername + File.separator+ sizefiltered[i]);
            try {
                FileInputStream fileInputStream=new FileInputStream(sizefilterfile);
                int fis_size=fileInputStream.available();
                if (fis_size>= this.size) {
                    M1sizefiltered[j] = String.valueOf(sizefilterfile);
                    j++;
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        String[] M2sizefiltered = new String[j];
        for (int k = 0; k < j; k++) {
            M2sizefiltered[k] = M1sizefiltered[k];
        }
        this.sizefiltered = M2sizefiltered;

    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }
}
