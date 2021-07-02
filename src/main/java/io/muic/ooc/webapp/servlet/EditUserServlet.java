package io.muic.ooc.webapp.servlet;

import io.muic.ooc.webapp.Routable;
import io.muic.ooc.webapp.service.SecurityService;
import org.apache.commons.lang.StringUtils;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

public class EditUserServlet extends HttpServlet implements Routable {

    private SecurityService securityService;

    @Override
    public String getMapping() {
        return "/edit-user";
    }

    @Override
    public void setSecurityService(SecurityService securityService) {
        this.securityService = securityService;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        boolean authorized = securityService.isAuthorized(request);
        if (authorized) {
            String username = (String) request.getSession().getAttribute("username");
            request.setAttribute("username", username);
            RequestDispatcher rd = request.getRequestDispatcher("WEB-INF/edit-user.jsp");
            rd.include(request, response);
        } else {
            response.sendRedirect("/login");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = (String) request.getSession().getAttribute("username");
        String newUsername = request.getParameter("new-username");
        String newFirstname = request.getParameter("new-firstname");
        String newLastname = request.getParameter("new-lastname");
        String newPassword = request.getParameter("new-password");

        if(StringUtils.isBlank(newUsername) && StringUtils.isBlank(newFirstname) && StringUtils.isBlank(newLastname) && StringUtils.isBlank(newPassword)) {
            String error = "No information is filled";
            request.setAttribute("error", error);
            RequestDispatcher rd = request.getRequestDispatcher("WEB-INF/edit-user.jsp");
            rd.include(request, response);
        } else if(securityService.getUsers().contains(newUsername)) {
            String error = "Username already exists";
            request.setAttribute("error", error);
            RequestDispatcher rd = request.getRequestDispatcher("WEB-INF/edit-user.jsp");
            rd.include(request, response);
        } else {
            try {
                if(!StringUtils.isBlank(newUsername)) {
                    securityService.editUser("username", username, newUsername);
                    username = newUsername;
                    request.setAttribute("username", username);
                }
                if(!StringUtils.isBlank(newFirstname)) {
                    securityService.editUser("firstname", username, newFirstname);
                }
                if(!StringUtils.isBlank(newLastname)) {
                    securityService.editUser("lastname", username, newLastname);
                }
                if(!StringUtils.isBlank(newPassword)) {
                    securityService.editUser("password", username, newPassword);
                }
                String message = "Successfully edited the user information";
                request.setAttribute("message", message);
            } catch (SQLException | ClassNotFoundException e) {
                String error = "Failed to edit the user information";
                request.setAttribute("error", error);
            }
            RequestDispatcher rd = request.getRequestDispatcher("WEB-INF/edit-user.jsp");
            rd.include(request, response);
        }
    }
}
