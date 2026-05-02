<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Sign In — Team Task Manager</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body class="auth-page">

<div class="orb orb-1"></div>
<div class="orb orb-2"></div>
<div class="orb orb-3"></div>

<div class="auth-box">
    <div class="auth-card">

        <div class="auth-logo"><i class="bi bi-kanban-fill" style="color:#fff;"></i></div>
        <h1 class="auth-title">Team Task Manager</h1>
        <p class="auth-sub">Sign in to manage your projects & tasks</p>

        <% if (request.getAttribute("success") != null) { %>
        <div class="alert-m alert-m-success">
            <i class="bi bi-check-circle-fill"></i> ${success}
        </div>
        <% } %>
        <% if (request.getAttribute("error") != null) { %>
        <div class="alert-m alert-m-danger">
            <i class="bi bi-exclamation-circle-fill"></i> ${error}
        </div>
        <% } %>

        <form action="${pageContext.request.contextPath}/login" method="post" id="loginForm" novalidate>
            <label class="f-label" for="email">Email address</label>
            <div class="f-wrap">
                <i class="bi bi-envelope f-icon"></i>
                <input type="email" class="f-input" id="email" name="email" placeholder="you@example.com" required autofocus>
            </div>

            <label class="f-label" for="password">Password</label>
            <div class="f-wrap" style="margin-bottom:1.5rem;">
                <i class="bi bi-lock f-icon"></i>
                <input type="password" class="f-input" id="password" name="password" placeholder="••••••••" required>
            </div>

            <button type="submit" class="btn-primary">
                <i class="bi bi-box-arrow-in-right"></i> Sign In
            </button>
        </form>

        <p class="auth-footer">
            Don't have an account? <a href="${pageContext.request.contextPath}/register">Create one</a>
        </p>
    </div>
</div>

<script>
    document.getElementById('loginForm').addEventListener('submit', function(e) {
        const email = document.getElementById('email').value.trim();
        const pass  = document.getElementById('password').value.trim();
        if (!email || !pass) { e.preventDefault(); alert('Please fill in all fields.'); }
    });
</script>
</body>
</html>
