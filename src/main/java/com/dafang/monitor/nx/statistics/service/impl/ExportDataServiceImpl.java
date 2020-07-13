package com.dafang.monitor.nx.statistics.service.impl;

import com.dafang.monitor.nx.statistics.entity.po.ExportDataParam;
import com.dafang.monitor.nx.statistics.mapper.ExportDataMapper;
import com.dafang.monitor.nx.statistics.service.ExportDataService;
import com.dafang.monitor.nx.utils.CommonUtils;
import com.dafang.monitor.nx.utils.ImportToExcel;
import com.dafang.monitor.nx.utils.PropertiesConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class ExportDataServiceImpl implements ExportDataService {

    @Autowired
    private ExportDataMapper exportDataMapper;

    @Override
    public File exportData(ExportDataParam params) {
        Map<String, Object> map = new HashMap<>();
        ImportToExcel im = new ImportToExcel();
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
        int count = 0;// 符合条件的总条数
        // 压缩文件
        File zip = new File(
                PropertiesConfig.readData("excelFilePath") + params.getFileNames() + "_" + format.format(new Date()) + ".zip");// 压缩文件
        String[] regionID = params.getRegions().split(",");
        CommonUtils.getCondition(params.getRegions());

        //查询数据
        List<Map<String, Object>> list = exportDataMapper.getBasicsDataQueryDaoByRegion(params.getTable(), params.getCol(),
                params.getColStr(), params.getStartTime(), params.getEndTime(), params.getCondition(),
                params.getMin(), params.getMax(), 0, 120000);
        //查询总条数
        count = exportDataMapper.selectCount(params.getTable(), params.getCol(), params.getStartTime(), params.getEndTime(), params.getCondition(), params.getMin(), params.getMax());
        // 用于存放生成的文件名称集合
        List<String> fileNames = new ArrayList<>();
        // 2.判断符合条件的数据总数是否大于最大限制（规定为60w，主要是为了减少内存占用，和响应时间）
        if (count > 120000) {
            // 2.1向上取整得到查询次数
            double result = count / 120000.0;
            int queryCount = (int) Math.ceil(result);
            // 2.1根据数据量进行多次分页查询
            for (int a = 0; a < queryCount; a++) {
                // 2.2计算当前分页开始位置，和所查数据量
                int index = a * 120000 + 1;
                int rows = count - a * 120000 > 120000 ? 120000 : count - a * 120000;
                // 2.3根据分页情况查询数据
                List<Map<String, Object>> tempList = exportDataMapper.getBasicsDataQueryDaoByRegion(params.getTable(), params.getCol(), params.getColStr(), params.getStartTime(),
                        params.getEndTime(), params.getCondition(), params.getMin(), params.getMax(), index, rows);
                Date date = new Date();
                // 生成文件名称：文件名_生成时间
                String fie_Name = params.getFileNames() + "_" + format.format(date);
                try {
                    // 分文件导出数据
                    if (params.getFileType().equals("csv")) {
                        im.toCSV(tempList, 60000, fie_Name, params.getColStr(), params.getColTextStr(), fileNames);
                    } else {
                        im.toTxt(tempList, 60000, fie_Name, params.getColStr(), params.getColTextStr(), fileNames);
                    }
                } catch (IOException e) {
                    System.err.println("分文件导出" + e);
                }
            }
        } else {
            if (list.size() > 0) {
                Date date = new Date();
                // 生成文件名称：文件名_生成时间
                String fie_Name = params.getFileNames() + "_" + format.format(date);
                try {
                    // 分文件导出数据
                    list.remove(list.size() - 1);
                    if (params.getFileType().equals("csv")) {
                        im.toCSV(list, 60000, fie_Name, params.getColStr(), params.getColTextStr(), fileNames);
                    } else {
                        im.toTxt(list, 60000, fie_Name, params.getColStr(), params.getColTextStr(), fileNames);
                    }
                } catch (IOException e) {
                    System.err.println("单文件导出报错" + e);
                }
            }
            // 3.压缩文件
            File srcfile[] = new File[fileNames.size()];
            for (int i = 0, n = fileNames.size(); i < n; i++) {
                srcfile[i] = new File(fileNames.get(i));
            }
            im.ZipFiles(srcfile, zip);
            // 5.删除原来的文件
            // 5.1刪除excel文件
            for (String file : fileNames) {
                delFile(file);
            }
        }


        return zip;
    }

    /**
     * 删除文件
     */
    public void delFile(String filename) {
        File file = new File(filename);
        if (file.exists() && file.isFile())
            file.delete();
    }
}
