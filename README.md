# Medical Record

`Java 11` · `Spring Boot 2.3.4` · `React 17` · `MySQL 8` · `Docker` · `Material-UI` · `REST API`

Full-stack digital health records management system for patients and doctors. Enables GP registration, appointment tracking, diagnosis recording, prescription management, and sick leave documentation.

---

## Table of Contents

- [Overview](#overview)
- [Architecture](#architecture)
- [Tech Stack](#tech-stack)
- [Project Structure](#project-structure)
- [Data Model](#data-model)
- [API Reference](#api-reference)
- [Frontend](#frontend)
- [Tests](#tests)
- [Getting Started](#getting-started)
- [Configuration](#configuration)

---

## Overview

Medical Record is a web application for managing patient-doctor relationships and medical history. Key capabilities:

- Manage doctor profiles and specializations
- Record appointments with diagnosis, prescribed medicines, and sick leave dates
- View appointment history per patient and per doctor

---

## Architecture

```
┌─────────────────────┐        HTTP        ┌──────────────────────────┐
│   React Frontend    │ ◄────────────────► │  Spring Boot REST API    │
│   (port 3000)       │   localhost:8080   │  (port 8080)             │
└─────────────────────┘                    └────────────┬─────────────┘
                                                        │ JPA / Hibernate
                                                        ▼
                                            ┌──────────────────────────┐
                                            │      MySQL 8             │
                                            │   (port 3306)            │
                                            └──────────────────────────┘
```

**Backend layered architecture:**

```
Controller  →  Service (interface + impl)  →  DAO (JpaRepository)  →  MySQL
     ↕
   DTO  ←→  ModelMapper  ←→  Entity
```

---

## Tech Stack

### Backend
| Technology | Version | Purpose |
|---|---|---|
| Java | 11 | Language |
| Spring Boot | 2.3.4.RELEASE | Application framework |
| Spring Data JPA | managed | ORM and data access |
| Hibernate | managed | JPA implementation |
| MySQL Connector/J | managed | Database driver |
| Lombok | 1.18.36 | Boilerplate reduction |
| ModelMapper | 2.4.4 | Entity ↔ DTO mapping |
| Apache Commons Collections | 4.1 | Utilities |
| Spring Boot Validation | managed | Input validation |
| Jackson Databind | managed | JSON serialization |

### Frontend
| Technology | Version | Purpose |
|---|---|---|
| React | 17.0.1 | UI framework |
| React Router DOM | 5.2.0 | Client-side routing |
| Axios | 0.21.0 | HTTP client |
| Material-UI Core | 4.11.0 | UI component library |
| Material-UI Icons | 4.9.1 | Icon set |

### Infrastructure
| Technology | Purpose |
|---|---|
| Docker | Containerization |
| Docker Compose | Multi-container orchestration |
| MySQL 8 | Relational database |

---

## Project Structure

```
MedicalRecord/
├── MedicalRecord/                          # Spring Boot backend
│   ├── src/main/java/com/cscb869/MedicalRecord/
│   │   ├── controller/                     # REST controllers
│   │   │   ├── PatientController.java
│   │   │   ├── DoctorController.java
│   │   │   ├── SpecializationController.java
│   │   │   └── AppointmentController.java
│   │   ├── service/                        # Business logic interfaces + implementations
│   │   │   ├── PatientService.java / PatientServiceImpl.java
│   │   │   ├── DoctorService.java / DoctorServiceImpl.java
│   │   │   ├── SpecializationService.java / SpecializationServiceImpl.java
│   │   │   └── AppointmentService.java / AppointmentServiceImpl.java
│   │   ├── dao/                            # Spring Data JPA repositories
│   │   │   ├── PatientDAO.java
│   │   │   ├── DoctorDAO.java
│   │   │   ├── SpecializationDAO.java
│   │   │   └── AppointmentDAO.java
│   │   ├── model/                          # JPA entities
│   │   │   ├── Patient.java
│   │   │   ├── Doctor.java
│   │   │   ├── Specialization.java
│   │   │   └── Appointment.java
│   │   ├── dto/                            # Data Transfer Objects
│   │   │   ├── PatientDTO.java
│   │   │   ├── DoctorDTO.java
│   │   │   ├── SpecializationDTO.java
│   │   │   ├── AppointmentDTO.java
│   │   │   └── AppointmentRequest.java     # Create/update request body
│   │   ├── commons/
│   │   │   ├── paths/Constants.java        # API path constants
│   │   │   └── utilities/Mapper.java       # ModelMapper wrapper
│   │   ├── configuration/
│   │   │   └── BeanConfiguration.java      # Spring beans (ModelMapper, CORS)
│   │   ├── exception/
│   │   │   └── NotFoundException.java
│   │   └── MedicalRecordApplication.java
│   ├── src/main/resources/
│   │   └── application.properties
│   ├── src/test/java/com/cscb869/MedicalRecord/
│   │   ├── PatientServiceImplTest.java
│   │   ├── DoctorServiceImplTest.java
│   │   ├── SpecializationServiceImplTest.java
│   │   └── AppointmentServiceImplTest.java
│   ├── Dockerfile
│   └── pom.xml
│
├── FrontEnd_MedicalRecord/                 # React frontend
│   ├── src/
│   │   ├── Components/
│   │   │   ├── home.js
│   │   │   ├── menu.js
│   │   │   ├── drawer.js
│   │   │   ├── view_doctors.js
│   │   │   ├── view_patients.js
│   │   │   ├── view_specializations.js
│   │   │   ├── view_appointments.js
│   │   │   ├── create_doctor.js
│   │   │   ├── create_patient.js
│   │   │   ├── create_specialization.js
│   │   │   ├── create_appointment.js
│   │   │   ├── update_appointment.js
│   │   │   ├── patient_appointments.js
│   │   │   └── doctor_appointments.js
│   │   ├── App.js                          # Routes definition
│   │   └── index.js
│   ├── Dockerfile
│   └── package.json
│
├── docker-compose.yml
└── run_containers.sh
```

---

## Data Model

### Entity Relationships

```
DOCTOR (1) ─────────────────────────── (0..*) PATIENT
   │  │                                           │
   │  │ (M:N via doctors_specializations)         │ @ManyToOne
   │  │                                           │
   │  └──── @OneToMany (appointments) ──────► APPOINTMENT ◄──── @OneToMany (appointments)
   │                                                                         │
SPECIALIZATION                                                        @ManyToOne (patient)
                                                                      @ManyToOne (doctor)
```

### Entities

#### Patient
| Field | Type | Constraints |
|---|---|---|
| `id` | `long` | PK, auto-generated |
| `firstName` | `String` | NOT NULL |
| `lastName` | `String` | NOT NULL |
| `EGN` | `long` | NOT NULL (Bulgarian personal ID) |
| `doctor` | `Doctor` | `@ManyToOne` |
| `hasPaidInsurance` | `boolean` | default: `false` |
| `appointments` | `List<Appointment>` | `@OneToMany(mappedBy="patient")`, lazy, JSON-ignored |

#### Doctor
| Field | Type | Constraints |
|---|---|---|
| `id` | `long` | PK, auto-generated |
| `firstName` | `String` | NOT NULL |
| `lastName` | `String` | NOT NULL |
| `specializations` | `List<Specialization>` | `@ManyToMany` |
| `isGp` | `boolean` | default: `false` |
| `patients` | `List<Patient>` | `@OneToMany`, eager |
| `appointments` | `List<Appointment>` | `@OneToMany(mappedBy="doctor")`, lazy, JSON-ignored |

#### Specialization
| Field | Type | Constraints |
|---|---|---|
| `id` | `long` | PK, auto-generated |
| `name` | `String` | NOT NULL |
| `doctors` | `List<Doctor>` | `@ManyToMany`, eager |

#### Appointment
| Field | Type | Constraints |
|---|---|---|
| `id` | `long` | PK, auto-generated |
| `appointmentDate` | `LocalDate` | — |
| `sickLeaveStartDate` | `LocalDate` | — |
| `sickLeaveEndDate` | `LocalDate` | — |
| `illness` | `String` | — |
| `medicine` | `String` | — |
| `patient` | `Patient` | `@ManyToOne`, `@JoinColumn(name="patient_id")` |
| `doctor` | `Doctor` | `@ManyToOne`, `@JoinColumn(name="doctor_id")` |

Date format for all `LocalDate` fields: `yyyy-MM-dd`

#### AppointmentRequest (DTO for create/update)
Accepts `patient_id` and `doctor_id` as plain longs — the service layer resolves the full entities.

```json
{
  "appointment_date": "2024-03-15",
  "illness": "Influenza",
  "medicine": "Paracetamol 500mg",
  "sick_leave_start_date": "2024-03-15",
  "sick_leave_end_date": "2024-03-22",
  "patient_id": 1,
  "doctor_id": 2
}
```

---

## API Reference

Base URL: `http://localhost:8080/api`

### Patients `/api/patient`

| Method | Endpoint | Description |
|---|---|---|
| `GET` | `/list` | Get all patients |
| `GET` | `/id/{id}` | Get patient by ID |
| `GET` | `/egn/{EGN}` | Get patient by EGN |
| `POST` | `/create` | Create a new patient |
| `PUT` | `/update/{id}` | Update patient by ID |
| `DELETE` | `/delete/{id}` | Delete patient by ID |
| `GET` | `/{id}/history` | Get all appointments for a patient |
| `POST` | `/{id}/pay-insurance` | Mark patient insurance as paid |
| `POST` | `/{patientId}/register-doctor/{doctorId}` | Assign patient to a doctor |
| `GET` | `/summary/patients-number-with-illness/{illness}` | Count patients with a given illness |

**Create / Update Patient request body:**
```json
{
  "firstName": "Ivan",
  "lastName": "Petrov",
  "EGN": 8501010001,
  "hasPaidInsurance": false
}
```

---

### Doctors `/api/doctor`

| Method | Endpoint | Description |
|---|---|---|
| `GET` | `/list` | Get all doctors |
| `GET` | `/{id}` | Get doctor by ID |
| `POST` | `/create` | Create a new doctor |
| `PUT` | `/update/{id}` | Update doctor by ID |
| `DELETE` | `/delete/{id}` | Delete doctor by ID |
| `GET` | `/{doctorId}/patient-history/{patientId}` | Get appointment history between doctor and patient |
| `GET` | `/{doctorId}/registered-patients` | Get count of patients registered to a doctor |
| `POST` | `/{id}/become-gp` | Promote doctor to General Practitioner |

**Create / Update Doctor request body:**
```json
{
  "firstName": "Maria",
  "lastName": "Georgieva",
  "isGp": false
}
```

---

### Specializations `/api/specialization`

| Method | Endpoint | Description |
|---|---|---|
| `GET` | `/list` | Get all specializations |
| `GET` | `/id/{id}` | Get specialization by ID |
| `GET` | `/name/{name}` | Get specialization by name |
| `POST` | `/create` | Create a new specialization |
| `PUT` | `/update/{id}` | Update specialization by ID |
| `DELETE` | `/delete/{id}` | Delete specialization by ID |

**Create / Update Specialization request body:**
```json
{
  "name": "Cardiology"
}
```

---

### Appointments `/api/appointment`

| Method | Endpoint | Description |
|---|---|---|
| `GET` | `/list` | Get all appointments |
| `GET` | `/{id}` | Get appointment by ID |
| `POST` | `/create` | Create a new appointment |
| `PUT` | `/update/{id}` | Update appointment by ID |
| `DELETE` | `/delete/{id}` | Delete appointment by ID |
| `GET` | `/doctor/{doctorId}` | Get all appointments for a specific doctor |
| `GET` | `/{doctorId}/visitations` | Get total appointment count for a doctor |

**Create / Update Appointment request body:**
```json
{
  "appointment_date": "2024-03-15",
  "illness": "Influenza",
  "medicine": "Paracetamol 500mg",
  "sick_leave_start_date": "2024-03-15",
  "sick_leave_end_date": "2024-03-22",
  "patient_id": 1,
  "doctor_id": 2
}
```

---

## Frontend

The React SPA communicates with the backend at `http://localhost:8080/api`.

### Routes

| Path | Component | Description |
|---|---|---|
| `/` | `Home` | Landing page |
| `/doctors` | `ViewDoctors` | List all doctors with delete and appointments |
| `/patients` | `ViewPatients` | List all patients with delete and history |
| `/specializations` | `ViewSpecialization` | List all specializations with delete |
| `/create-doctor` | `CreateDoctor` | New doctor form |
| `/create-patient` | `CreatePatient` | New patient form |
| `/create-specialization` | `CreateSpecialization` | New specialization form |
| `/appointments` | `ViewAppointments` | List all appointments with edit/delete |
| `/create-appointment` | `CreateAppointment` | New appointment form with patient/doctor dropdowns |
| `/update-appointment/:id` | `UpdateAppointment` | Edit existing appointment |
| `/patient-appointments/:id` | `PatientAppointments` | Appointment history for a patient |
| `/doctor-appointments/:id` | `DoctorAppointments` | All appointments for a doctor |

### Key Components

| Component | Description |
|---|---|
| `menu.js` | Top navigation bar (Material-UI AppBar) |
| `drawer.js` | Side navigation drawer with links to all pages |
| `view_patients.js` | Patient table with delete and history navigation |
| `view_doctors.js` | Doctor table with delete and appointments navigation |
| `view_specializations.js` | Specialization table with delete |
| `view_appointments.js` | Appointment table with edit and delete |
| `create_patient.js` | Form: firstName, lastName, EGN |
| `create_doctor.js` | Form: firstName, lastName |
| `create_specialization.js` | Form: name |
| `create_appointment.js` | Form: patient/doctor dropdowns, dates, illness, medicine |
| `update_appointment.js` | Pre-populated edit form for an existing appointment |
| `patient_appointments.js` | Appointment history view for a specific patient |
| `doctor_appointments.js` | Appointment list view for a specific doctor |

All create/delete actions clear input fields and display a timed success message on completion.

---

## Tests

Unit tests are located in `MedicalRecord/src/test/java/com/cscb869/MedicalRecord/`.

All tests use JUnit 5 with Mockito (`@ExtendWith(MockitoExtension.class)`, `@Mock`, `@InjectMocks`).

| Test Class | Tests | Coverage |
|---|---|---|
| `PatientServiceImplTest` | 9 | create, get by id/egn, list, update, delete, pay insurance, register doctor, patient history |
| `DoctorServiceImplTest` | 10 | create, get by id, list, update, delete, become GP, patient history, registered patients, doctor appointments |
| `SpecializationServiceImplTest` | 9 | create, get by id/name, list, update, delete, not-found cases |
| `AppointmentServiceImplTest` | 13 | create, get by id, list, update, delete, doctor appointments, visitations, not-found cases |

**Run tests:**
```bash
cd MedicalRecord
./mvnw test
```

---

## Getting Started

### Prerequisites

#### Docker path
- Docker Desktop (or Docker Engine + Docker Compose plugin) installed and running
- Ports `3000`, `8080`, `3306` free on your machine
- JDK 11 and Maven (or the included `mvnw` wrapper) — needed to build the backend JAR before packaging it into a Docker image

#### Local path (without Docker)
- JDK 11 — **Spring Boot 2.3.4 does not support JDK 17+**
- Node.js 16+ and npm
- MySQL 8 running on `localhost:3306` with user `root` / password `root`

---

### Option A — Docker Compose (recommended)

All commands are run from the repo root (`MedicalRecord/`).

```bash
# 1. Package the backend JAR (required before building the Docker image)
cd MedicalRecord
./mvnw package -DskipTests
cd ..

# 2. Build the backend Docker image
cd MedicalRecord
docker build -t spring-md-st .
cd ..

# 3. Build the frontend Docker image
cd FrontEnd_MedicalRecord
docker build -t react-md-st .
cd ..

# 4. Start all three services (MySQL, Spring Boot, React)
docker-compose up
```

Or run all of the above in one go with the convenience script:

```bash
chmod +x run_containers.sh
./run_containers.sh
```

Once running, open:
| Service | URL |
|---|---|
| React frontend | http://localhost:3000 |
| Spring Boot API | http://localhost:8080/api |
| MySQL | `localhost:3306` (db: `medical_record`, user: `root`, pass: `root`) |

To stop and remove containers:
```bash
docker-compose down
```

---

### Option B — Run Locally (without Docker)

You need MySQL running locally before starting the backend.

**1. Start MySQL and create the database**

The datasource URL includes `createDatabaseIfNotExist=true`, so the schema is created automatically on first boot. Just ensure the server is up:

```bash
# macOS with Homebrew
brew services start mysql

# or start it manually and verify
mysql -u root -proot -e "SELECT 1;"
```

**2. Start the backend**

```bash
cd MedicalRecord
./mvnw spring-boot:run
```

> If you have JDK 17+ set as the default, point `JAVA_HOME` at JDK 11 first:
> ```bash
> export JAVA_HOME=/path/to/jdk-11
> ./mvnw spring-boot:run
> ```

The API is ready when you see `Started MedicalRecordApplication` in the console.

**3. Start the frontend** (in a separate terminal)

```bash
cd FrontEnd_MedicalRecord
npm install        # only needed the first time
npm start
```

The React dev server opens automatically at http://localhost:3000.

> If you see an OpenSSL error with Node 17+, the `package.json` start script already sets `NODE_OPTIONS=--openssl-legacy-provider` to handle this.

---

## Configuration

### Backend (`application.properties`)

```properties
spring.jpa.hibernate.ddl-auto=create
spring.datasource.url=jdbc:mysql://localhost:3306/medical_record?autoReconnect=true&useSSL=false&createDatabaseIfNotExist=true&allowPublicKeyRetrieval=true&serverTimezone=UTC
spring.datasource.username=root
spring.datasource.password=root
spring.jpa.properties.hibernate.globally_quoted_identifiers=true
spring.main.allow-circular-references=true
logging.level.root=WARN
```

> **Note:** `ddl-auto=create` drops and recreates the schema on every startup. Change to `update` or `validate` for persistent data.

### Docker Compose Environment Overrides

The `docker-compose.yml` overrides datasource settings so the Spring Boot container connects to the MySQL container by service name:

```yaml
SPRING_DATASOURCE_URL: jdbc:mysql://mr-sql-container:3306/medical_record?autoReconnect=true&useSSL=false&createDatabaseIfNotExist=true&allowPublicKeyRetrieval=true&serverTimezone=UTC
SPRING_DATASOURCE_USERNAME: root
SPRING_DATASOURCE_PASSWORD: root
SPRING_JPA_HIBERNATE_DDL-AUTO: create
```

### Java Version

The project targets Java 11. If running locally with a newer JDK, point `JAVA_HOME` to a JDK 11 installation:

```bash
export JAVA_HOME=/path/to/jdk-11
./mvnw spring-boot:run
```
