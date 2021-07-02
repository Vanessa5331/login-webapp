/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.muic.ooc.webapp.service;

import java.security.SecureRandom;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import javax.servlet.http.HttpServletRequest;

import at.favre.lib.crypto.bcrypt.BCrypt;

public class SecurityService {
    private Connection con;
    private PreparedStatement st;
    private ResultSet rs;

    public void addUser(String username, String password) throws SQLException, ClassNotFoundException {
        String hashedPassword = BCrypt.with(new SecureRandom()).hashToString(12, password.toCharArray());

        con = DBConnection.init();
        st = con.prepareStatement(String.format("insert into user(username, password) values('%s', '%s')", username, hashedPassword));
        st.executeUpdate();

        st.close();
        con.close();
    }

    public void removeUser(String username) throws SQLException, ClassNotFoundException {
        con = DBConnection.init();
        st = con.prepareStatement(String.format("delete from user where username='%s'", username));
        st.executeUpdate();

        st.close();
        con.close();
    }

    public void editUser(String column, String username, String info) throws SQLException, ClassNotFoundException {
        con = DBConnection.init();
        st = con.prepareStatement(String.format("update user set %s='%s' where username='%s'", column, info, username));
        st.executeUpdate();

        st.close();
        con.close();
    }

    public Set<String> getUsers() {
        Set<String> users = new HashSet<>();
        try {
            con = DBConnection.init();
            st = con.prepareStatement("select username from user");
            rs = st.executeQuery();

            while(rs.next()) {
                users.add(rs.getString("username"));
            }

            st.close();
            con.close();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return users;
    }

    public String showUserInfo(String username){
        StringBuilder sb = new StringBuilder();
        try {
            con = DBConnection.init();
            st = con.prepareStatement(String.format("select * from user where username='%s'", username));
            rs = st.executeQuery();
            rs.next();

            String firstname = rs.getString("firstname");
            if(firstname == null) {
                firstname = "-";
            }
            String lastname = rs.getString("lastname");
            if(lastname == null) {
                lastname = "-";
            }
            sb.append(String.format("Firstname: %s<br/>", firstname));
            sb.append(String.format("Lastname: %s<br/>", lastname));
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    public String showUserTable(){
        StringBuilder sb = new StringBuilder();
        for (String user : getUsers()){
            sb.append("<tr><td>" + user +
                    "</td><td style=\"padding-top:20px\"><form action=\"/remove-user\" method=\"get\">" +
                    "<input type=\"submit\" name=\"remove\" value=\"Remove\"" +
                    "<input type=\"hidden\" name=\"removeUser\" value=\"" + user + "\">"+
                    "</form></td></tr>");
        }
        return sb.toString();
    }
    
    public boolean isAuthorized(HttpServletRequest request) {
        String username = (String) request.getSession().getAttribute("username");
        return (username != null && getUsers().contains(username));
    }
    
    public boolean authenticate(String username, String password, HttpServletRequest request) {
        try {
            con = DBConnection.init();
            st = con.prepareStatement(String.format("select username, password from user where username='%s'", username));
            rs = st.executeQuery();
            rs.next();

            String hashedPassword = rs.getString("password");

            st.close();
            con.close();
            if (BCrypt.verifyer().verify(password.toCharArray(), hashedPassword).verified) {
                request.getSession().setAttribute("username", username);
                return true;
            } else {
                return false;
            }
        } catch (SQLException | ClassNotFoundException e) {
            return false;
        }
    }
    
    public void logout(HttpServletRequest request) {
        request.getSession().invalidate();
    }
    
}
