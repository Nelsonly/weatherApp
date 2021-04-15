package com.nelson.weather.holder;

public class DailyDataHolder {
    private static DailyDataHolder uniqueInstance = null;
    private DailyDataHolder(){

    }
    public static synchronized DailyDataHolder getInstance(){

        //判断存储实例的变量是否有值
        if(uniqueInstance == null){
            //如果没有，就创建一个类实例，并把值赋值给存储类实例的变量
            uniqueInstance = new DailyDataHolder();
        }

        //如果有值，那就直接使用
        return uniqueInstance;
    }

}
