package com.dafang.monitor.nx.utils;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ReflectUtil;
import com.dafang.monitor.nx.statistics.entity.po.Daily;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.DoubleSummaryStatistics;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @description:以反射的方式处理数据
 * @author: echo
 * @createDate: 2020/3/15
 * @version: 1.0
 */
public class ReflectHandleUtils {
    /*
     * 根据对应的操作指令得到相应的处理值
     * @param datas 集合
     * @param element 查询要素字段
     * @param op (min|max|avg|sum)
     * @return double 返回对应的处理值
     * @author echo
     * @date 2020/3/15
     */
    public static <T> double getValByOp(List<T> datas,String element,String op){
        String methodName = "get"+element.substring(0,1).toUpperCase()+element.substring(1);
        DoubleSummaryStatistics summaryStatistics = datas.stream().mapToDouble(x -> {
            Method method = ReflectUtil.getMethod(x.getClass(), methodName);
            Object invoke = null;
            try {
                invoke = method.invoke(x);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return Convert.toDouble(invoke);
        }).summaryStatistics();
        double res = switch(op){
            case "min"-> summaryStatistics.getMin();
            case "max"-> summaryStatistics.getMax();
            case "sum"-> summaryStatistics.getSum();
            default -> summaryStatistics.getAverage();
        };
        return res;
    }
/*
 * 根据单要素过滤异常数据
 * @param datas
 * @param element
 * @return java.util.List<T>
 * @author echo
 * @date 2020/3/18
 */
public static <T> List<T> filterData(List<T> datas,String element){
    String methodName = "get"+element.substring(0,1).toUpperCase()+element.substring(1);
   return datas.stream().filter(x -> {
        Method method = ReflectUtil.getMethod(x.getClass(), methodName);
        Object invoke = null;
        try {
            invoke = method.invoke(x);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Double aDouble = Convert.toDouble(invoke);
        return aDouble <= 999 && aDouble >= -999;
    }).collect(Collectors.toList());
};


    public static void main(String[] args) {
        List<Daily> dailies = filterData(initData(),"TEM_Avg");
        double valByOp = getValByOp(dailies, "TEM_Avg", "min");
        System.out.println(valByOp);
    }

    public static List<Daily> initData(){
        var dailies = new ArrayList<Daily>();
        var build1 = new Daily();
        build1.setPRE_Time_2020(12.2);
        build1.setTEM_Avg(-0.5);
        build1.setTEM_Max(35.0);

        var build2 = new Daily();
        build2.setPRE_Time_2020(12.2);
        build2.setTEM_Avg(-0.3);
        build1.setTEM_Max(34.0);

        var build3 = new Daily();
        build3.setPRE_Time_2020(12.2);
        build3.setTEM_Avg(-9999.0);
        build1.setTEM_Max(38.0);


        dailies.add(build1);
        dailies.add(build2);
        dailies.add(build3);

        return dailies;
    }
}
