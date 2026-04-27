# 🏥 Hospital Management System — Backend

Spring Boot REST API covering all 8 entities: Department, Doctor, Patient, Appointment, Admission, MedicalRecord, Medicine, Billing.

---

## Tech Stack

| Layer       | Technology                  |
|-------------|-----------------------------|
| Language    | Java 17                     |
| Framework   | Spring Boot 3.2             |
| ORM         | Spring Data JPA + Hibernate |
| Database    | MySQL 8.x                   |
| Validation  | Jakarta Bean Validation     |
| Boilerplate | Lombok                      |
| Build       | Maven                       |

---

## Project Structure

```
hospital-backend/
├── pom.xml
├── src/main/resources/application.properties
└── src/main/java/com/hospital/
    ├── HospitalApplication.java
    ├── config/        ApiResponse.java · CorsConfig.java
    ├── model/         Department · Doctor · Patient · Appointment
    │                  Admission · MedicalRecord · Medicine · Billing
    ├── dto/           {Entity}RequestDTO · {Entity}ResponseDTO  (16 files)
    ├── repository/    8 JPA repositories with custom JPQL queries
    ├── service/       8 service classes with business logic
    ├── controller/    8 REST controllers (45+ endpoints total)
    └── exception/     GlobalExceptionHandler · ApiError
                       ResourceNotFoundException · DuplicateResourceException · BadRequestException
```

---

## Setup

```bash
# 1. Load database (schema + sample data + triggers + views + procedures)
mysql -u root -p < database/hospital.sql

# 2. Edit src/main/resources/application.properties
spring.datasource.password=YOUR_PASSWORD_HERE

# 3. Run
cd hospital-backend && mvn spring-boot:run
# → http://localhost:8080
```

---

## API Response Format

```json
// Success
{ "success": true, "message": "...", "data": { ... } }

// Validation error
{ "status": 400, "error": "Validation Failed",
  "fieldErrors": { "phone": "Invalid number", "salary": "Min ₹30,000" } }

// Not found
{ "status": 404, "error": "Not Found", "message": "Patient not found with id: '9999'" }
```

---

## API Reference

### 🏢 `/api/departments`
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/departments` | Create department |
| GET | `/api/departments` | List all (with doctor count) |
| GET | `/api/departments/{id}` | Get by ID |
| PUT | `/api/departments/{id}` | Update |
| DELETE | `/api/departments/{id}` | Delete (blocked if doctors exist) |

### 🩺 `/api/doctors`
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/doctors` | Add doctor |
| GET | `/api/doctors` | List all |
| GET | `/api/doctors/{id}` | Get by ID |
| GET | `/api/doctors/search?name=` | Search by name |
| GET | `/api/doctors/department/{deptId}` | By department |
| GET | `/api/doctors/top-salary` | Highest-paid |
| PUT | `/api/doctors/{id}` | Update |
| DELETE | `/api/doctors/{id}` | Delete |

### 👤 `/api/patients`
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/patients` | Register patient |
| GET | `/api/patients` | List all |
| GET | `/api/patients/{id}` | Get by ID |
| GET | `/api/patients/phone/{phone}` | By phone |
| GET | `/api/patients/search?q=` | Search name/phone/email |
| GET | `/api/patients/pending-bills` | With pending bills |
| PUT | `/api/patients/{id}` | Update |
| DELETE | `/api/patients/{id}` | Delete |

### 🛏️ `/api/admissions`
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/admissions` | Admit patient |
| GET | `/api/admissions` | All admissions |
| GET | `/api/admissions/{id}` | Get by ID |
| GET | `/api/admissions/current` | Currently admitted |
| GET | `/api/admissions/patient/{id}` | Patient history |
| PATCH | `/api/admissions/{id}/discharge` | Discharge patient |

### 📅 `/api/appointments`
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/appointments` | Book appointment |
| GET | `/api/appointments` | All appointments |
| GET | `/api/appointments/{id}` | Get by ID |
| GET | `/api/appointments/today` | Today's schedule |
| GET | `/api/appointments/date?date=` | By date |
| GET | `/api/appointments/patient/{id}` | By patient |
| GET | `/api/appointments/doctor/{id}` | By doctor |
| PATCH | `/api/appointments/{id}/status?status=` | Update status |
| DELETE | `/api/appointments/{id}/cancel` | Cancel |

### 🗂️ `/api/medical-records`
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/medical-records` | Add record |
| GET | `/api/medical-records` | All records |
| GET | `/api/medical-records/{id}` | Get by ID |
| GET | `/api/medical-records/patient/{id}` | Patient history |
| GET | `/api/medical-records/admission/{id}` | By admission |
| PUT | `/api/medical-records/{id}` | Update |
| DELETE | `/api/medical-records/{id}` | Delete |

### 💊 `/api/medicines`
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/medicines` | Add medicine |
| GET | `/api/medicines` | List all |
| GET | `/api/medicines/{id}` | Get by ID |
| GET | `/api/medicines/search?q=` | Search |
| GET | `/api/medicines/low-stock` | Low / out of stock |
| GET | `/api/medicines/expiring-soon` | Expiring within 90 days |
| PUT | `/api/medicines/{id}` | Update |
| PATCH | `/api/medicines/{id}/restock?qty=` | Add stock |
| DELETE | `/api/medicines/{id}` | Delete |

### 💳 `/api/billing`
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/billing` | Generate bill (auto-computes total + status) |
| GET | `/api/billing` | All bills |
| GET | `/api/billing/{id}` | Get by ID |
| GET | `/api/billing/patient/{id}` | Patient's bills |
| GET | `/api/billing/pending` | Pending/partial bills |
| PATCH | `/api/billing/{id}/payment?amount=` | Record payment |

---

## Business Rules

| Rule | Where enforced |
|------|----------------|
| Doctor salary ≥ ₹30,000 | DTO `@Min` + DB CHECK + DB trigger |
| Patient phone unique | Service + DB UNIQUE |
| Gender = Male/Female/Other | DTO `@Pattern` + DB CHECK |
| No double-booking same doctor + time slot | Service + DB trigger |
| Paid amount ≤ total | Service + DB CHECK |
| Cannot delete patient with bills | DB trigger (SIGNAL) |
| Cannot delete dept with doctors | Service |
| Cannot re-admit an already-admitted patient | Service |
| Discharge date ≥ admit date | Service + DB CHECK |
| One bill per admission | DB UNIQUE on admission_id |
| Medicine stock ≥ 0 | DTO `@PositiveOrZero` + DB CHECK |
