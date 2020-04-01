package com.dafang.monitor.nx.accessment.service.impl;

import com.dafang.monitor.nx.accessment.entity.po.Comfort;
import com.dafang.monitor.nx.accessment.entity.po.ComfortParam;
import com.dafang.monitor.nx.accessment.mapper.HumanComfortMapper;
import com.dafang.monitor.nx.accessment.service.HumanComfortService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class HumanComfortServiceImpl implements HumanComfortService {

    @Autowired
    private HumanComfortMapper humanComfortMapper;

    @Override
    public List<Comfort> queryContinueComfortValue(ComfortParam comfortParam) {
        List<Comfort> baseData = humanComfortMapper.findContinueComfortValue(comfortParam);
        List<String> staList = baseData.parallelStream().map(x -> x.getStationNo()).distinct().collect(Collectors.toList());
        for (String station : staList) {
            List<Comfort> singleList = baseData.stream().filter(x -> StringUtils.equals(station, x.getStationNo())).collect(Collectors.toList());

        }

//        baseData.parallelStream().filter(x->)

        return null;
    }

    @Override
    public List<Comfort> queryPeriodComfortValue(ComfortParam comfortParam) {
        List<Comfort> baseData = humanComfortMapper.findPeriodComfortValue(comfortParam);

        return null;
    }
}
