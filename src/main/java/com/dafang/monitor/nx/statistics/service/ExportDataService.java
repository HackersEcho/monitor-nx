package com.dafang.monitor.nx.statistics.service;

import com.dafang.monitor.nx.statistics.entity.po.ExportDataParam;

import java.io.File;

/**
 * @description:导出数据接口
 * @author: Renxin
 * @createDate: 2020/06/16
 * @version: 1.0
 */
public interface ExportDataService {
    //导出数据
    File exportData(ExportDataParam params);
}
