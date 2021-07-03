package io.muic.ooc.webapp.servlet;

import io.muic.ooc.webapp.Routable;
import io.muic.ooc.webapp.service.SecurityService;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

public class RemoveUserServlet extends HttpServlet implements Routable {

    private SecurityService securityService;

    @Override
    public String getMapping() {
        return "/remove-user";
    }

    @Override
    public void setSecurityService(SecurityService securityService) {
        this.securityService = securityService;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        boolean authorized = securityService.isAuthorized(request);
        if (authorized) {
            String remove = request.getParameter("remove");
            if (remove != null) {
                String user = (String) request.getSession().getAttribute("username");
                String removeUser = request.getParameter("removeUser");
                if (!user.equals(removeUser)) {
                    request.setAttribute("removeUser", removeUser);
                    RequestDispatcher rd = request.getRequestDispatcher("WEB-INF/remove-user.jsp");
                    rd.include(request, response);
                } else {
                    String error = "Cannot remove your own account";
                    request.setAttribute("error", error);
                    String userTable = securityService.showUserTable();
                    request.setAttribute("userTable", userTable);
                    String userInfo = securityService.showUserInfo(user);
                    request.setAttribute("userInfo", userInfo);
                    RequestDispatcher rd = request.getRequestDispatcher("WEB-INF/home.jsp");
                    rd.include(request, response);
                }
            } else {
                response.sendRedirect("/");
            }
        } else {
            response.sendRedirect("/login");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String user = (String) request.getSession().getAttribute("username");
        String removeUser = request.getParameter("removeUser");
        String remove = request.getParameter("remove");
        if (remove != null) {
            try {
                securityService.removeUser(removeUser);
                String message = "Successfully removed " + removeUser;
                request.setAttribute("message", message);
            } catch (SQLException | ClassNotFoundException e) {
                e.printStackTrace();
                String error = "Failed to remove " + removeUser;
                request.setAttribute("error", error);
            }
            String userTable = securityService.showUserTable();
            request.setAttribute("userTable", userTable);
            String userInfo = securityService.showUserInfo(user);
            request.setAttribute("userInfo", userInfo);
            RequestDispatcher rd = request.getRequestDispatcher("WEB-INF/home.jsp");
            rd.include(request, response);
        } else {
            response.sendRedirect("/");
        }
    }
}
