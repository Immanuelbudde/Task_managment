<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Register — Job Tracker</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body class="auth-page">

<div class="orb orb-1"></div>
<div class="orb orb-2"></div>
<div class="orb orb-3"></div>

<div class="auth-box" style="max-width:480px;">
    <div class="auth-card">

        <div class="auth-logo"><i class="bi bi-briefcase-fill" style="color:#fff;"></i></div>
        <h1 class="auth-title">Create account</h1>
        <p class="auth-sub">Start tracking your job applications today</p>

        <% if (request.getAttribute("error") != null) { %>
        <div class="alert-m alert-m-danger">
            <i class="bi bi-exclamation-circle-fill"></i> ${error}
        </div>
        <% } %>

        <form action="${pageContext.request.contextPath}/register" method="post" id="registerForm" novalidate>

            <label class="f-label" for="name">Full Name</label>
            <div class="f-wrap">
                <i class="bi bi-person f-icon"></i>
                <input type="text" class="f-input" id="name" name="name" placeholder="Jane Doe" required autofocus>
            </div>

            <label class="f-label" for="email">Email address</label>
            <div class="f-wrap">
                <i class="bi bi-envelope f-icon"></i>
                <input type="email" class="f-input" id="email" name="email" placeholder="you@example.com" required>
            </div>

            <label class="f-label" for="password">Password</label>
            <div class="f-wrap">
                <i class="bi bi-lock f-icon"></i>
                <input type="password" class="f-input" id="password" name="password" placeholder="Minimum 6 characters" required minlength="6">
            </div>

            <label class="f-label" for="confirm">Confirm Password</label>
            <div class="f-wrap">
                <i class="bi bi-lock-fill f-icon"></i>
                <input type="password" class="f-input" id="confirm" name="confirm" placeholder="Repeat your password" required>
            </div>

            <label class="f-label" for="role">Your Role</label>
            <div class="f-wrap" style="margin-bottom:1.5rem;">
                <i class="bi bi-shield-lock f-icon"></i>
                <select class="f-input" id="role" name="role" style="appearance:auto; padding-left:2.6rem;">
                    <option value="MEMBER">Team Member</option>
                    <option value="ADMIN">Project Admin</option>
                </select>
            </div>

            <button type="submit" class="btn-primary">
                <i class="bi bi-person-plus"></i> Create Account
            </button>
        </form>

        <p class="auth-footer">
            Already have an account? <a href="${pageContext.request.contextPath}/login">Sign in</a>
        </p>
    </div>
</div>

<script>
    document.getElementById('registerForm').addEventListener('submit', function(e) {
        const name    = document.getElementById('name').value.trim();
        const email   = document.getElementById('email').value.trim();
        const pass    = document.getElementById('password').value.trim();
        const confirm = document.getElementById('confirm').value.trim();
        if (!name || !email || !pass || !confirm) { e.preventDefault(); alert('All fields are required.'); return; }
        if (pass.length < 6) { e.preventDefault(); alert('Password must be at least 6 characters.'); return; }
        if (pass !== confirm) { e.preventDefault(); alert('Passwords do not match.'); }
    });
</script>
</body>
</html>
