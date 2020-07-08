package com.dafang.monitor.nx.drought.service.Impl;

import com.dafang.monitor.nx.drought.entity.po.Drought;
import com.dafang.monitor.nx.drought.entity.po.DroughtParam;
import com.dafang.monitor.nx.drought.mapper.DroughtMapper;
import com.dafang.monitor.nx.drought.service.CIService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CIServiceImpl implements CIService {

    @Autowired
    DroughtMapper mapper;

    @Override
    public List<Map<String, Object>> getCI(DroughtParam params) {
        List<Map<String, Object>> result = new ArrayList<>();
        List<Drought> baseData = mapper.getContinueData(params);
        for (Drought base : baseData) {
            Double ciVal = base.getVal();
            Map<String,Object> map = new HashMap<>();
            String level = "";
            if (ciVal > -0.6){
                level = "无旱";
            }else if (ciVal > -1.2 && ciVal <= -0.6){
                level = "轻旱";
            }else if (ciVal > -1.8 && ciVal <= -1.2){
                level = "中旱";
            }else if (ciVal > -2.4 && ciVal <= -1.8){
                level = "重旱";
            }else if (ciVal <= -2.4){
                level = "特旱";
            }
            map.put("stationNo",base.getStationNo());
            map.put("stationName",base.getStationName());
            map.put("longitude",base.getLongitude());
            map.put("latitude",base.getLatitude());
            map.put("ciVal",base.getVal());
            map.put("level",level);
            result.add(map);
        }
        return result;
    }
}
