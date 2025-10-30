# 📚 Book Hive - Library Management System

A modern **Library Management Application** built to streamline library operations and provide a seamless experience for users and admins.  

---

##  About

**Book Hive** is a full-featured library management system that allows users to reserve books, read e-books, and enables admins to manage the library efficiently. The system comes with a **dashboard for admins**, detailed reporting, automated fine calculation, **role-based access control (RBAC)**, and **email notifications**.  

This project is built using a **modern tech stack**:  
- **Backend:** Java, Spring Boot, REST APIs ,Hibernate ORM 
- **Frontend:** React, CSS, HTML, JavaScript  
- **Database:** MySQL  
- **Build Tool:** Maven  
- **Authentication & Security:** JWT-based authentication with **RBAC**  
- **Notifications:** Email notifications for due dates, account recovery, and registration status  

---

##  Features

### User Features
- Reserve books for **24 hours**.  
- Read e-books directly through the platform.  
- Track borrowed books and return deadlines.  
- **Request membership**, which can be approved or rejected by the admin.  
- **User pagination** for browsing members or books efficiently.  
- Receive **email notifications** for:  
  - Due dates  
  - Account recovery  
  - Registration approval/rejection  

### Admin Features
- Approve or reject **member registration requests**.  
- Add **books, authors, and publishers**.  
- Return books by **record ID** and calculate fines automatically.  
- Generate **all library reports**.  
- **Dashboard** with detailed statistics:  
  - Total active members  
  - Total books  
  - Discarded books  
  - Other library metrics  

### Security Features
- **RBAC** ensures users and admins have **role-specific access**.  
- JWT authentication for secure login and API access.  

---

##  Tech Stack

| Frontend       | Backend       | Database   | Authentication & Security | Notifications | Others        |
|----------------|---------------|------------|---------------------------|---------------|---------------|
| React          | Spring Boot   | MySQL      | JWT + RBAC                | Email         | REST APIs     |
| HTML / CSS /JS | Java          |            |                           | SMTP          | Maven         |

---
