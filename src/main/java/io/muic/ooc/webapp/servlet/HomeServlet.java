/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.muic.ooc.webapp.servlet;

import io.muic.ooc.webapp.Routable;
import io.muic.ooc.webapp.service.SecurityService;

import java.io.IOException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class HomeServlet extends HttpServlet implements Routable {

    private SecurityService securityService;

    @Override
    public String getMapping() {
        return "/index.jsp";
    }

    @Override
    public void setSecurityService(SecurityService securityService) {
        this.securityService = securityService;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        boolean authorized = securityService.isAuthorized(request);
        if (authorized) {
            // do MVC in here
            String username = (String) request.getSession().getAttribute("username");
            request.setAttribute("username", username);
            String userTable = securityService.showUserTable();
            request.setAttribute("userTable", userTable);
            RequestDispatcher rd = request.getRequestDispatcher("WEB-INF/home.jsp");
            rd.include(request, response);
        } else {
            response.sendRedirect("/login");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String logout = request.getParameter("logout");
        if (logout != null) {
            securityService.logout(request);
            response.sendRedirect("/");
        } else {
            for (String username: securityService.getUserTable().keySet()) {
                String button = request.getParameter(username);
                if (button != null) {
                    if (!username.equals(request.getSession().getAttribute("username"))) {
                        securityService.removeUser(username);
                        String message = "Successfully removed user";
                        request.setAttribute("message", message);
                        String userTable = securityService.showUserTable();
                        request.setAttribute("userTable", userTable);
                        RequestDispatcher rd = request.getRequestDispatcher("WEB-INF/home.jsp");
                        rd.include(request, response);
                    } else {
                        String error = "Cannot remove your own account";
                        request.setAttribute("error", error);
                        String userTable = securityService.showUserTable();
                        request.setAttribute("userTable", userTable);
                        RequestDispatcher rd = request.getRequestDispatcher("WEB-INF/home.jsp");
                        rd.include(request, response);
                    }
                }
            }
        }
    }
}
