/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.muic.ooc.webapp.service;

import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang.StringUtils;

/**
 *
 * @author gigadot
 */
public class SecurityService {
    
    private Map<String, String> userCredentials = new HashMap<String, String>() {{
        put("admin", "123456");
        put("muic", "1111");
    }};

    public void addUser(String username, String password) {
        userCredentials.put(username, password);
    }

    public void removeUser(String username) {
        userCredentials.remove(username);
    }

    public Map<String, String> getUserTable() {
        return userCredentials;
    }

    public String showUserTable(){
        // for testing only
        StringBuilder sb = new StringBuilder();
        sb.append( "<table style=\"width:40%\">" );
        sb.append("<tr><th>Username</th>");
        for (Map.Entry<String, String> entry : userCredentials.entrySet()){
            sb.append("<tr><td>" + entry.getKey() + "</td><td><form action=\"/remove-user\" method=\"get\"><input type=\"submit\" name=\"" + entry.getKey() + "\" value=\"Remove\"> </form></td></tr>");
        }
        sb.append("</table>");
        return sb.toString();
    }
    
    public boolean isAuthorized(HttpServletRequest request) {
        String username = (String) request.getSession()
                .getAttribute("username");
        // do checking
       return (username != null && userCredentials.containsKey(username));
    }
    
    public boolean authenticate(String username, String password, HttpServletRequest request) {
        String passwordInDB = userCredentials.get(username);
        boolean isMatched = StringUtils.equals(password, passwordInDB);
        if (isMatched) {
            request.getSession().setAttribute("username", username);
            return true;
        } else {
            return false;
        }
    }
    
    public void logout(HttpServletRequest request) {
        request.getSession().invalidate();
    }
    
}
