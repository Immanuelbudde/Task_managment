package com.jobtracker.servlet;

import com.jobtracker.dao.ProjectDAO;
import com.jobtracker.dao.TaskDAO;
import com.jobtracker.model.Project;
import com.jobtracker.model.Task;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Date;

@WebServlet({"/create-project", "/create-task", "/update-task-status", "/delete-task"})
public class ActionServlet extends HttpServlet {

    private final ProjectDAO projectDAO = new ProjectDAO();
    private final TaskDAO taskDAO = new TaskDAO();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getServletPath();
        HttpSession session = req.getSession(false);
        
        if (session == null || session.getAttribute("userId") == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        int userId = (int) session.getAttribute("userId");
        String role = (String) session.getAttribute("userRole");

        try {
            if ("/create-project".equals(path)) {
                if (!"ADMIN".equals(role)) { resp.sendError(403); return; }
                Project p = new Project();
                p.setName(req.getParameter("name"));
                p.setDescription(req.getParameter("description"));
                p.setCreatedBy(userId);
                projectDAO.createProject(p);
            } 
            else if ("/create-task".equals(path)) {
                if (!"ADMIN".equals(role)) { resp.sendError(403); return; }
                Task t = new Task();
                t.setProjectId(Integer.parseInt(req.getParameter("projectId")));
                t.setAssignedTo(Integer.parseInt(req.getParameter("assignedTo")));
                t.setTitle(req.getParameter("title"));
                t.setDescription(req.getParameter("description"));
                t.setDueDate(Date.valueOf(req.getParameter("dueDate")));
                t.setStatus("Pending");
                t.setPriority(req.getParameter("priority"));
                taskDAO.createTask(t);
            }
            else if ("/update-task-status".equals(path)) {
                int taskId = Integer.parseInt(req.getParameter("taskId"));
                String status = req.getParameter("status");
                taskDAO.updateStatus(taskId, status);
            }
            else if ("/delete-task".equals(path)) {
                if (!"ADMIN".equals(role)) { resp.sendError(403); return; }
                int taskId = Integer.parseInt(req.getParameter("taskId"));
                taskDAO.deleteTask(taskId);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        resp.sendRedirect(req.getContextPath() + "/dashboard");
    }
}
