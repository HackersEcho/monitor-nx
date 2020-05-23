package com.dafang.monitor.nx.product.impl.evaluate;

import cn.hutool.core.convert.Convert;
import com.dafang.monitor.nx.accessment.entity.emun.ComfortLevelEnum;
import com.dafang.monitor.nx.entity.RegionStaEnum;
import com.dafang.monitor.nx.entity.SeasonEnum;
import com.dafang.monitor.nx.product.TemplateAbstract;
import com.dafang.monitor.nx.product.entity.emun.ProductEmun;
import com.dafang.monitor.nx.product.entity.po.ProductParams;
import com.dafang.monitor.nx.product.mapper.ProductMapper;
import com.dafang.monitor.nx.utils.CommonUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ClimateAndHumanComfort extends TemplateAbstract {

    @Autowired
    ProductMapper mapper;
    String[] Areas = {"7","8","9","10","11"};
    String[] Seasons = {"春季_舒适_较不舒适","夏季_舒适_较不舒适","秋季_舒适_不舒适","冬季_寒冷_极冷"};

    @Override
    protected void init(ProductParams params) {
        year = params.getYear();
        fileName = year + "年" + ProductEmun.getFileName(10);
        templateName = "气候与人体舒适度.ftl";
    }

    @Override
    protected Map<String, Object> getDatas(ProductParams params) {
        return contentData(handleData(params));
    }
    //数据处理
    public Map<String,Object> handleData(ProductParams params){
        params.setRemark("comfort");
        Map<String,Object> result = new HashMap<>();
        for (String s : Seasons) {//季节遍历
            List<Map<String,Object>> list = new ArrayList<>();
            season = s.split("_")[0];
            String[] slots = SeasonEnum.getSlotBySeason(season).get(0).split("_");
            params.setST(slots[0]);
            params.setET(slots[1]);
            baseData = mapper.periodComfort(params);
            currentList = baseData.stream().filter(x -> StringUtils.equals(x.getYear().toString(), year)).collect(Collectors.toList());
            for (ComfortLevelEnum value : ComfortLevelEnum.values()) {//舒适度遍历
                if (StringUtils.equals(value.getIndex(),"5")){
                    Map<String,Object> map = new HashMap<>();
                    String fell = value.getFell();//舒适程度
                    String desc = value.getDesc();//舒适度范围
                    String[] range = desc.split("_");
                    map.put("fell",fell);
                    map.put("desc",desc);
                    for (String area : Areas) {//区域遍历
                        List<String> stas = RegionStaEnum.getStas(area);
                        String regionName = RegionStaEnum.getDesc(area);
                        String stationNos = String.join(",", stas);//站点
                        long count = currentList.stream().filter(x -> StringUtils.contains(stationNos, x.getStationNo())
                                && Convert.toDouble(x.getVal()) >= Convert.toDouble(range[0]) && Convert.toDouble(x.getVal()) <= Convert.toDouble(range[1])).count();
                        map.put(regionName,count);
                    }
                    list.add(map);
                }
            }
            result.put(season,list);
        }
        return result;
    }


    public Map<String,Object> contentData(Map<String,Object> map){
        Map<String,Object> result = new HashMap<>();
        //文字
        int index = 0;
        for (String s : Seasons) {
            index++;
            String[] sea = s.split("_");
            season = sea[0];
            String comfort1 = sea[1];
            String comfort2 = sea[2];
            List<Map<String,Object>> list = (List<Map<String,Object>>)map.get(season);
            String str = "%s。各市%s天数在%s～%s天之间，%s最多，为%s天，%s最少，为%s天；%s天数在%s～%s天之间，%s最多，为%s天，%s最少，为%s天。";
            Map<String, Object> content1 = getContent(list, comfort1);
            Map<String, Object> content2 = getContent(list, comfort2);
            str = String.format(str,season,comfort1,content1.get("min"),content1.get("max"),content1.get("maxRegion"),content1.get("max"),content1.get("minRegion"),content1.get("min"),
                    comfort2,content2.get("min"),content2.get("max"),content2.get("maxRegion"),content2.get("max"),content2.get("minRegion"),content2.get("min"));
            result.put("comfort"+index,str);//文字
            result.put("table"+index,(List<Map<String,Object>>)map.get(season));//表格
        }
        result.put("year",year);
        return result;
    }

    /**
     * 内容数据
     * @param list
     * @param comfort
     * @return
     */
    public Map<String,Object> getContent(List<Map<String,Object>> list,String comfort){
        Map<String,Object> result = new HashMap<>();
        Map<String, Object> fell = list.stream().filter(x -> StringUtils.contains(x.get("fell").toString(), comfort)).collect(Collectors.toList()).get(0);
        double max = -999,min = 999; String maxRegion = "",minRegion = "";
        for (Map.Entry<String, Object> entry : fell.entrySet()) {
            boolean integer = CommonUtils.isInteger(entry.getValue().toString());//判断是否为数字
            if (integer) {
                if (max <= Convert.toDouble(entry.getValue())){
                    max = Convert.toDouble(entry.getValue());
                    maxRegion = entry.getKey();
                }
                if (min >= Convert.toDouble(entry.getValue())){
                    min = Convert.toDouble(entry.getValue());
                    minRegion = entry.getKey();
                }
            }
        }
        result.put("min",min);
        result.put("max",max);
        result.put("minRegion",minRegion);
        result.put("maxRegion",maxRegion);
        return result;
    }


}
