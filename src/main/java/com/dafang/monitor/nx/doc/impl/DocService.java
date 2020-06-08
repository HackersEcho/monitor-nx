package com.dafang.monitor.nx.doc.impl;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.NumberUtil;
import com.dafang.monitor.nx.doc.DocAbstrator;
import com.dafang.monitor.nx.doc.entity.emun.SlotEmun;
import com.dafang.monitor.nx.doc.entity.po.Doc;
import com.dafang.monitor.nx.doc.entity.po.DocParams;
import com.dafang.monitor.nx.doc.mapper.DocMapper;
import com.dafang.monitor.nx.utils.CommonUtils;
import com.dafang.monitor.nx.utils.DateUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class DocService extends DocAbstrator {

    @Autowired
    private DocMapper mapper;

    public static void main(String[] args) {
        DocParams params = new DocParams("20190501");
        params.setRegions("1");
        params.setCondition(CommonUtils.getCondition(params.getRegions()));
        new DocService().init(params);
    }

    @Override
    public void init(DocParams params) {
        Calendar c = Calendar.getInstance();
        endDate = params.getEndDate();
        year = endDate.substring(0,4);
        Date et = DateUtil.strToDate(endDate, "yyyyMMdd");
        c.setTime(et);
        for (SlotEmun value : SlotEmun.values()) {
            c.add(Calendar.DAY_OF_MONTH,-value.getDays());
            startDate = DateUtil.dateToStr(c.getTime(), "yyyyMMdd");
            params.setST(startDate.substring(4,8));
            params.setET(endDate.substring(4,8));
            TemAbnormal(params,value.getName());//气温异常
        }
    }

    /*
    气温异常
     */
    public void TemAbnormal(DocParams params,String slot){
        List<Map<String,Object>> excelList = new ArrayList<>();
        List<Map<String,Object>> drawList = new ArrayList<>();

        //平均气温
        params.setMax(999.0);
        params.setMin(-999.0);
        params.setElement("TEM_Avg");
        params.setCal("AVG");
        List<Doc> baseDate = mapper.periodsList(params);
        sort(excelList,baseDate,slot);//异常筛选
        //高温日数
        params.setMax(999.0);
        params.setMin(35.0);
        baseDate = mapper.periodDays(params);
        sort(excelList,baseDate,slot);//异常筛选


    }

    public void sort(List<Map<String,Object>> excelList,List<Doc> baseDate,String slot){
        baseDate = baseDate.stream().filter(x-> Convert.toDouble(x.getYear()) > 1961).collect(Collectors.toList());
        List<String> stationNos = baseDate.stream().map(x -> x.getStationNo()).distinct().collect(Collectors.toList());
        for (String stationNo : stationNos) {
            Map<String,Object> map = new HashMap<>();
            //单站数据
            List<Doc> singleList = baseDate.stream().filter(x -> StringUtils.equals(x.getStationNo(), stationNo)).collect(Collectors.toList());
            //单站当年数据
            Doc doc = singleList.stream().filter(x -> StringUtils.equals(x.getYear(), year)).collect(Collectors.toList()).get(0);
            //数据集合
            List<Double> valList = singleList.stream().map(x -> Convert.toDouble(NumberUtil.round(x.getVal(),1))).collect(Collectors.toList());
            Collections.sort(valList);//正序排列
            double round = Convert.toDouble(NumberUtil.round(doc.getVal(), 1));
            int sortAsc = valList.indexOf(round) + 1;//正序排位
            Collections.sort(valList,Collections.reverseOrder());//倒序
            int sortDesc = valList.indexOf(round) + 1;//倒序排位
            map.put("stationNo",stationNo);
            map.put("stationName",doc.getStationName());
            map.put("MonitorIndicators","平均气温");//监测指标
            map.put("slot",slot);//时间段
            map.put("startDate",startDate);
            map.put("endDate",endDate);
            map.put("value",doc.getVal());
            if (sortAsc <= 5){
                map.put("sort",sortAsc);
                map.put("sortName","正序");
                excelList.add(map);
            }
            if (sortDesc <= 5){
                map.put("sort",sortDesc);
                map.put("sortName","倒序");
                excelList.add(map);
            }
        }
    }
}
