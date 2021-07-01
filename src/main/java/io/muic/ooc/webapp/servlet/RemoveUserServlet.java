package io.muic.ooc.webapp.servlet;

import io.muic.ooc.webapp.Routable;
import io.muic.ooc.webapp.service.SecurityService;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

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
            for (String username: securityService.getUserTable().keySet()) {
                String button = request.getParameter(username);
                if (button != null) {
                    if (!username.equals(request.getSession().getAttribute("username"))) {
                        request.setAttribute("removeUser", username);
                        RequestDispatcher rd = request.getRequestDispatcher("WEB-INF/remove-user.jsp");
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
        } else {
            response.sendRedirect("/login");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("removeUser");
        String remove = request.getParameter("remove");
        if (remove != null) {
            securityService.removeUser(username);
            String message = "Successfully removed " + username;
            request.setAttribute("message", message);
            String userTable = securityService.showUserTable();
            request.setAttribute("userTable", userTable);
            RequestDispatcher rd = request.getRequestDispatcher("WEB-INF/home.jsp");
            rd.include(request, response);
        } else {
            response.sendRedirect("/");
        }
    }
}
