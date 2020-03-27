package com.dafang.monitor.nx.utils;

import org.apache.commons.lang3.StringUtils;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @description:
 * @author: echo
 * @createDate: 2020/3/12
 * @version: 1.0
 */
public class CommonUtils {
    /**
     * 通过站点编号或区域编号得到相应的区域查询条件
     *
     * @param regions 区域编号通过，相连(如果是站点编号也是通过,相连)
     * @return
     * @author echo
     * @date 2019/04/17
     * @version 1.0
     */
    public static String getCondition(String regions) {
        regions = StringUtils.equals(regions, "0") ? "1,2,3" : regions;//0表示查询全省
        String[] regList = regions.split(",");
        //如果查询是单站或单个区域则用=查询否则用IN
        String condition = "";
        if (regList[0].length() > 3) {// 通过站点查找
            // 对站点编号进行空值处理
            if (StringUtils.isBlank(regions)) {
                regions = "\' \'";
            }
            condition = "b.device_id IN (" + regions + ")";
            if (!StringUtils.contains(regions, ",")) {
                condition = "b.device_id = '" + regions + "'";
            }
        } else {// 通过区域编号查找
            String regionField = "";
            if (regList[0].equals("0") || regList[0].equals("1") || regList[0].equals("2") || regList[0].equals("3")) {
                regionField = "region_id_two";
            } else if (regList[0].equals("4") || regList[0].equals("5")) {
                regionField = "basin_id";
            } else {
                regionField = "region_id";
            }
            condition = "b." + regionField + " IN (" + regions + ")";
            if (!StringUtils.contains(regions, ",")) {
                condition = "b." + regionField + "= " + regions;
            }
        }
        return condition;
    }

    /**
     * 对集合处理，用字符串，连接。如果长度大于三则后面的用。。。显示
     *
     * @param list
     * @return
     * @author echo
     * @date 2019/04/19
     */
    public static String stringHandle(List<String> list) {
        String name = "";
        if (list.size() > 3) {
            list = list.subList(0, 3);
            name = StringUtils.join(list, ",") + "...";
        } else {
            name = StringUtils.join(list, ",");
        }
        return name;
    }

    /**
     * 将时间以月日的区间形式展现
     *
     * @param startDate yyyyMMdd或者yyyy-MM-dd
     * @param endDate
     * @return
     * @author echo
     * @serialData 2019/04/26
     */
    public static String showMD(String startDate, String endDate) {
        LocalDate sDate = LocalDateUtils.stringToDate(startDate);
        LocalDate eDate = LocalDateUtils.stringToDate(endDate);
        String observationDays = sDate.getMonthValue() + "月" + sDate.getDayOfMonth() + "日~" + eDate.getMonthValue() + "月" + eDate.getDayOfMonth() + "日";
        return observationDays;
    }

    /**
     * 得到一个集合中某个时间字段的平均值
     *
     * @param datas
     * @param filed
     * @return 月日
     */
    public static String dateAvgHandle(List<Map<String, Object>> datas, String filed) {
        LocalDate now = LocalDate.now();
        double num = datas.stream().filter(x -> !x.get(filed).toString().equals("--"))
                .mapToDouble(x ->LocalDateUtils.stringToDate(x.get(filed).toString()).getDayOfYear()).average().getAsDouble();
        int days = (int) Math.round(num);
        return now.withDayOfYear(days).toString().replace("-", "").substring(4);
    }

    /**
     * *统计传来数据中该区域的个数，并将值put到map中
     *
     * @param data
     * @param region 需要统计的行政区号
     * @param key
     * @param map
     */
    public static void regionStaTimes(List<Map<String, Object>> data, String region, String key, Map<String, Object> map) {
        long count = data.stream().filter(x -> StringUtils.equals(region, x.get("regionId").toString())).count();
        map.put(key, count);
    }

    /**
     * @param data
     * @param filed
     * @return
     * @Description: TODO(根据某个字段过滤掉异常的数据)
     * @author: echo
     * @date: 2019年12月31日 下午3:44:05
     */
    public static List<Map<String, Object>> filterData(List<Map<String, Object>> data, String filed) {
        return data.stream().filter(x -> !Objects.isNull(x.get(filed))
                && Double.parseDouble(x.get(filed).toString()) > -999
                && Double.parseDouble(x.get(filed).toString()) < 999).collect(Collectors.toList());
    }
}