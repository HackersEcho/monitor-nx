package com.dafang.monitor.nx.drought.mapper;

import com.dafang.monitor.nx.drought.entity.po.Drought;
import com.dafang.monitor.nx.drought.entity.po.DroughtParam;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface DroughtMapper{

    /*
    获取连续相对湿润度相关数据（总蒸散量,总降水量）
     */
    List<Drought> getContinue(DroughtParam params);
    /*
    获取连续相对湿润度相关数据（总蒸散量，总降水量）
     */
    List<Drought> getPeriod(DroughtParam params);

    /*
    连续单要素
     */
    List<Drought> getContinueData(DroughtParam params);
    /*
    同期单要素
     */
    List<Drought> getPeriodData(DroughtParam params);

    /*
    常年值
     */
    List<Drought> getPerenVal(DroughtParam params);
}
