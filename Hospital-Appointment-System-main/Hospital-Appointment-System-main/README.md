# 🏥 Hospital Management System (Spring Boot)

A full-stack Hospital Management System developed using Spring Boot, designed to efficiently manage interactions between patients, doctors, and administrators.

This system includes secure role-based authentication, smart appointment booking with conflict prevention, doctor approval workflow, and a complete prescription management system.

---

## 🚀 Features

### 🔐 Authentication & Authorization
- Secure login and registration system  
- Role-based access (Admin, Doctor, Patient)  
- Password encryption using BCrypt  
- Spring Security integration  
- Doctors cannot login until approved by admin  

---

### 🧑‍⚕️ Doctor Module
- Doctor registration with admin approval  
- Manage availability using day-wise time slots  
- View appointments  
- Approve / Reject appointments  
- Create & update prescriptions  
- View patient prescription history  

---

### 🧑 Patient Module
- Register and login  
- View approved doctors only  
- Book appointments using time slots  
- View appointment history  
- Track status (Pending / Approved / Rejected)  
- View prescription history  

---

### 👨‍💼 Admin Module
- Dashboard with system overview  
- Approve / Reject doctors  
- Manage doctors, patients, appointments  
- Delete users and records  

---

### 📅 Appointment System
- Slot-based booking system  
- Prevents double booking using:
  - Same time conflict (±4 minutes logic)  
  - Same patient booking multiple doctors at same time  
  - One doctor per day per patient  
- Uses pessimistic locking for concurrency safety  
- Appointment status: Pending → Approved / Rejected  

---

### 📝 Prescription Module
- One prescription per appointment  
- Doctors can add:
  - Diagnosis  
  - Medicines  
  - Instructions  
- Edit existing prescriptions  
- Patients can view full history  
- Secure access (only doctor & patient)  

---

### ⏰ Doctor Time Slot System
- Doctors define working slots (day + time)  
- Patients can book only within available slots  
- Prevents booking outside working hours  

---

## 🧰 Tech Stack

| Layer      | Technology            |
|------------|---------------------|
| Backend    | Spring Boot          |
| Security   | Spring Security      |
| Database   | MySQL (JPA/Hibernate)|
| Frontend   | Thymeleaf, HTML, CSS |
| Build Tool | Maven                |

---

## 📂 Project Structure

```
src/
 ├── controller/
 ├── service/
 ├── dao/
 ├── entity/
 ├── security/
 ├── templates/
 └── static/
```

---

## ⚙️ Setup & Installation

### 1️⃣ Clone the repository
```bash
git clone https://github.com/NAVEEN170804/Hospital-Appointment-System.git
```

### 2️⃣ Create Database
```sql
CREATE DATABASE hospital;
```

### 3️⃣ Configure application.properties
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/hospital
spring.datasource.username=root
spring.datasource.password=your_password
spring.jpa.hibernate.ddl-auto=update
```

### 4️⃣ Run the Project
```bash
mvn spring-boot:run
```

### 5️⃣ Open in Browser
```
http://localhost:8080/login
```

---

## 🔐 Default Admin Login

```
Email    : admin@medcare.com  
Password : admin123  
```

---

## 📸 Screenshots

### 🔐 Login Page
<img width="1920" height="1020" alt="Login_page" src="https://github.com/user-attachments/assets/964b099b-c62c-40ed-b08d-8ad7b6c94234" />

### 📝 Registration Page
<img width="1920" height="1020" alt="Register_Page" src="https://github.com/user-attachments/assets/f1878ad5-a4b0-473d-bd80-9206b41de002" />

### 👨‍💼 Admin Dashboard
<img width="1920" height="1020" alt="Admin-dashboard" src="https://github.com/user-attachments/assets/26a675cd-0ce8-405e-91db-83ec426e5083" />

### 📊 Admin View Appointments
<img width="1920" height="1020" alt="Admin-all-appointments" src="https://github.com/user-attachments/assets/590fa4e9-f1b0-4a20-b665-9edb2d198183" />

### 🧑‍⚕️ Doctor Dashboard
<img width="1920" height="1020" alt="Doctor-dashboard" src="https://github.com/user-attachments/assets/ea5c9acf-0a84-4427-9756-27814acb51a8" />

### 📝 Doctor Prescription
<img width="1920" height="1020" alt="Doctor-prescription-write" src="https://github.com/user-attachments/assets/d4a0d1a3-e4de-4ea6-9f13-858b29ee89ce" />

### ⏰ Doctor Time Slots
<img width="1920" height="1020" alt="Doctor-time-slots" src="https://github.com/user-attachments/assets/010866b9-38c0-433b-bb2f-46d0c6fcd60a" />

### 🧑 Patient Dashboard
<img width="1920" height="1020" alt="Patient-dashboard" src="https://github.com/user-attachments/assets/abb0f318-3b8b-4f19-b1e3-53e23bdce068" />

### 📅 Appointment Booking
<img width="1920" height="1020" alt="Patient-book-appointment" src="https://github.com/user-attachments/assets/1964c625-6fc2-43ed-ae31-cced0b2fd987" />

### 📝 Prescription History
<img width="1920" height="1020" alt="Patient-prescription-history" src="https://github.com/user-attachments/assets/afa86a91-0772-4c10-8329-e9a405bb65e3" />

---

## 🔥 Key Highlights

- Role-based secure authentication  
- Admin approval system for doctors  
- Smart appointment conflict prevention  
- Time-slot based scheduling  
- One prescription per appointment  
- Concurrency-safe booking using locking  
- Clean MVC architecture  

---

## 📌 Future Enhancements

- Email notifications  
- Online payment integration  
- QR code-based appointment system  
- Advanced analytics dashboard  

---

## 👨‍💻 Author

**Naveen A**  
📧 naveena170804@gmail.com  
🐙 https://github.com/NAVEEN170804  
🌐 https://naveen170804.github.io/Portfolio  

---

⭐ If you like this project, give it a star on GitHub!
