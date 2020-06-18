package com.dafang.monitor.nx.doc.impl;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.NumberUtil;
import com.dafang.monitor.nx.doc.DocAbstrator;
import com.dafang.monitor.nx.doc.entity.emun.IndicatorsEmun;
import com.dafang.monitor.nx.doc.entity.emun.SlotEnum;
import com.dafang.monitor.nx.doc.entity.po.Doc;
import com.dafang.monitor.nx.doc.entity.po.DocParams;
import com.dafang.monitor.nx.doc.mapper.DocMapper;
import com.dafang.monitor.nx.utils.DateUtil;
import com.dafang.monitor.nx.utils.ExpExcelUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.io.FileOutputStream;
import java.util.List;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class DocEventService extends DocAbstrator {

    @Autowired
    private DocMapper mapper;

    @Override
    public void init(DocParams params,String timeType) {
        filePath = "F:/NX_file/kmlfile/";//生成路径
        endDate = params.getEndDate();
        year = endDate.substring(0,4);
        if (StringUtils.equals(timeType,"DAY")) {
            date = endDate.substring(0,4) + "年" + endDate.substring(4,6) + "月" + endDate.substring(6,8) + "日";
        }else{//评价计算上个月的x
            Calendar c = Calendar.getInstance();
            Date yyyyMMdd = DateUtil.strToDate(endDate, "yyyyMMdd");
            c.setTime(yyyyMMdd);
            c.add(Calendar.MONTH,-1);
            endDate = DateUtil.dateToStr(c.getTime(), "yyyyMMdd");
            startDate = endDate.substring(0,6)+"01";
            endDate = endDate.substring(0,6)+"31";
            year = endDate.substring(0,4);
            date = endDate.substring(0,4) + "年" + endDate.substring(4,6) + "月";
        }
    }

    @Override
    public void process(DocParams params,String directoryName,String timeType) {
        List<List<String>> excelList = new ArrayList<>();
        excelName = date + directoryName + "异常信息列表.xlsx";
        for (IndicatorsEmun indicators : IndicatorsEmun.values()) {//遍历监测指标
            indicator = indicators.getIndicators();
            indicatorName = indicators.getName();
            drawValue = indicators.getDrawValue();
            sortYear = indicators.getSortYear();
            sheetHead = indicators.getSheetHead();
            if(StringUtils.equals(directoryName,indicator)){
                if (StringUtils.equals(indicator,"气温") || StringUtils.equals(indicator,"降水") || StringUtils.equals(indicator,"霜冻")) {
                    for (SlotEnum slots : SlotEnum.values()) {//遍历时间尺度
                        if (StringUtils.equals(slots.getTimeType(),timeType)){
                            slotName = slots.getName();
                            Calendar c = Calendar.getInstance();
                            Date et = DateUtil.strToDate(endDate,"yyyyMMdd");
                            c.setTime(et);
                            c.add(Calendar.DAY_OF_MONTH, -slots.getDays());
                            startDate = DateUtil.dateToStr(c.getTime(), "yyyyMMdd");
                            params.setST(startDate.substring(4, 8));
                            params.setET(endDate.substring(4, 8));
                            params.setElement(indicators.getElement());
                            params.setCal(indicators.getCal());
                            params.setMin(Convert.toDouble(indicators.getSlot().split("_")[0]));
                            params.setMax(Convert.toDouble(indicators.getSlot().split("_")[1]));
                            excelList.addAll(TpAbnormalProcess(params));
                        }
                    }
                }else if(StringUtils.equals(indicator,"首场透雨")){
                    params.setElement(indicators.getElement());
                    excelList.addAll(AbnormalProcess(params));
                }else if(StringUtils.equals(indicator,"四季入季")){
                    params.setCal(indicators.getCal());
                    excelList.addAll(AbnormalProcess(params));
                }
            }
        }
        //异常列表生成
        createExcel(excelList);
    }

    /**
     * 气温降水异常处理
     * @param params
     * @return
     */
    public List<List<String>> TpAbnormalProcess(DocParams params){
        List<List<String>> excelList = new ArrayList<>();
        List<Doc> baseDate = new ArrayList<>();
        //异常列表
        if (StringUtils.contains(drawValue,"Day")){
            baseDate = mapper.periodDays(params);
        }else{
            baseDate = mapper.periodsList(params);
        }
        sort(excelList,baseDate);//异常筛选
        return excelList;
    }
    /**
     * 首场透雨异常处理
     * @param params
     * @return
     */
    public List<List<String>> AbnormalProcess(DocParams params){
        List<List<String>> excelList = new ArrayList<>();
        List<Doc> baseDate = new ArrayList<>();
        //异常列表
        if (StringUtils.equals(indicator,"首场透雨")){
            baseDate = mapper.getFirstSoaker(params);
        }else if (StringUtils.equals(indicator,"四季入季")){
            baseDate = mapper.getSeason(params);
        }
//        for (Doc doc : baseDate) {
//            if (StringUtils.equals(doc.getTime(),endDate)){
                sort(excelList,baseDate);//异常筛选
//            }
//        }
        return excelList;
    }


    /**
     * 创建Excel
     * @param dataList
     */
    public void createExcel(List<List<String>> dataList){
//        String sheetHead = "站号,站名,监测指标,时间尺度,开始日期,结束日期,值,起始年限,历史排位,排名顺序";
        ExpExcelUtils expExcelUtils = new ExpExcelUtils();
        XSSFWorkbook xwb = new XSSFWorkbook();
        expExcelUtils.createExcel(xwb,excelName,excelName,sheetHead.split(","),new Color(208, 221, 241),dataList.size(), dataList);
        try {
            FileOutputStream output = new FileOutputStream(filePath + excelName);
            xwb.write(output);
            output.flush();
            output.close();
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    /**
     * 排序得到排位满足异常条件的集合
     * @param excelList 结果结合
     * @param baseDate 基础数据
     * @return
     */
    public void sort(List<List<String>> excelList,List<Doc> baseDate){
        baseDate = baseDate.stream().filter(x->x.getYear().length() == 4).collect(Collectors.toList());
        baseDate = baseDate.stream().filter(x-> Convert.toInt(x.getYear()) >= sortYear).collect(Collectors.toList());
        List<String> stationNos = baseDate.stream().map(x -> x.getStationNo()).distinct().collect(Collectors.toList());
        for (String stationNo : stationNos) {
            List<String> list = new ArrayList<>();
            Map<String,Object> map = new HashMap<>();
            //单站数据
            List<Doc> singleList = baseDate.stream().filter(x -> StringUtils.equals(x.getStationNo(), stationNo)).collect(Collectors.toList());
            //单站当年数据
            List<Doc> currentSingleList = singleList.stream().filter(x -> StringUtils.equals(x.getYear(), year)).collect(Collectors.toList());
            if (currentSingleList.size() > 0){
                Doc doc = currentSingleList.get(0);
                //数据集合
                List<Double> valList = singleList.stream().map(x -> Convert.toDouble(NumberUtil.round(x.getVal(),1))).collect(Collectors.toList());
                Collections.sort(valList);//正序排列
                double round = Convert.toDouble(NumberUtil.round(doc.getVal(), 1));
                int sortAsc = valList.indexOf(round) + 1;//正序排位
                Collections.sort(valList,Collections.reverseOrder());//倒序
                int sortDesc = valList.indexOf(round) + 1;//倒序排位
                list.add(stationNo);//站号
                list.add(doc.getStationName());//站名
                list.add(indicatorName);//监测指标
                if(StringUtils.equals(indicator,"气温") || StringUtils.equals(indicator,"降水") || StringUtils.equals(indicator,"霜冻")){
                    list.add(slotName);//时间尺度
                    list.add(startDate);//开始时间
                }
                list.add(endDate);//结束时间
                list.add(NumberUtil.round(doc.getVal(),1)+"");//值
                list.add(sortYear+"");//起始年份
                if (sortAsc <= 5){
                    list.add(sortAsc+"");//排位
                    list.add("正序");
                    excelList.add(list);
                }
                if (sortDesc <= 5){
                    list.add(sortDesc+"");
                    list.add("倒序");
                    excelList.add(list);
                }
            }
        }
    }
}
