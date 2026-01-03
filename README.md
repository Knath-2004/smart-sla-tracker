# Smart SLA Tracker

Smart SLA Tracker is a role-based ticket management and SLA monitoring system built using Spring Boot.  
The application helps organizations efficiently track support tickets, monitor SLA compliance, manage escalations, and analyze resolution performance through an admin dashboard.

---

## 🚀 Features

- User ticket creation and tracking
- Role-based access control (User / Admin)
- SLA monitoring and breach detection
- Ticket escalation management
- Centralized admin dashboard
- Ticket lifecycle tracking (Open, In Progress, Resolved, Escalated)
- Secure authentication and authorization
- RESTful API architecture

---

## 🛠️ Tech Stack

- **Backend:** Java, Spring Boot
- **Frameworks:** Spring MVC, Spring Data JPA, Spring Security
- **Database:** MySQL / PostgreSQL
- **Build Tool:** Maven
- **API Style:** RESTful APIs
- **Version Control:** Git & GitHub

---

## 👥 User Roles

### 🔹 User
- Register and log in
- Raise support tickets
- View ticket status and updates
- Track SLA deadlines

### 🔹 Admin
- View all tickets
- Monitor SLA compliance
- Escalate overdue tickets
- Track resolved vs pending tickets
- Analyze ticket resolution performance

---

## 🧩 System Workflow

1. User registers and logs into the system
2. User raises a support ticket with issue details
3. Ticket is assigned SLA based on priority
4. Admin monitors tickets via dashboard
5. Tickets are resolved or escalated if SLA is breached
6. System maintains ticket history and status logs

---

## 📌 Future Enhancements

- JWT-based authentication
- Email notifications for SLA breaches
- Priority-based ticket assignment
- Analytics and reporting module
- Frontend integration using React or Angular

---

## 📂 Project Setup

1. Clone the repository
   ```bash
   git clone https://github.com/<your-username>/smart-sla-tracker.git
