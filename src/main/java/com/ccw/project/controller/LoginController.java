package com.ccw.project.controller;


import com.alibaba.fastjson.JSONObject;
import com.ccw.project.entities.Questions;
import com.ccw.project.entities.User;
import com.ccw.project.service.LoginService;
import com.ccw.project.service.MainService;
import com.ccw.project.untils.PBKDF2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.HashMap;
import java.util.List;
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

    @RequestMapping("/ccw/login/forgetpaswordpage")
    public String forgetPassPage(Model model){
        model.addAttribute("user", new User());
        return "forgetpasword";
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

        String pbkdf2Pass = null;
        String salt = null;
        try {
            // Get the salt
            salt = PBKDF2.getSalt();

            pbkdf2Pass = PBKDF2.getPBKDF2(user.getPassword(), salt);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        }

        user.setPassword(pbkdf2Pass);
        user.setSalt(salt);

        int result = loginService.addUser(user);

        return "login";
    }

    @PostMapping("/ccw/login/forgetpass")
    public String forgetPass(User user){

        String salt = loginService.getSalt(user.getUsername());

        String pbkdf2Pass = null;
        try {
            pbkdf2Pass = PBKDF2.getPBKDF2(user.getPassword(), salt);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        }

        // Update the new password
        loginService.updatePassword(user.getUsername(), pbkdf2Pass);

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

    @RequestMapping("/ccw/login/checksequs1")
    @ResponseBody
    public Map<String,Object> checkSequs1(@RequestParam("username") String username, @RequestParam("sequs1") String sequs1){

        Map<String,Object> map = new HashMap<>();

        boolean flag = loginService.checkSequs1(username, sequs1);

        if(flag){
            map.put("errMeg","0");
        }else {
            map.put("errMeg","1");
        }
        return map;
    }

    @RequestMapping("/ccw/login/checksequs2")
    @ResponseBody
    public Map<String,Object> checkSequs2(@RequestParam("username") String username, @RequestParam("sequs2") String sequs2){
        Map<String,Object> map = new HashMap<>();

        boolean flag = loginService.checkSequs2(username, sequs2);

        if(flag){
            map.put("errMeg","0");
        }else {
            map.put("errMeg","1");
        }
        return map;
    }

    @RequestMapping("/ccw/login/checkpassword")
    @ResponseBody
    public Map<String,Object> checkPassword(@RequestParam("username") String username, @RequestParam("password") String password){
        Map<String,Object> map = new HashMap<>();

        String salt = loginService.getSalt(username);

        String checkPass = null;

        try {
            checkPass = PBKDF2.getPBKDF2(password, salt);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        }

        boolean flag = loginService.checkPassword(username, checkPass);

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
//            session.setAttribute("questions", mainService.getQuestions());
            // Get the new question list
            List<Questions> returnQuestions = mainService.getQuestions();

            List<Questions> newList = mainService.getAfPinQuestions(returnQuestions, ((User)session.getAttribute("User")).getId());
            session.setAttribute("questions", newList);
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
//        session.setAttribute("questions", mainService.getQuestions());
        // Get the new question list
        List<Questions> returnQuestions = mainService.getQuestions();

        List<Questions> newList = mainService.getAfPinQuestions(returnQuestions, userAll.getId());
        session.setAttribute("questions", newList);
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
