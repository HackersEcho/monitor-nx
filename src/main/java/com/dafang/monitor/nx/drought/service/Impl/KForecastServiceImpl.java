package com.dafang.monitor.nx.drought.service.Impl;

import com.dafang.monitor.nx.drought.entity.po.Drought;
import com.dafang.monitor.nx.drought.entity.po.DroughtParam;
import com.dafang.monitor.nx.drought.mapper.DroughtMapper;
import com.dafang.monitor.nx.drought.service.KForecastService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class KForecastServiceImpl implements KForecastService {

    @Autowired
    DroughtMapper mapper;

    @Override
    public List<Map<String, Object>> getKForecast(DroughtParam params) {
        List<Map<String,Object>> result = new ArrayList<>();
        KServiceImpl kService = new KServiceImpl();
        params.setST(params.getStartDate().substring(4,8));
        params.setET(params.getEndDate().substring(4,8));
        //获得时间区间
        List<String> times = kService.getTime(params.getStartDate(), params.getEndDate());
        //降水常年值
        params.setElement("PRE_Time_2020");
        List<Drought> prePerenVal = mapper.getPerenVal(params);
        //蒸散量常年值
        params.setCode("0");
        params.setElement("PE_VAL");
        List<Drought> peList = mapper.getPeriodData(params);
        List<Drought> pePerenVal = kService.getPEPerenVal(peList);
        params.setST("");
        params.setET("");
        for (String time : times) {
            params.setEndDate(time);
            //降水相对变率
            params.setCal("SUM");
            params.setCode("4");
            params.setElement("preValue");
            params.setStartDate(kService.getStartDate(time,30));
            List<Drought> preSum30 = mapper.getContinue(params);
            params.setStartDate(kService.getStartDate(time,90));
            List<Drought> preSum90 = mapper.getContinue(params);
            List<Drought> preRve30 = kService.getRveList(preSum30,prePerenVal);//30天降水相对变率
            List<Drought> preRve90 = kService.getRveList(preSum90,prePerenVal);//90天降水相对变率
            //蒸散量相对变率
            params.setCal("SUM");
            params.setCode("4");
            params.setElement("temValue");
            params.setStartDate(kService.getStartDate(time,30));
            List<Drought> peSum30 = mapper.getContinue(params);
            params.setStartDate(kService.getStartDate(time,90));
            List<Drought> peSum90 = mapper.getContinue(params);
            List<Drought> peRve30 = kService.getRveList(peSum30,pePerenVal);//30天降水相对变率
            List<Drought> peRve90 = kService.getRveList(peSum90,pePerenVal);//90天降水相对变率
            //K指数
            List<Drought> k30 = kService.getRveList(preRve30, peRve30);
            List<Drought> k90 = kService.getRveList(preRve90, peRve90);
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
                        map.put("K30Level",kService.getLevel(K30));
                        map.put("K90",K90);
                        map.put("K90Level",kService.getLevel(K90));
                        result.add(map);
                    }
                }
            }
        }
        return result;
    }
}
