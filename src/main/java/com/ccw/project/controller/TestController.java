package com.ccw.project.controller;

import com.ccw.project.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

@Controller
public class TestController {

    @Autowired
    private TestService testService;

    @RequestMapping("/hello")
    public String hello(){
        return "boot";
    }

    @RequestMapping("/test/{userId}")
    @ResponseBody
    public Map<String,Object> test(@PathVariable("userId") int id){
        Map<String,Object> map = new HashMap<>();
        map.put("result",testService.getInfo(id));
//        map.put("result1","2");
        return map;
    }
}
