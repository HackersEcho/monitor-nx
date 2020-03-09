package tk.mybatis;

import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

/**
 * 自己的 Mapper
 * 特别注意，该接口不能被扫描到，否则会出错
 * <p>Title: MyMapper</p>
 * <p>Description: </p>
 * @author echo
 * @version 1.0
 * @date 2020/3/1
 */
public interface MyMapper<T> extends Mapper<T>, MySqlMapper<T> {
}