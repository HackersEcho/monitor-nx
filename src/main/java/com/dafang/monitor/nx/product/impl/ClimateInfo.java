package com.dafang.monitor.nx.product.impl;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.NumberUtil;
import com.dafang.monitor.nx.entity.RegionStaEnum;
import com.dafang.monitor.nx.product.TemplateAbstract;
import com.dafang.monitor.nx.product.entity.emun.ProductEmun;
import com.dafang.monitor.nx.product.entity.po.ProductParams;
import com.dafang.monitor.nx.product.mapper.ProductMapper;
import com.dafang.monitor.nx.utils.ReflectHandleUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 重要气候信息
 */
@Service()
public class ClimateInfo extends TemplateAbstract {

    @Autowired
   private ProductMapper mapper;

    @Override
    protected void init(ProductParams params) {
        startData = params.getStartDate().substring(0,4)+"年"+params.getStartDate().substring(4)+"日";
        endData = params.getEndDate().substring(0,4)+"年"+params.getEndDate().substring(4)+"日";
        year = Convert.toInt(startData.substring(0,4));
        templateName = startData+"-"+endData+ProductEmun.getFileName(3);
        baseData = mapper.periodList(params);
        currentList = baseData.stream().filter(x-> x.getYear() == year).collect(Collectors.toList());
        perenList = baseData.stream().filter(x->x.getYear() >= 1981 && x.getYear() <= 2010).collect(Collectors.toList());
    }

    @Override
    protected Map<String, Object> getDatas() {
        Map<String,Object> result = new HashMap<>();
        Map<String,Object> map = new HashMap<>();
        String[] regionids = {"1-全区","4-引黄灌区","5-中部干旱带","6-南部山区"};
        String[] factors = {"Tem_Avg-.getTEM_Avg()","PRE_Time_2020-.getPRE_Time_2020()"};
        for (String factor : factors) {
            String method = factor.split("-")[1];
            String fac = factor.split("-")[0];
            for (String region : regionids) {
                double liveVal = currentList.stream().filter(x -> {
                    for (String sta : RegionStaEnum.getStas(region.split("-")[0])) {
                        StringUtils.equals(x.getStationNo(), sta);
                    }
                    return false;
                }).mapToDouble(x -> Convert.toDouble(x+method)).summaryStatistics().getAverage();
                double perenVal = ReflectHandleUtils.getValByOp(ReflectHandleUtils.filterData(perenList, fac), fac, "avg");
                double annomlyVal = liveVal - perenVal;
                if (factor.equals("PRE_Time_2020")){
                    annomlyVal = (liveVal-perenVal)/perenVal*100;
                }
                map.put(region+"-"+fac+"-liveVal", NumberUtil.round(liveVal,2));
                map.put(region+"-"+fac+"-perenVal", NumberUtil.round(perenVal,2));
                map.put(region+"-"+fac+"-annomlyVal", NumberUtil.round(annomlyVal,2));
            }
        }
//        "X月X日～X月X日以来，我区气温总体偏X，降水偏X。" +
//                "全区平均气温为X℃，较常年同期偏高X℃；其中引黄灌区、中部干旱带、南部山区平均气温分别为X℃、X℃、X℃，偏X X℃、X℃、X℃。" +
//                "全区平均降水量为Xmm，较常年同期偏多X%；其中引黄灌区、中部干旱带、南部山区分别为Xmm、Xmm、Xmm，偏 X X%、X%、X%。";
        String str = "%s～%s以来，我区气温总体偏%s，降水偏%s。";
//                +
//                "全区平均气温为%s℃，较常年同期偏%s%s℃；其中引黄灌区、中部干旱带、南部山区平均气温分别为%s℃、%s℃、%s℃，偏%s %s℃、%s℃、%s℃。" +
//                "全区平均降水量为%smm，较常年同期偏多%s%；其中引黄灌区、中部干旱带、南部山区分别为%smm、%smm、%smm，偏 %s %s%、%s%、%s%。";
        String s1 = startData;
        String s2 = endData;
        String s3 = Convert.toDouble(map.get("全区-Tem_Avg-annomlyVal")) > 0 ? "高" : "低";
        String s4 = Convert.toDouble(map.get("全区-PRE_Time_2020-annomlyVal")) > 0 ? "多" : "少";
        str = String.format(s1,s2,s3,s4);
        return null;
    }


}
