package com.dafang.monitor.nx.event.mapper;

import com.dafang.monitor.nx.event.entity.po.User;
import org.apache.ibatis.annotations.Mapper;
import tk.mybatis.MyMapper;

import java.util.List;
@Mapper
public interface UserMapper extends MyMapper<User> {

    public void insertUser(User user);

    public void updateUser(User user);

    public void deleteUser(User user);

    public User selectUserById(int id);

    public List<User> selectUserList();

}
