package com.jobtracker.servlet;

import com.jobtracker.dao.ApplicationDAO;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/delete-application")
public class DeleteApplicationServlet extends HttpServlet {

    private final ApplicationDAO applicationDAO = new ApplicationDAO();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        int userId = (int) session.getAttribute("userId");

        String appIdStr = req.getParameter("appId");
        if (appIdStr == null || appIdStr.trim().isEmpty()) {
            resp.sendRedirect(req.getContextPath() + "/dashboard?error=invalid");
            return;
        }

        int appId;
        try {
            appId = Integer.parseInt(appIdStr.trim());
        } catch (NumberFormatException e) {
            resp.sendRedirect(req.getContextPath() + "/dashboard?error=invalid");
            return;
        }

        applicationDAO.deleteApplication(appId, userId);
        resp.sendRedirect(req.getContextPath() + "/dashboard?deleted=true");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        // GET not supported; redirect to dashboard
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }
        resp.sendRedirect(req.getContextPath() + "/dashboard");
    }
}
