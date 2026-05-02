<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Team Dashboard — Task Manager</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    <style>
        .role-badge { font-size: 0.65rem; padding: 2px 6px; border-radius: 4px; background: rgba(150,138,220,0.2); color: var(--primary); font-weight: 800; vertical-align: middle; margin-left: 5px; }
        .priority-high { color: var(--red); }
        .priority-medium { color: var(--amber); }
        .priority-low { color: var(--green); }
    </style>
</head>
<body class="app-page">

<nav class="nav-m">
    <a class="nav-brand" href="${pageContext.request.contextPath}/dashboard">
        <div class="nav-logo"><i class="bi bi-kanban-fill" style="color:#fff;font-size:.9rem;"></i></div>
        <span class="nav-title">Task Manager</span>
    </a>
    <div class="nav-right">
        <div class="nav-user">
            <div class="nav-avatar">${fn:substring(sessionScope.userName,0,1)}</div>
            <span>${sessionScope.userName} <span class="role-badge">${sessionScope.userRole}</span></span>
        </div>
        <c:if test="${sessionScope.userRole eq 'ADMIN'}">
            <button onclick="document.getElementById('projectModal').style.display='flex'" class="btn-add" style="background: var(--bg2); color: var(--primary); border: 1px solid var(--border);">
                <i class="bi bi-folder-plus"></i> New Project
            </button>
            <button onclick="document.getElementById('taskModal').style.display='flex'" class="btn-add">
                <i class="bi bi-plus-lg"></i> Assign Task
            </button>
        </c:if>
        <a href="${pageContext.request.contextPath}/logout" class="btn-logout">
            <i class="bi bi-box-arrow-right"></i> Logout
        </a>
    </div>
</nav>

<div class="page-wrap">

    <!-- STAT CARDS -->
    <div class="stats-grid">
        <div class="stat-card s-total">
            <div class="stat-icon"><i class="bi bi-list-task"></i></div>
            <div class="stat-num">${totalCount}</div>
            <div class="stat-lbl">Total Tasks</div>
        </div>
        <div class="stat-card s-oa">
            <div class="stat-icon"><i class="bi bi-clock-history"></i></div>
            <div class="stat-num">${statusCounts['Pending'] != null ? statusCounts['Pending'] : 0}</div>
            <div class="stat-lbl">Pending</div>
        </div>
        <div class="stat-card s-applied">
            <div class="stat-icon"><i class="bi bi-play-circle"></i></div>
            <div class="stat-num">${statusCounts['In Progress'] != null ? statusCounts['In Progress'] : 0}</div>
            <div class="stat-lbl">In Progress</div>
        </div>
        <div class="stat-card s-interview">
            <div class="stat-icon"><i class="bi bi-check2-all"></i></div>
            <div class="stat-num">${statusCounts['Completed'] != null ? statusCounts['Completed'] : 0}</div>
            <div class="stat-lbl">Completed</div>
        </div>
        <div class="stat-card s-rejected">
            <div class="stat-icon"><i class="bi bi-exclamation-triangle"></i></div>
            <div class="stat-num">${statusCounts['Overdue'] != null ? statusCounts['Overdue'] : 0}</div>
            <div class="stat-lbl">Overdue</div>
        </div>
    </div>

    <!-- TASKS TABLE -->
    <div class="table-card">
        <div class="table-head">
            <div class="table-title">
                <i class="bi bi-grid-3x3-gap-fill"></i>
                ${sessionScope.userRole eq 'ADMIN' ? 'All Team Tasks' : 'My Assigned Tasks'}
            </div>
            <span class="count-badge">${fn:length(tasks)} task(s)</span>
        </div>

        <div style="overflow-x:auto;">
            <table class="tbl">
                <thead>
                    <tr>
                        <th>Task</th>
                        <th>Project</th>
                        <th>Assigned To</th>
                        <th>Due Date</th>
                        <th>Priority</th>
                        <th>Status</th>
                        <th>Actions</th>
                    </tr>
                </thead>
                <tbody>
                    <c:choose>
                        <c:when test="${empty tasks}">
                            <tr>
                                <td colspan="7">
                                    <div class="empty-state">
                                        <i class="bi bi-clipboard-x"></i>
                                        No tasks found.
                                    </div>
                                </td>
                            </tr>
                        </c:when>
                        <c:otherwise>
                            <c:forEach var="task" items="${tasks}">
                            <tr>
                                <td class="td-company">
                                    ${fn:escapeXml(task.title)}
                                    <div class="td-muted" style="font-weight:400; font-size: 0.75rem;">${fn:escapeXml(task.description)}</div>
                                </td>
                                <td><span class="badge b-oa" style="border-radius:6px;">${fn:escapeXml(task.projectName)}</span></td>
                                <td>${fn:escapeXml(task.assignedToName)}</td>
                                <td>
                                    <fmt:formatDate value="${task.dueDate}" pattern="MMM dd, yyyy" />
                                </td>
                                <td>
                                    <span class="priority-${fn:toLowerCase(task.priority)}" style="font-weight:700; font-size:0.8rem;">
                                        <i class="bi bi-circle-fill" style="font-size:0.5rem; margin-right:4px;"></i>${task.priority}
                                    </span>
                                </td>
                                <td>
                                    <c:choose>
                                        <c:when test="${task.status eq 'Pending'}"><span class="badge b-noresponse">${task.status}</span></c:when>
                                        <c:when test="${task.status eq 'In Progress'}"><span class="badge b-applied">${task.status}</span></c:when>
                                        <c:when test="${task.status eq 'Completed'}"><span class="badge b-interview">${task.status}</span></c:when>
                                        <c:when test="${task.status eq 'Overdue'}"><span class="badge b-rejected">${task.status}</span></c:when>
                                    </c:choose>
                                </td>
                                <td>
                                    <form action="${pageContext.request.contextPath}/update-task-status" method="post" style="display:inline-flex; gap:5px;">
                                        <input type="hidden" name="taskId" value="${task.id}">
                                        <select name="status" class="inline-select" style="min-width:110px;">
                                            <option value="Pending" ${task.status eq 'Pending' ? 'selected' : ''}>Pending</option>
                                            <option value="In Progress" ${task.status eq 'In Progress' ? 'selected' : ''}>In Progress</option>
                                            <option value="Completed" ${task.status eq 'Completed' ? 'selected' : ''}>Completed</option>
                                        </select>
                                        <button type="submit" class="btn-icon btn-icon-save"><i class="bi bi-check-lg"></i></button>
                                    </form>
                                    <c:if test="${sessionScope.userRole eq 'ADMIN'}">
                                        <form action="${pageContext.request.contextPath}/delete-task" method="post" style="display:inline;" onsubmit="return confirm('Delete this task?');">
                                            <input type="hidden" name="taskId" value="${task.id}">
                                            <button type="submit" class="btn-icon btn-icon-del"><i class="bi bi-trash"></i></button>
                                        </form>
                                    </c:if>
                                </td>
                            </tr>
                            </c:forEach>
                        </c:otherwise>
                    </c:choose>
                </tbody>
            </table>
        </div>
    </div>
