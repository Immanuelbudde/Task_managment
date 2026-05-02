<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Add Application — Job Tracker</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body class="app-page">

<!-- NAVBAR -->
<nav class="nav-m">
    <a class="nav-brand" href="${pageContext.request.contextPath}/dashboard">
        <div class="nav-logo"><i class="bi bi-briefcase-fill" style="color:#fff;font-size:.9rem;"></i></div>
        <span class="nav-title">Job Tracker</span>
    </a>
    <div class="nav-right">
        <div class="nav-user">
            <div class="nav-avatar"><i class="bi bi-person"></i></div>
            <span>${sessionScope.userName}</span>
        </div>
        <a href="${pageContext.request.contextPath}/logout" class="btn-logout">
            <i class="bi bi-box-arrow-right"></i> Logout
        </a>
    </div>
</nav>

<div class="page-wrap" style="max-width:760px;">

    <div style="display:flex;align-items:center;gap:1rem;margin-bottom:1.75rem;">
        <a href="${pageContext.request.contextPath}/dashboard" class="btn-back">
            <i class="bi bi-arrow-left"></i> Back
        </a>
        <div>
            <h1 class="page-heading">Add New Application</h1>
            <p class="page-sub">Track a new job opportunity</p>
        </div>
    </div>

    <% if (request.getAttribute("error") != null) { %>
    <div class="alert-m alert-m-danger" style="margin-bottom:1.5rem;">
        <i class="bi bi-exclamation-circle-fill"></i> ${error}
    </div>
    <% } %>

    <div class="form-card">
        <form action="${pageContext.request.contextPath}/add-application" method="post" id="addForm" novalidate>

            <div class="form-section-title">Basic Information</div>

            <div style="display:grid;grid-template-columns:1fr 1fr;gap:1rem;margin-bottom:1rem;">
                <div>
                    <label class="f-label" for="companyName">Company Name <span class="req">*</span></label>
                    <input type="text" class="f-input-bare" id="companyName" name="companyName"
                           placeholder="e.g. Google" required>
                </div>
                <div>
                    <label class="f-label" for="role">Role / Position <span class="req">*</span></label>
                    <input type="text" class="f-input-bare" id="role" name="role"
                           placeholder="e.g. Software Engineer" required>
                </div>
                <div>
                    <label class="f-label" for="jobId">Job ID / Requisition</label>
                    <input type="text" class="f-input-bare" id="jobId" name="jobId"
                           placeholder="Optional reference ID">
                </div>
                <div>
                    <label class="f-label" for="appliedDate">Applied Date</label>
                    <input type="date" class="f-input-bare" id="appliedDate" name="appliedDate">
                </div>
            </div>

            <div class="form-section-title" style="margin-top:1.5rem;">Status & Follow-up</div>

            <div style="display:grid;grid-template-columns:1fr 1fr;gap:1rem;margin-bottom:1rem;">
                <div>
                    <label class="f-label" for="status">Status</label>
                    <select class="f-select" id="status" name="status">
                        <option value="Applied">Applied</option>
                        <option value="OA Received">OA Received</option>
                        <option value="Interview Scheduled">Interview Scheduled</option>
                        <option value="Rejected">Rejected</option>
                        <option value="Offer Received">Offer Received</option>
                        <option value="No Response">No Response</option>
                    </select>
                </div>
                <div>
                    <label class="f-label" for="followUpDate">Follow-up Date</label>
                    <input type="date" class="f-input-bare" id="followUpDate" name="followUpDate">
                </div>
            </div>

            <div class="form-section-title" style="margin-top:1.5rem;">Additional Details</div>

            <div style="margin-bottom:1rem;">
                <label class="f-label" for="jobUrl">Job URL</label>
                <input type="url" class="f-input-bare" id="jobUrl" name="jobUrl"
                       placeholder="https://company.com/careers/...">
            </div>

            <div style="margin-bottom:1.75rem;">
                <label class="f-label" for="notes">Notes</label>
                <textarea class="f-textarea" id="notes" name="notes" rows="4"
                          placeholder="Recruiter contact, interview details, salary info…"></textarea>
            </div>

            <div style="display:flex;gap:.75rem;flex-wrap:wrap;">
                <button type="submit" class="btn-submit">
                    <i class="bi bi-plus-circle-fill"></i> Add Application
                </button>
                <a href="${pageContext.request.contextPath}/dashboard" class="btn-cancel">
                    Cancel
                </a>
            </div>

        </form>
    </div>
</div>

<script>
    document.getElementById('addForm').addEventListener('submit', function(e) {
        const company = document.getElementById('companyName').value.trim();
        const role    = document.getElementById('role').value.trim();
        if (!company || !role) {
            e.preventDefault();
            alert('Company name and role are required fields.');
        }
    });
</script>
</body>
</html>
