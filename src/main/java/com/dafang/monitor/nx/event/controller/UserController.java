package com.dafang.monitor.nx.event.controller;

import com.dafang.monitor.nx.event.entity.po.User;
import com.dafang.monitor.nx.event.service.UserService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
/**
 * @author echo
 * @Api：修饰整个类，描述Controller的作用
 * @ApiOperation：描述一个类的一个方法，或者说一个接口
 * @ApiParam：单个单数描述
 * @ApiModel：用对象来接收参数
 * @ApiProperty：用对象接收参数时，描述对象的一个字段
 * @ApiResponse：HTTP响应其中一个描述
 * @ApiResponses：HTTP响应整体描述
 * @ApiIgnore：使用该注解忽略这个API
 * @ApiError：发生错误返回的信息
 * @ApiImplicitParam：一个请求参数
 * @ApiImplicitParams：多个请求参数
 * @create 2020-03-02
 */
@Api(value = "用户类", tags = {"测试Controller访问接口"})
@RestController
public class UserController {

    @Autowired
    private UserService userService;
    @ApiOperation("通过id用户")
    @GetMapping("/findById/{id}")
    public User findById(@ApiParam("主键id") int id) {
        User user = userService.findById(id);
        return user;
    }

    @GetMapping("/findAll")
    public List<User> findAll() {
        List<User> userList = userService.findAll();
        return userList;
    }

    @PostMapping("/insert")
    public void insert() {
        User user = new User();
        user.setName("王五");
        user.setSex("男");
        user.setAge(18);
        user.setAddress("江西省");
        user.setPhone("456789645");
        userService.insert(user);
    }
//
//    @RequestMapping("/delete")
//    public void delete() {
//        User user = new User();
//        user.setId(5);
//        userService.delete(user);
//    }
//
//    @RequestMapping("/update")
//    public void update() {
//        User user = new User();
//        user.setId(5);
//        user.setName("李四");
//        userService.update(user);
//    }
    @ApiOperation("测试接口")
    @ApiResponses(value = {
            @ApiResponse(code = 1000, message = "成功"),
            @ApiResponse(code = 1001, message = "失败"),
            @ApiResponse(code = 1002, message = "缺少参数")})
    @RequestMapping(value = "/demo3", method = RequestMethod.POST)
    public String demo3(@ApiParam("电影名称") @RequestParam("filmName") String filmName,
                        @ApiParam(value = "分数", allowEmptyValue = true) @RequestParam("score") Short score,
                        @ApiParam("发布时间") @RequestParam(value = "publishTime", required = false) String publishTime,
                        @ApiParam("创建者id") @RequestParam("creatorId") Long creatorId) {
        return "ccc";
    }
}
