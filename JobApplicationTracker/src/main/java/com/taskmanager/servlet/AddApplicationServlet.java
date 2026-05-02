package com.taskmanager.servlet;

import com.taskmanager.dao.ApplicationDAO;
import com.taskmanager.model.Application;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Date;

@WebServlet("/add-application")
public class AddApplicationServlet extends HttpServlet {

    private final ApplicationDAO applicationDAO = new ApplicationDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }
        req.getRequestDispatcher("/jsp/add-application.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        int userId = (int) session.getAttribute("userId");

        String companyName   = req.getParameter("companyName")   != null ? req.getParameter("companyName").trim()   : "";
        String role          = req.getParameter("role")          != null ? req.getParameter("role").trim()          : "";
        String jobId         = req.getParameter("jobId")         != null ? req.getParameter("jobId").trim()         : "";
        String appliedDateStr= req.getParameter("appliedDate")   != null ? req.getParameter("appliedDate").trim()   : "";
        String status        = req.getParameter("status")        != null ? req.getParameter("status").trim()        : "Applied";
        String followUpStr   = req.getParameter("followUpDate")  != null ? req.getParameter("followUpDate").trim()  : "";
        String notes         = req.getParameter("notes")         != null ? req.getParameter("notes").trim()         : "";
        String jobUrl        = req.getParameter("jobUrl")        != null ? req.getParameter("jobUrl").trim()        : "";

        // Validation
        if (companyName.isEmpty() || role.isEmpty()) {
            req.setAttribute("error", "Company name and role are required.");
            req.getRequestDispatcher("/jsp/add-application.jsp").forward(req, resp);
            return;
        }

        Date appliedDate = null;
        if (!appliedDateStr.isEmpty()) {
            try {
                appliedDate = Date.valueOf(appliedDateStr);
            } catch (IllegalArgumentException e) {
                req.setAttribute("error", "Invalid applied date format. Use YYYY-MM-DD.");
                req.getRequestDispatcher("/jsp/add-application.jsp").forward(req, resp);
                return;
            }
        }

        Date followUpDate = null;
        if (!followUpStr.isEmpty()) {
            try {
                followUpDate = Date.valueOf(followUpStr);
            } catch (IllegalArgumentException e) {
                req.setAttribute("error", "Invalid follow-up date format. Use YYYY-MM-DD.");
                req.getRequestDispatcher("/jsp/add-application.jsp").forward(req, resp);
                return;
            }
        }

        Application app = new Application(
            userId,
            companyName,
            role,
            jobId.isEmpty()  ? null : jobId,
            appliedDate,
            status,
            followUpDate,
            notes.isEmpty()  ? null : notes,
            jobUrl.isEmpty() ? null : jobUrl
        );

        boolean success = applicationDAO.addApplication(app);
        if (success) {
            resp.sendRedirect(req.getContextPath() + "/dashboard?added=true");
        } else {
            req.setAttribute("error", "Failed to add application. Please try again.");
            req.getRequestDispatcher("/jsp/add-application.jsp").forward(req, resp);
        }
    }
}
