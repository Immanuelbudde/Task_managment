package com.jobtracker.servlet;

import com.jobtracker.dao.UserDAO;
import com.jobtracker.model.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {

    private final UserDAO userDAO = new UserDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        // If already logged in, redirect to dashboard
        if (req.getSession(false) != null && req.getSession(false).getAttribute("user") != null) {
            resp.sendRedirect(req.getContextPath() + "/dashboard");
            return;
        }
        req.getRequestDispatcher("/jsp/register.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String name     = req.getParameter("name")     != null ? req.getParameter("name").trim()     : "";
        String email    = req.getParameter("email")    != null ? req.getParameter("email").trim()    : "";
        String password = req.getParameter("password") != null ? req.getParameter("password").trim() : "";
        String confirm  = req.getParameter("confirm")  != null ? req.getParameter("confirm").trim()  : "";
        String role     = req.getParameter("role")     != null ? req.getParameter("role").trim()     : "MEMBER";

        // Validation
        if (name.isEmpty() || email.isEmpty() || password.isEmpty() || confirm.isEmpty()) {
            req.setAttribute("error", "All fields are required.");
            req.getRequestDispatcher("/jsp/register.jsp").forward(req, resp);
            return;
        }
        if (!password.equals(confirm)) {
            req.setAttribute("error", "Passwords do not match.");
            req.getRequestDispatcher("/jsp/register.jsp").forward(req, resp);
            return;
        }
        if (password.length() < 6) {
            req.setAttribute("error", "Password must be at least 6 characters.");
            req.getRequestDispatcher("/jsp/register.jsp").forward(req, resp);
            return;
        }
        if (userDAO.emailExists(email)) {
            req.setAttribute("error", "An account with that email already exists.");
            req.getRequestDispatcher("/jsp/register.jsp").forward(req, resp);
            return;
        }

        User user = new User(name, email, password, role);
        boolean success = userDAO.register(user);

        if (success) {
            resp.sendRedirect(req.getContextPath() + "/login?registered=true");
        } else {
            req.setAttribute("error", "Registration failed. Please try again.");
            req.getRequestDispatcher("/jsp/register.jsp").forward(req, resp);
        }
    }
}
