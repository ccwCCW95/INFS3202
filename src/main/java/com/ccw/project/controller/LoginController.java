package com.ccw.project.controller;


import com.alibaba.fastjson.JSONObject;
import com.ccw.project.entities.User;
import com.ccw.project.service.LoginService;
import com.ccw.project.service.MainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.util.DigestUtils;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Controller
public class LoginController {

    @Autowired
    private LoginService loginService;

    @Autowired
    private MainService mainService;

    @Autowired
    private RestTemplate restTemplate;

    @RequestMapping("/ccw/login")
    public void index(HttpSession session, HttpServletRequest request, HttpServletResponse response){
        if (session.getAttribute("User") != null){
            try {
                response.sendRedirect(request.getContextPath() + "/ccw/login/submit");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else {
            try {
                response.sendRedirect(request.getContextPath() + "/ccw/login/loadview");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @RequestMapping("/ccw/login/loadview")
    public String loadView(Model model){
        model.addAttribute("user", new User());
        return "login";
    }

    @RequestMapping("/ccw/login/registration")
    public String regis(Model model){
        model.addAttribute("user", new User());
        return "signup";
    }

    @RequestMapping("/ccw/login/registration/checkusername")
    @ResponseBody
    public Map<String,Object> checkUserExist(@RequestParam("username") String username){
        Map<String,Object> map = new HashMap<>();

        boolean flag = loginService.checkUserExist(username);

        if(flag){
            map.put("errMeg","0");
        }else {
            map.put("errMeg","1");
        }
        return map;
    }

    @RequestMapping("/ccw/login/registration/checkemail")
    @ResponseBody
    public Map<String,Object> checkEmailExist(@RequestParam("email") String email){
        Map<String,Object> map = new HashMap<>();

        boolean flag = loginService.checkEmailExist(email);

        if(flag){
            map.put("errMeg","0");
        }else {
            map.put("errMeg","1");
        }
        return map;
    }

    @PostMapping("/ccw/login/regist")
    public String subRegis(User user){
        // Encrption for password
        String md5Pass = DigestUtils.md5DigestAsHex(user.getPassword().getBytes());
        user.setPassword(md5Pass);

        int result = loginService.addUser(user);

        return "login";
    }

    @RequestMapping("/ccw/login/checkusername")
    @ResponseBody
    public Map<String,Object> checkUser(@RequestParam("username") String username){
        Map<String,Object> map = new HashMap<>();

        boolean flag = loginService.checkUserExist(username);

        if(flag){
            map.put("errMeg","1");
        }else {
            map.put("errMeg","0");
        }
        return map;
    }

    @RequestMapping("/ccw/login/checkpassword")
    @ResponseBody
    public Map<String,Object> checkPassword(@RequestParam("username") String username, @RequestParam("password") String password){
        Map<String,Object> map = new HashMap<>();

        // Encrption for password
        String md5Pass = DigestUtils.md5DigestAsHex(password.getBytes());

        boolean flag = loginService.checkPassword(username, md5Pass);

        if(flag){
            map.put("errMeg","0");
        }else {
            map.put("errMeg","1");
        }
        return map;
    }

    @RequestMapping("/ccw/login/submit")
    public String subLogin(HttpServletRequest request, HttpServletResponse response, HttpSession session, User user, Model model){

        if (session.getAttribute("User") != null){
            session.setAttribute("questions", mainService.getQuestions());
            session.setAttribute("loginuser", session.getAttribute("User"));

            String url="https://global.juheapi.com/aqi/v1/city?q=brisbane&apikey=0F4u2KJ5uRr1AVuhDGk7ttLFujJQyYGr";
            ResponseEntity<String> results = restTemplate.exchange(url, HttpMethod.GET, null, String.class);
            String json = results.getBody();

            JSONObject trueJson = JSONObject.parseObject(json);
            JSONObject tempObject = (JSONObject) trueJson.get("data");
            String pm25 = (String) tempObject.get("pm25");

            session.setAttribute("pm25", pm25);

            return "main";
        }

        User userAll = loginService.getUserInfor(user.getUsername());

        session.setAttribute("User", userAll);
        session.setAttribute("questions", mainService.getQuestions());
        session.setAttribute("loginuser", userAll);

        String url="https://global.juheapi.com/aqi/v1/city?q=brisbane&apikey=0F4u2KJ5uRr1AVuhDGk7ttLFujJQyYGr";
        ResponseEntity<String> results = restTemplate.exchange(url, HttpMethod.GET, null, String.class);
        String json = results.getBody();

        JSONObject trueJson = JSONObject.parseObject(json);
        JSONObject tempObject = (JSONObject) trueJson.get("data");
        String pm25 = (String) tempObject.get("pm25");

        session.setAttribute("pm25", pm25);

        // check if cookies is needed
        if (user.getRememberme()) {
            Cookie cookie_username = new Cookie("user_name", userAll.getUsername());
            cookie_username.setMaxAge(30 * 24 * 60 * 60);
            cookie_username.setPath("/");
            response.addCookie(cookie_username);
        }


        return "main";
    }

    @RequestMapping("/ccw/login/signout")
    public void signOut(HttpServletRequest request, HttpServletResponse response, HttpSession session){
        session.removeAttribute("User");
        session.removeAttribute("questions");
        session.removeAttribute("loginuser");
        session.removeAttribute("pm25");

        Cookie cookie_username = new Cookie("user_name", "");
        cookie_username.setMaxAge(0);
        cookie_username.setPath("/");
        response.addCookie(cookie_username);

        try {
            response.sendRedirect(request.getContextPath() + "/ccw/login/loadview");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
