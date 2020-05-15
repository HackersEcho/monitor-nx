package com.dafang.monitor.nx.statistics.service.impl;


import com.dafang.monitor.nx.statistics.entity.po.DailyParam;
import com.dafang.monitor.nx.statistics.entity.po.InfoParam;
import com.dafang.monitor.nx.statistics.mapper.BaseMapper;
import com.dafang.monitor.nx.statistics.service.BaseService;
import com.dafang.monitor.nx.utils.CommonUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class BaseServiceImpl implements BaseService {
    @Autowired
    private BaseMapper mapper;

    @Override
    public List<Map<String, Object>> staInfoList(DailyParam params) {
        // 获取数据
        List<Map<String, Object>> resList = mapper.staInfoList(params);
        return resList;
    }

    @Override
    public PageInfo infoList(InfoParam params) {
        String elements = params.getElement();
        String condition = CommonUtils.getCondition(params.getRegions());
        // 如果是天气现象的查询，传进来的要素为天气现象码
        if (StringUtils.equalsAnyIgnoreCase(params.getTableName(), "t_mete_ns_day_wea")) {
            condition += " AND t.WEP_Code IN (" + elements + ") ";
            elements = "WEP_Name,WEP_Code";
        }else {
            String thresholdcondition = " AND t." + params.getElement() + " BETWEEN " + params.getMin() + " AND " + params.getMax();// 当为单要素是
            if ((params.getElement().contains(","))) {// 多要素时或者阈值为空时不统计阈值
                thresholdcondition = "";
            }
            condition += thresholdcondition;
        }
        String sql = "SELECT t.stationNo,DATE_FORMAT(t.ObserverTime,'%Y%m%d %H') AS observerTime"
                + ",b.station_name ,b.latitude as lat,b.longitude as lon,t."
                + elements + "" + " FROM " + params.getTableName() + " t RIGHT JOIN geography_station_info b ON t.stationNo = b.device_id"
                + " WHERE t.ObserverTime BETWEEN '" + params.getStartDate() + "' AND '" + params.getEndDate() + "' AND " + condition;

        PageHelper.startPage(params.getPageNo(), params.getPageSize());
        List<Map<String, Object>> maps = mapper.infoList(sql);
        PageInfo pageInfo = new PageInfo(maps);
        return pageInfo;
    }

}
