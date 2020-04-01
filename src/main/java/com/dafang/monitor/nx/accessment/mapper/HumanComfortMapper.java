package com.dafang.monitor.nx.accessment.mapper;

import com.dafang.monitor.nx.accessment.entity.po.Comfort;
import com.dafang.monitor.nx.accessment.entity.po.ComfortParam;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;
import tk.mybatis.MyMapper;

import java.util.List;

@Mapper
@Repository
public interface HumanComfortMapper extends MyMapper<Comfort> {

    /*
    查询连续舒适度指数
     */
    List<Comfort> findContinueComfortValue(ComfortParam comfortParam);
    /*
    查询同期舒适度指数
     */
    List<Comfort> findPeriodComfortValue(ComfortParam comfortParam);
}
