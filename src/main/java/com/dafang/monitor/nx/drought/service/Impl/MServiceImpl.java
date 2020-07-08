package com.dafang.monitor.nx.drought.service.Impl;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.NumberUtil;
import com.dafang.monitor.nx.drought.entity.po.Drought;
import com.dafang.monitor.nx.drought.entity.po.DroughtParam;
import com.dafang.monitor.nx.drought.mapper.DroughtMapper;
import com.dafang.monitor.nx.drought.service.MService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class MServiceImpl implements MService {
    
    @Autowired
    DroughtMapper mapper;

    /**
     * 连续相对湿润度
     * @param params
     * @return
     */
    public List<Map<String,Object>> getContiuneM(DroughtParam params){
        List<Map<String,Object>> result = new ArrayList<>();
        params.setCal("SUM");
        //蒸散量
        params.setElement("PE_VAL");
        params.setCode("0");
        List<Drought> pes = mapper.getContinue(params);
        //降水
        params.setElement("PRE_Time_2020");
        params.setCode("1");
        List<Drought> pres = mapper.getContinue(params);
        for (Drought value1 : pres) {
            for (Drought value2 : pes) {
                Map<String,Object> map = new HashMap<>();
                if (StringUtils.equals(value1.getStationNo(),value2.getStationNo())){
                    double pre = Convert.toDouble(value1.getVal());
                    double pe = Convert.toDouble(value2.getVal());
                    double M = NumberUtil.div(pre-pe,pe,2);
                    map.put("stationNo",value1.getStationNo());
                    map.put("stationName",value1.getStationName());
                    map.put("value",M);
                    if (M > -0.4) {
                        map.put("level", "无旱");
                    } else if (M > -0.65 && M <= -0.4) {
                        map.put("level", "轻旱");
                    } else if (M > -0.8 && M <= -0.65) {
                        map.put("level", "中旱");
                    } else if (M > -0.95 && M <= -0.8) {
                        map.put("level", "重旱");
                    } else {
                        map.put("level", "特旱");
                    }
                    result.add(map);
                }
            }
        }
        return result;
    }

    /**
     * 同期相对湿润度
     * @param params
     * @return
     */
    public List<Map<String,Object>> getPeriodM(DroughtParam params){
        List<Map<String,Object>> result = new ArrayList<>();
        params.setCal("SUM");
        params.setST(params.getStartDate().substring(4,8));
        params.setET(params.getEndDate().substring(4,8));
        int sYear = Integer.valueOf(params.getStartDate().substring(0,4));
        int eYear = Integer.valueOf(params.getEndDate().substring(0,4));
        //蒸散量
        params.setElement("PE_VAL");
        params.setCode("0");
        List<Drought> pes = mapper.getPeriod(params);
        pes = pes.stream().filter(x -> x.getYear() >= eYear && x.getYear() <= sYear).collect(Collectors.toList());
        //降水
        params.setElement("PRE_Time_2020");
        params.setCode("1");
        List<Drought> pres = mapper.getPeriod(params);
        pres = pres.stream().filter(x -> x.getYear() >= eYear && x.getYear() <= sYear).collect(Collectors.toList());
        for (Drought value1 : pres) {
            for (Drought value2 : pes) {
                Map<String,Object> map = new HashMap<>();
                if (StringUtils.equals(value1.getStationNo(),value2.getStationNo()) && value1.getYear().equals(value2.getYear())){
                    double pre = Convert.toDouble(value1.getVal());
                    double pe = Convert.toDouble(value2.getVal());
                    double M = NumberUtil.div(pre-pe,pe,2);
                    map.put("stationNo",value1.getStationNo());
                    map.put("stationName",value1.getStationName());
                    map.put("year",value1.getYear());
                    map.put("value",M);
                    if (M > -0.4) {
                        map.put("level", "无旱");
                    } else if (M > -0.65 && M <= -0.4) {
                        map.put("level", "轻旱");
                    } else if (M > -0.8 && M <= -0.65) {
                        map.put("level", "中旱");
                    } else if (M > -0.95 && M <= -0.8) {
                        map.put("level", "重旱");
                    } else {
                        map.put("level", "特旱");
                    }
                    result.add(map);
                }
            }
        }
        return result;
    }

}
