package com.taskmanager.servlet;

import com.taskmanager.dao.ApplicationDAO;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/update-status")
public class UpdateStatusServlet extends HttpServlet {

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
        String status   = req.getParameter("status");

        if (appIdStr == null || appIdStr.trim().isEmpty() || status == null || status.trim().isEmpty()) {
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

        applicationDAO.updateStatus(appId, status.trim(), userId);

        // Preserve any active filter/search so dashboard shows the same view
        String statusFilter = req.getParameter("statusFilter");
        String searchQuery  = req.getParameter("searchQuery");

        StringBuilder redirect = new StringBuilder(req.getContextPath() + "/dashboard?updated=true");
        if (statusFilter != null && !statusFilter.isEmpty()) {
            redirect.append("&status=").append(statusFilter);
        }
        if (searchQuery != null && !searchQuery.isEmpty()) {
            redirect.append("&search=").append(searchQuery);
        }
        resp.sendRedirect(redirect.toString());
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
