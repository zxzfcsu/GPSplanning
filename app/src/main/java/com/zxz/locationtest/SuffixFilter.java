package com.zxz.locationtest;


import java.io.File;

/**
 * Created by 亮剑 on 2017/3/12.
 * 该类用以后缀名筛选文件，为第一步
 * suffixfiltered储存筛选后的文件名，注意不是路径
 * suffixname储存筛选的方法，也即后缀名，默认为txt
 * setSuffixfiltered方法是筛选方法，参数file为文件夹名
 */
public class SuffixFilter {
    private String[] suffixfiltered;
    private String suffixname = ".txt";

    public String getSuffixname() {
        return suffixname;
    }

    public void setSuffixname(String suffixname) {
        this.suffixname = suffixname;
    }

    public String[] getSuffixfiltered() {
        return suffixfiltered;
    }

    public void setSuffixfiltered(File file) {
        Myfilter filter = new Myfilter();
        filter.setType(this.suffixname);
        this.suffixfiltered = file.list(filter);
    }
}
