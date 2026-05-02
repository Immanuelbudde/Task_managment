# 🚀 Team Task Manager (Full-Stack)

A professional, high-performance web application built to manage team projects and track task progress with Role-Based Access Control (RBAC).

## 🌟 Key Features
- **Role-Based Access Control (RBAC):**
  - **Admin:** Can create projects, assign tasks to team members, and manage the global task list.
  - **Member:** Can view their assigned tasks and update progress status.
- **Dynamic Dashboard:** Real-time statistics for Total, Pending, In Progress, Completed, and Overdue tasks.
- **Premium UI/UX:** A modern, responsive "Lavender" themed interface built with Vanilla CSS and Bootstrap Icons.
- **Cloud-Ready:** Fully optimized for seamless deployment on Railway with dynamic environment variable support.

## 🛠️ Technology Stack
- **Backend:** Java 11 (Servlets, JSP, JSTL)
- **Database:** MySQL 8.0
- **Frontend:** HTML5, CSS3 (Custom Design), JavaScript
- **Build Tool:** Maven 3.9
- **Server:** Apache Tomcat 9
- **Deployment:** Railway

## 📂 Project Structure
```text
src/main/java/com/jobtracker/
├── model/       # Data Models (User, Project, Task)
├── dao/         # Data Access Objects (JDBC logic)
├── servlet/     # Controllers (Routing & Business Logic)
└── util/        # Helpers (DB Connection)
src/main/webapp/
├── jsp/         # View Pages (Login, Dashboard, Register)
├── css/         # Styling
└── WEB-INF/     # Configuration (web.xml)
```

## ⚙️ Local Setup Instructions

### 1. Database Setup
Execute the following SQL in your MySQL instance:
```sql
CREATE DATABASE task_manager;
USE task_manager;
-- [Refer to db-setup.sql in repository for full schema]
```

### 2. Build the Project
Navigate to the root directory and run:
```bash
mvn clean package
```

### 3. Deploy to Tomcat
Copy the generated `.war` file from the `target/` folder to your Tomcat `webapps/` directory.

## 🌐 Deployment (Railway)
This application is built to detect Railway environment variables automatically.
1. Push this code to a GitHub repository.
2. Connect the repository to **Railway.app**.
3. Add a **MySQL** database plugin in Railway.
4. The app will automatically connect to the cloud database using the `MYSQL_URL` variable.

## 👥 Demo Credentials
- **Admin:** `admin@task.com` / `admin123`
- **Member:** `member@task.com` / `member123`
