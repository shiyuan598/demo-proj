package com.shiyuan.base.controller;

import com.shiyuan.base.entity.Load;
import com.shiyuan.base.service.LoadService;
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
@RequestMapping("/load")
public class LoadController {
    @Autowired
    private LoadService loadService;

    @GetMapping("/dict")
    public ResponseEntity<Object> dict() {
        try {
            List<Load> listData = loadService.list();
            System.out.println(listData);
            Map<String, Object> map = new HashMap<>(4);
            map.put("data", listData);
            map.put("code", 0);
            map.put("msg", "success");
            return new ResponseEntity<>(map, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