</div>

<!-- MODALS -->
<div id="projectModal" class="modal-overlay" style="display:none; position:fixed; inset:0; background:rgba(0,0,0,0.5); z-index:2000; align-items:center; justify-content:center;">
    <div class="auth-card" style="width:100%; max-width:400px; animation:none;">
        <h2 class="auth-title" style="text-align:left;">New Project</h2>
        <form action="${pageContext.request.contextPath}/create-project" method="post">
            <label class="f-label">Project Name</label>
            <input type="text" name="name" class="f-input-bare" required style="margin-bottom:1rem;">
            <label class="f-label">Description</label>
            <textarea name="description" class="f-textarea" style="margin-bottom:1.5rem;"></textarea>
            <div style="display:flex; gap:10px;">
                <button type="submit" class="btn-primary">Create</button>
                <button type="button" onclick="this.closest('.modal-overlay').style.display='none'" class="btn-cancel">Cancel</button>
            </div>
        </form>
    </div>
</div>

<div id="taskModal" class="modal-overlay" style="display:none; position:fixed; inset:0; background:rgba(0,0,0,0.5); z-index:2000; align-items:center; justify-content:center;">
    <div class="auth-card" style="width:100%; max-width:500px; animation:none;">
        <h2 class="auth-title" style="text-align:left;">Assign New Task</h2>
        <form action="${pageContext.request.contextPath}/create-task" method="post">
            <div style="display:grid; grid-template-columns:1fr 1fr; gap:1rem; margin-bottom:1rem;">
                <div>
                    <label class="f-label">Project</label>
                    <select name="projectId" class="f-select" required>
                        <c:forEach var="p" items="${projects}">
                            <option value="${p.id}">${p.name}</option>
                        </c:forEach>
                    </select>
                </div>
                <div>
                    <label class="f-label">Assigned To (User ID)</label>
                    <input type="number" name="assignedTo" class="f-input-bare" placeholder="User ID" required>
                </div>
            </div>
            <label class="f-label">Task Title</label>
            <input type="text" name="title" class="f-input-bare" required style="margin-bottom:1rem;">
            <label class="f-label">Description</label>
            <textarea name="description" class="f-textarea" style="margin-bottom:1rem;"></textarea>
            <div style="display:grid; grid-template-columns:1fr 1fr; gap:1rem; margin-bottom:1.5rem;">
                <div>
                    <label class="f-label">Due Date</label>
                    <input type="date" name="dueDate" class="f-input-bare" required>
                </div>
                <div>
                    <label class="f-label">Priority</label>
                    <select name="priority" class="f-select">
                        <option value="Low">Low</option>
                        <option value="Medium" selected>Medium</option>
                        <option value="High">High</option>
                    </select>
                </div>
            </div>
            <div style="display:flex; gap:10px;">
                <button type="submit" class="btn-primary">Assign Task</button>
                <button type="button" onclick="this.closest('.modal-overlay').style.display='none'" class="btn-cancel">Cancel</button>
            </div>
        </form>
    </div>
</div>

</body>
</html>
