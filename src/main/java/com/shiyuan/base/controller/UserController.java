package com.shiyuan.base.controller;

import com.shiyuan.base.entity.User;
import com.shiyuan.base.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping
    public String hello() {
        return "hello";
    }

    @GetMapping("/list")
    public ResponseEntity<Object> list() {
        try {
            List<User> listData = userService.list();
            System.out.println(listData);
            Map<String, Object> map = new HashMap<>(4);
            map.put("code", 200);
            map.put("data", listData);
            map.put("msg", "success");
            return new ResponseEntity<>(map, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
