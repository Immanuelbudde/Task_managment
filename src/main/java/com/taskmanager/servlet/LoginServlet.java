package com.taskmanager.servlet;

import com.taskmanager.dao.UserDAO;
import com.taskmanager.model.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    private final UserDAO userDAO = new UserDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        // If already logged in redirect to dashboard
        HttpSession existing = req.getSession(false);
        if (existing != null && existing.getAttribute("user") != null) {
            resp.sendRedirect(req.getContextPath() + "/dashboard");
            return;
        }
        // Pass registration success message if redirected from register
        String registered = req.getParameter("registered");
        if ("true".equals(registered)) {
            req.setAttribute("success", "Account created! Please log in.");
        }
        req.getRequestDispatcher("/jsp/login.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String email    = req.getParameter("email")    != null ? req.getParameter("email").trim()    : "";
        String password = req.getParameter("password") != null ? req.getParameter("password").trim() : "";

        if (email.isEmpty() || password.isEmpty()) {
            req.setAttribute("error", "Email and password are required.");
            req.getRequestDispatcher("/jsp/login.jsp").forward(req, resp);
            return;
        }

        User user = userDAO.login(email, password);
        if (user == null) {
            req.setAttribute("error", "Invalid email or password.");
            req.getRequestDispatcher("/jsp/login.jsp").forward(req, resp);
            return;
        }

        // Create session and store user
        HttpSession session = req.getSession(true);
        session.setAttribute("user", user);
        session.setAttribute("userId", user.getId());
        session.setAttribute("userName", user.getName());
        session.setAttribute("userRole", user.getRole()); // Store role (ADMIN or MEMBER)
        session.setMaxInactiveInterval(30 * 60); // 30 minutes

        resp.sendRedirect(req.getContextPath() + "/dashboard");
    }
}
