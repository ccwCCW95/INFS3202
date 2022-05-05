package com.ccw.project.interceptor;

import com.ccw.project.entities.User;
import com.ccw.project.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Component
public class LoginInterceptor implements HandlerInterceptor {

    @Autowired
    private LoginService loginService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // Get the cookie
        Cookie[] cookies = request.getCookies();

        if (cookies == null) {
            response.sendRedirect(request.getContextPath() + "/ccw/login/loadview");
            return false;
        }

        // Get the specific cookie
        String cookie_username = null;
        for (Cookie item : cookies) {
            if ("user_name".equals(item.getName())) {
                cookie_username = item.getValue();
                break;
            }
        }

        // check if login cookie is empty
        if ("".equals(cookie_username)) {
            response.sendRedirect(request.getContextPath() + "/ccw/login/loadview");
            return false;
        }

        // Store the information into session
        HttpSession session = request.getSession();
        // Get user information that exists in the session after we log in. If it is empty, the session has expired
        Object obj = session.getAttribute("User");

        // fix bug for no cookie and no session
        if (cookie_username == null && null == obj){
            response.sendRedirect(request.getContextPath() + "/ccw/login/loadview");
            return false;
        }

        if (null == obj) {
            // Get the user information and store it in the session
            User userAll = loginService.getUserInfor(cookie_username);

            session.setAttribute("User", userAll);

        }

        return true;
    }

}
