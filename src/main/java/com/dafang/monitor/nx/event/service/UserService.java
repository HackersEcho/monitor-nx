package com.dafang.monitor.nx.event.service;

import com.dafang.monitor.nx.event.entity.po.User;

import java.util.List;

public interface UserService {

    public User findById(int id);

    public List<User> findAll();

    public void insert(User user);

    public void update(User user);

    public void delete(User user);

}
