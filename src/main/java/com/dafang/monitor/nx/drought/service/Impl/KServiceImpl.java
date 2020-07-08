package com.dafang.monitor.nx.drought.service.Impl;

import cn.hutool.core.util.NumberUtil;
import com.dafang.monitor.nx.drought.entity.po.Drought;
import com.dafang.monitor.nx.drought.entity.po.DroughtParam;
import com.dafang.monitor.nx.drought.mapper.DroughtMapper;
import com.dafang.monitor.nx.utils.DateUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class KServiceImpl {

    @Autowired
    DroughtMapper mapper;

    public List<Map<String,Object>> getK(DroughtParam params){
        List<Map<String,Object>> result = new ArrayList<>();
        params.setST(params.getStartDate().substring(4,8));
        params.setET(params.getEndDate().substring(4,8));
        //获得时间区间
        List<String> times = getTime(params.getStartDate(), params.getEndDate());
        //降水常年值
        params.setElement("PRE_Time_2020");
        List<Drought> prePerenVal = mapper.getPerenVal(params);
        //蒸散量常年值
        params.setCode("0");
        params.setElement("PE_VAL");
        List<Drought> peList = mapper.getPeriodData(params);
        List<Drought> pePerenVal = getPEPerenVal(peList);
        params.setST("");
        params.setET("");
        for (String time : times) {
            params.setEndDate(time);
            //降水相对变率
            params.setCal("SUM");
            params.setCode("1");
            params.setElement("PRE_Time_2020");
            params.setStartDate(getStartDate(time,30));
            List<Drought> preSum30 = mapper.getContinue(params);
            params.setStartDate(getStartDate(time,90));
            List<Drought> preSum90 = mapper.getContinue(params);
            List<Drought> preRve30 = getRveList(preSum30,prePerenVal);//30天降水相对变率
            List<Drought> preRve90 = getRveList(preSum90,prePerenVal);//90天降水相对变率
            //蒸散量相对变率
            params.setCal("SUM");
            params.setCode("0");
            params.setElement("PE_VAL");
            params.setStartDate(getStartDate(time,30));
            List<Drought> peSum30 = mapper.getContinue(params);
            params.setStartDate(getStartDate(time,90));
            List<Drought> peSum90 = mapper.getContinue(params);
            List<Drought> peRve30 = getRveList(peSum30,pePerenVal);//30天降水相对变率
            List<Drought> peRve90 = getRveList(peSum90,pePerenVal);//90天降水相对变率
            //K指数
            List<Drought> k30 = getRveList(preRve30, peRve30);
            List<Drought> k90 = getRveList(preRve90, peRve90);
            for (Drought drought1 : k30) {
                for (Drought drought2 : k90) {
                    if (StringUtils.equals(drought1.getStationNo(),drought2.getStationNo())){
                        Map<String,Object> map = new HashMap<>();
                        double K30 = drought1.getVal();
                        double K90 = drought2.getVal();
                        map.put("stationNo",drought1.getStationNo());
                        map.put("stationName",drought1.getStationName());
                        map.put("observerTime",time);
                        map.put("K30",K30);
                        map.put("K30Level",getLevel(K30));
                        map.put("K90",K90);
                        map.put("K90Level",getLevel(K90));
                        result.add(map);
                    }
                }
            }
        }
        return result;
    }

    /**
     * 得到干旱等级
     * @param KValue
     * @return
     */
    public String getLevel(double KValue){
        String level = "";
        if(KValue>2){
            level = "湿润";
        }else if(KValue<=2 && KValue>1.5){
            level = "适宜";
        }else if(KValue<=1.5 && KValue>1){
            level = "轻旱";
        }else if(KValue<= 1 && KValue>0.5){
            level = "中旱";
        }else if(KValue<=0.5){
            level = "重旱";
        }
        return level;
    }

    /**
     * 两集合内容相除
     * @param list1
     * @param list2
     * @return
     */
    public List<Drought> getRveList(List<Drought> list1,List<Drought> list2){
        List<Drought> rveList = new ArrayList<>();
        for (Drought drought1 : list1) {
            for (Drought drought2 : list2) {
                if (StringUtils.equals(drought1.getStationNo(),drought2.getStationNo())){
                    Drought drought = new Drought();
                    double rve = NumberUtil.div(drought1.getVal(), drought2.getVal(), 1);
                    drought.setStationNo(drought1.getStationNo());
                    drought.setStationName(drought1.getStationName());
                    drought.setVal(rve);
                    rveList.add(drought);
                }
            }
        }
        return rveList;
    }

    /**
     * 计算时间
     * @param endDate
     * @param days
     * @return
     */
    public String getStartDate(String endDate,int days){
        Calendar c = Calendar.getInstance();
        c.setTime(DateUtil.strToDate(endDate,"yyyyMMdd"));
        c.add(Calendar.DAY_OF_MONTH,-days);
        return DateUtil.dateToStr(c.getTime(),"yyyyMMdd");
    }

    /**
     * 蒸散量常年值
     * @param list
     * @return
     */
    public List<Drought> getPEPerenVal(List<Drought> list){
        List<Drought> PEPerenVal = new ArrayList<>();
        List<String> stationNos = list.stream().map(Drought::getStationNo).distinct().collect(Collectors.toList());
        for (String stationNo : stationNos) {
            double perenVal = list.stream().filter(x -> StringUtils.equals(stationNo, x.getStationNo()) && x.getYear() >= 1981 && x.getYear() <= 2010)
                    .mapToDouble(Drought::getVal).summaryStatistics().getSum()/30;
            Drought drought = new Drought();
            drought.setStationNo(stationNo);
            drought.setVal(perenVal);
            PEPerenVal.add(drought);
        }
        return PEPerenVal;
    }

    /**
     * 获取时间段集合
     * @param startDate
     * @param endDate
     * @return
     */
    public List<String> getTime(String startDate,String endDate){
        List<String> result = new ArrayList<>();
        Calendar c1 = Calendar.getInstance();
        Calendar c2 = Calendar.getInstance();
        Date st = DateUtil.strToDate(startDate, "yyyyMMdd");
        Date et = DateUtil.strToDate(endDate, "yyyyMMdd");
        c1.setTime(st);
        c2.setTime(et);
        while (c1.getTime().getTime() <= c2.getTime().getTime()){
            result.add(DateUtil.dateToStr(c1.getTime(),"yyyyMMdd"));
            c1.add(Calendar.DAY_OF_MONTH,1);
        }
        return result;
    }
}
