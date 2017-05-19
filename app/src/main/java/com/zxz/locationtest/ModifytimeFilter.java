package com.zxz.locationtest;

import java.io.File;

/**
 * Created by 亮剑 on 2017/3/13.
 * 此类用于筛选出时间最优的文件
 * modifytimefiltered用于储存筛选出的结果
 * millsecond用于储存结果文件的最后修改时间
 * setModifytimefiltered方法用以筛选最优文件
 * 其参数strings为上一步筛选后的文件路径全称
 */
public class ModifytimeFilter {
    private File modifytimefiltered;
    private long millsecond;

    public File getModifytimefiltered() {
        return modifytimefiltered;
    }

    public long getMillsecond() {
        return millsecond;
    }

    public void setModifytimefiltered(String[] strings) {
        int length=strings.length;
        long[] millsec=new long[length];
        int maxtime=0;

        for (int i=0;i<length;i++){
            File file=new File(strings[i]);
            millsec[i]=file.lastModified();
        }
        for (int j=0;j<length;j++){
            if (millsec[j]>millsec[maxtime]){
                maxtime=j;
            }
        }
        this.modifytimefiltered=new File(strings[maxtime]);
        this.millsecond=millsec[maxtime];
    }
}
