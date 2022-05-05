package com.ccw.project.system;


import java.util.HashSet;

/**
 * Store the information of online users
 */
public class OnlineControl {
    public static HashSet<String> onlineUsers = new HashSet<String>();

    public static int getActiveUserSum(){
        return onlineUsers.size();
    }

    public static HashSet<String> getActiveUsers(){
        return onlineUsers;
    }
}
