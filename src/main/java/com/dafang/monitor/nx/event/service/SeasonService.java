package com.dafang.monitor.nx.event.service;

import com.dafang.monitor.nx.event.entity.po.SeasonParam;
import com.dafang.monitor.nx.event.entity.vo.FourSeason;

import java.util.List;

/**
 * @description:
 * @author: echo
 * @createDate: 2020/4/23
 * @version: 1.0
 */
public interface SeasonService {
    // 入季出季数据查询
    List<FourSeason> inOutSeason(SeasonParam params);
    // 季节长度
    List<FourSeason> seasonLen(SeasonParam params);
}
