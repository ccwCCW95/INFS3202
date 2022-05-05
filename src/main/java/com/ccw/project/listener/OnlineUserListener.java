package com.ccw.project.listener;

import com.ccw.project.entities.User;
import com.ccw.project.system.OnlineControl;

import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSessionAttributeListener;
import javax.servlet.http.HttpSessionBindingEvent;


@WebListener
public class OnlineUserListener implements HttpSessionAttributeListener {

    public void attributeAdded(HttpSessionBindingEvent se) {
        // check if the modified attribute is loginuser
        if("loginuser".equals(se.getName())){
            OnlineControl.onlineUsers.add(((User)se.getValue()).getUsername());
        }


    }

    public void attributeRemoved(HttpSessionBindingEvent se) {
        // check if the modified attribute is loginuser
        if("loginuser".equals(se.getName())){
            OnlineControl.onlineUsers.remove(((User)se.getValue()).getUsername());
        }
    }

}

