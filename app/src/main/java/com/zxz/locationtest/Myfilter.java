package com.zxz.locationtest;

import java.io.File;
import java.io.FilenameFilter;

/**
 * Created by 亮剑 on 2017/3/8.
 * 该类为接口FilenameFilter的具体实现
 * 在list（）方法中用以过滤后缀名
 */
public class Myfilter implements FilenameFilter {

    private String type;

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public boolean accept(File dir, String name) {
        return name.endsWith(type);
    }
}
