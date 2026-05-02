package com.taskmanager.servlet;

import com.taskmanager.dao.ProjectDAO;
import com.taskmanager.dao.TaskDAO;
import com.taskmanager.model.Project;
import com.taskmanager.model.Task;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@WebServlet("/dashboard")
public class DashboardServlet extends HttpServlet {

    private final TaskDAO taskDAO = new TaskDAO();
    private final ProjectDAO projectDAO = new ProjectDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        int userId = (int) session.getAttribute("userId");
        String role = (String) session.getAttribute("userRole");

        // Fetch tasks based on role
        List<Task> tasks;
        if ("ADMIN".equals(role)) {
            tasks = taskDAO.getAllTasks();
        } else {
            tasks = taskDAO.getTasksByUser(userId);
        }

        // Fetch stat counts for dashboard cards
        Map<String, Integer> statusCounts = taskDAO.getStatusCounts(userId, role);
        int totalCount = tasks.size();

        // Admin also needs project list to assign tasks
        if ("ADMIN".equals(role)) {
            List<Project> projects = projectDAO.getAllProjects();
            req.setAttribute("projects", projects);
        }

        req.setAttribute("tasks", tasks);
        req.setAttribute("statusCounts", statusCounts);
        req.setAttribute("totalCount", totalCount);

        req.getRequestDispatcher("/jsp/dashboard.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        doGet(req, resp);
    }
}
