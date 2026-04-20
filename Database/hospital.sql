-- ══════════════════════════════════════════════════════════════════════════════
--  hospital.sql  –  Hospital Management System  (MySQL 8.x)
--
--  Contains:
--    1.  Schema creation  (DDL)
--    2.  Sample data      (DML  – all rows from the project document)
--    3.  Constraints      (CHECK, UNIQUE, FOREIGN KEY)
--    4.  Indexes          (for query performance)
--    5.  Views            (from Chapter 3)
--    6.  Triggers         (from Chapter 3)
--    7.  Stored Procedures + Cursors  (from Chapter 3)
--    8.  Sample queries   (from Chapter 3 – aggregate, joins, subqueries, sets)
-- ══════════════════════════════════════════════════════════════════════════════

-- ─────────────────────────────────────────────────────────────────────────────
--  0.  DATABASE SETUP
-- ─────────────────────────────────────────────────────────────────────────────
CREATE DATABASE IF NOT EXISTS HospitalDB
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE HospitalDB;

-- Drop tables in reverse FK order so re-runs don't fail
DROP TABLE IF EXISTS BILLING;
DROP TABLE IF EXISTS MEDICAL_RECORD;
DROP TABLE IF EXISTS ADMISSION;
DROP TABLE IF EXISTS APPOINTMENT;
DROP TABLE IF EXISTS MEDICINE;
DROP TABLE IF EXISTS PATIENT;
DROP TABLE IF EXISTS DOCTOR;
DROP TABLE IF EXISTS DEPARTMENT;

-- ─────────────────────────────────────────────────────────────────────────────
--  1.  TABLE DEFINITIONS
-- ─────────────────────────────────────────────────────────────────────────────

-- Table 1: DEPARTMENT
CREATE TABLE DEPARTMENT (
    dept_id   INT          NOT NULL AUTO_INCREMENT,
    dept_name VARCHAR(50)  NOT NULL,
    location  VARCHAR(50)  NOT NULL,
    CONSTRAINT pk_department  PRIMARY KEY (dept_id),
    CONSTRAINT uq_dept_name   UNIQUE (dept_name)
);

-- Table 2: DOCTOR
CREATE TABLE DOCTOR (
    doctor_id      INT          NOT NULL AUTO_INCREMENT,
    name           VARCHAR(50)  NOT NULL,
    specialization VARCHAR(50)  NOT NULL,
    phone          VARCHAR(15),
    email          VARCHAR(50),
    salary         INT          NOT NULL,
    dept_id        INT,
    CONSTRAINT pk_doctor        PRIMARY KEY (doctor_id),
    CONSTRAINT uq_doctor_phone  UNIQUE (phone),
    CONSTRAINT uq_doctor_email  UNIQUE (email),
    CONSTRAINT fk_doctor_dept   FOREIGN KEY (dept_id)
                                REFERENCES DEPARTMENT(dept_id)
                                ON DELETE SET NULL
                                ON UPDATE CASCADE,
    -- Mirrors the BEFORE INSERT trigger logic at the DB constraint level
    CONSTRAINT chk_salary       CHECK (salary >= 30000)
);

-- Table 3: PATIENT
CREATE TABLE PATIENT (
    patient_id  INT          NOT NULL AUTO_INCREMENT,
    name        VARCHAR(50)  NOT NULL,
    dob         DATE         NOT NULL,
    gender      VARCHAR(10),
    blood_group VARCHAR(5),
    phone       VARCHAR(15)  NOT NULL,
    email       VARCHAR(50),
    address     VARCHAR(100),
    CONSTRAINT pk_patient       PRIMARY KEY (patient_id),
    CONSTRAINT uq_patient_phone UNIQUE (phone),
    CONSTRAINT chk_gender       CHECK (gender IN ('Male', 'Female', 'Other'))
);

-- Table 4: APPOINTMENT
CREATE TABLE APPOINTMENT (
    appointment_id   INT          NOT NULL AUTO_INCREMENT,
    patient_id       INT,
    doctor_id        INT,
    appointment_date DATE         NOT NULL,
    appointment_time VARCHAR(10),
    status           VARCHAR(20)  DEFAULT 'Scheduled',
    notes            VARCHAR(200),
    CONSTRAINT pk_appointment     PRIMARY KEY (appointment_id),
    CONSTRAINT fk_appt_patient    FOREIGN KEY (patient_id)
                                  REFERENCES PATIENT(patient_id)
                                  ON DELETE CASCADE,
    CONSTRAINT fk_appt_doctor     FOREIGN KEY (doctor_id)
                                  REFERENCES DOCTOR(doctor_id)
                                  ON DELETE SET NULL,
    CONSTRAINT chk_appt_status    CHECK (status IN ('Scheduled', 'Completed', 'Cancelled'))
);

-- Table 5: ADMISSION
CREATE TABLE ADMISSION (
    admission_id   INT  NOT NULL AUTO_INCREMENT,
    patient_id     INT,
    doctor_id      INT,
    admit_date     DATE NOT NULL,
    discharge_date DATE,
    total_charges  INT  DEFAULT 0,
    CONSTRAINT pk_admission      PRIMARY KEY (admission_id),
    CONSTRAINT fk_adm_patient    FOREIGN KEY (patient_id)
                                 REFERENCES PATIENT(patient_id)
                                 ON DELETE CASCADE,
    CONSTRAINT fk_adm_doctor     FOREIGN KEY (doctor_id)
                                 REFERENCES DOCTOR(doctor_id)
                                 ON DELETE SET NULL,
    CONSTRAINT chk_discharge     CHECK (discharge_date IS NULL OR discharge_date >= admit_date)
);

-- Table 6: MEDICAL_RECORD
CREATE TABLE MEDICAL_RECORD (
    record_id    INT          NOT NULL AUTO_INCREMENT,
    admission_id INT,
    patient_id   INT,
    doctor_id    INT,
    diagnosis    VARCHAR(200) NOT NULL,
    treatment    VARCHAR(200),
    record_date  DATE         NOT NULL,
    CONSTRAINT pk_medical_record  PRIMARY KEY (record_id),
    CONSTRAINT fk_mr_admission    FOREIGN KEY (admission_id)
                                  REFERENCES ADMISSION(admission_id)
                                  ON DELETE CASCADE,
    CONSTRAINT fk_mr_patient      FOREIGN KEY (patient_id)
                                  REFERENCES PATIENT(patient_id)
                                  ON DELETE CASCADE,
    CONSTRAINT fk_mr_doctor       FOREIGN KEY (doctor_id)
                                  REFERENCES DOCTOR(doctor_id)
                                  ON DELETE SET NULL
);

-- Table 7: MEDICINE
CREATE TABLE MEDICINE (
    medicine_id  INT          NOT NULL AUTO_INCREMENT,
    name         VARCHAR(100) NOT NULL,
    manufacturer VARCHAR(100),
    unit_price   INT          NOT NULL,
    stock_qty    INT          DEFAULT 0,
    expiry_date  DATE,
    CONSTRAINT pk_medicine       PRIMARY KEY (medicine_id),
    CONSTRAINT uq_medicine_name  UNIQUE (name),
    CONSTRAINT chk_unit_price    CHECK (unit_price > 0),
    CONSTRAINT chk_stock         CHECK (stock_qty >= 0)
);

-- Table 8: BILLING
CREATE TABLE BILLING (
    bill_id          INT         NOT NULL AUTO_INCREMENT,
    patient_id       INT,
    admission_id     INT,
    bill_date        DATE        NOT NULL,
    room_charges     INT         DEFAULT 0,
    medicine_charges INT         DEFAULT 0,
    doctor_charges   INT         DEFAULT 0,
    total_amount     INT         NOT NULL,
    paid_amount      INT         DEFAULT 0,
    payment_status   VARCHAR(20) DEFAULT 'Pending',
    CONSTRAINT pk_billing            PRIMARY KEY (bill_id),
    CONSTRAINT fk_bill_patient       FOREIGN KEY (patient_id)
                                     REFERENCES PATIENT(patient_id)
                                     ON DELETE CASCADE,
    CONSTRAINT fk_bill_admission     FOREIGN KEY (admission_id)
                                     REFERENCES ADMISSION(admission_id)
                                     ON DELETE CASCADE,
    CONSTRAINT uq_bill_admission     UNIQUE (admission_id),   -- one bill per admission
    CONSTRAINT chk_payment_status    CHECK (payment_status IN ('Pending', 'Partial', 'Paid')),
    CONSTRAINT chk_paid_lte_total    CHECK (paid_amount <= total_amount),
    CONSTRAINT chk_total_positive    CHECK (total_amount >= 0)
);

-- ─────────────────────────────────────────────────────────────────────────────
--  2.  INDEXES  (speed up common lookups)
-- ─────────────────────────────────────────────────────────────────────────────

CREATE INDEX idx_doctor_dept       ON DOCTOR(dept_id);
CREATE INDEX idx_appt_patient      ON APPOINTMENT(patient_id);
CREATE INDEX idx_appt_doctor       ON APPOINTMENT(doctor_id);
CREATE INDEX idx_appt_date         ON APPOINTMENT(appointment_date);
CREATE INDEX idx_admission_patient ON ADMISSION(patient_id);
CREATE INDEX idx_billing_patient   ON BILLING(patient_id);
CREATE INDEX idx_billing_status    ON BILLING(payment_status);

-- ─────────────────────────────────────────────────────────────────────────────
--  3.  SAMPLE DATA  (from project document)
-- ─────────────────────────────────────────────────────────────────────────────

-- DEPARTMENT
INSERT INTO DEPARTMENT (dept_name, location) VALUES
    ('Cardiology',       'Block A'),
    ('Neurology',        'Block B'),
    ('Orthopaedics',     'Block C'),
    ('General Medicine', 'Block D'),
    ('Gynaecology',      'Block E');

-- DOCTOR
INSERT INTO DOCTOR (name, specialization, phone, email, salary, dept_id) VALUES
    ('Dr. Ramesh Kumar',  'Cardiology',       '9876543210', 'ramesh@hms.com',  85000, 1),
    ('Dr. Priya Sharma',  'Neurology',        '9876543211', 'priya@hms.com',   75000, 2),
    ('Dr. Anand Rajan',   'Orthopaedics',     '9876543212', 'anand@hms.com',   70000, 3),
    ('Dr. Kavitha Menon', 'Gynaecology',      '9876543213', 'kavitha@hms.com', 65000, 5),
    ('Dr. Suresh Pillai', 'General Medicine', '9876543214', 'suresh@hms.com',  60000, 4);

-- PATIENT
INSERT INTO PATIENT (name, dob, gender, blood_group, phone, email, address) VALUES
    ('Arun Kumar',   '1990-06-15', 'Male',   'B+',  '9845671230', 'arun@gmail.com',    'Chennai'),
    ('Meena Devi',   '1985-03-22', 'Female', 'O-',  '9756432108', 'meena@gmail.com',   'Coimbatore'),
    ('Vikram Singh', '1978-11-08', 'Male',   'A+',  '9812345670', 'vikram@gmail.com',  'Madurai'),
    ('Divya Nair',   '1995-07-30', 'Female', 'AB+', '9865432107', 'divya@gmail.com',   'Trichy'),
    ('Karthik Raj',  '1988-02-14', 'Male',   'O+',  '9745321089', 'karthik@gmail.com', 'Salem');

-- APPOINTMENT
INSERT INTO APPOINTMENT (patient_id, doctor_id, appointment_date, appointment_time, status, notes) VALUES
    (1, 1, '2025-03-10', '09:00 AM', 'Completed', 'Chest pain'),
    (2, 2, '2025-03-11', '10:00 AM', 'Completed', 'Headache'),
    (3, 3, '2025-03-12', '11:00 AM', 'Scheduled',  'Knee pain'),
    (4, 1, '2025-03-13', '02:00 PM', 'Cancelled',  'Routine checkup'),
    (5, 4, '2025-03-14', '03:00 PM', 'Completed',  'Prenatal visit');

-- ADMISSION
INSERT INTO ADMISSION (patient_id, doctor_id, admit_date, discharge_date, total_charges) VALUES
    (1, 1, '2025-03-10', NULL,         0),
    (2, 2, '2025-03-11', NULL,         0),
    (3, 3, '2025-03-12', NULL,         0),
    (4, 4, '2025-03-13', NULL,         0),
    (5, 4, '2025-03-01', '2025-03-07', 25000);

-- MEDICAL_RECORD
INSERT INTO MEDICAL_RECORD (admission_id, patient_id, doctor_id, diagnosis, treatment, record_date) VALUES
    (1, 1, 1, 'Acute chest pain, suspected angina', 'ECG, blood thinners prescribed',     '2025-03-10'),
    (2, 2, 2, 'Chronic migraine',                   'Sumatriptan prescribed, MRI advised', '2025-03-11'),
    (3, 3, 3, 'ACL tear in right knee',             'Physiotherapy, Naproxen prescribed',  '2025-03-12'),
    (4, 4, 4, 'Normal prenatal checkup',            'Iron and folic acid supplements',     '2025-03-13'),
    (5, 5, 4, 'Routine antenatal visit',            'Calcium supplements prescribed',      '2025-03-01');

-- MEDICINE
INSERT INTO MEDICINE (name, manufacturer, unit_price, stock_qty, expiry_date) VALUES
    ('Paracetamol 500mg', 'Sun Pharma', 10,  500, '2026-12-31'),
    ('Amoxicillin 250mg', 'Cipla',      30,  300, '2026-06-30'),
    ('Naproxen 250mg',    'Torrent',    20,  250, '2027-03-31'),
    ('Sumatriptan 50mg',  'Lupin',      80,  100, '2026-09-30'),
    ('Ferrous Sulfate',   'Mankind',    15,  400, '2026-12-31');

-- BILLING  (total_amount = room + medicine + doctor)
INSERT INTO BILLING (patient_id, admission_id, bill_date, room_charges, medicine_charges, doctor_charges, total_amount, paid_amount, payment_status) VALUES
    (1, 1, '2025-03-10', 5000, 300,  2000, 7300,  7300, 'Paid'),
    (2, 2, '2025-03-11', 4000, 800,  1500, 6300,  0,    'Pending'),
    (3, 3, '2025-03-12', 3500, 400,  1500, 5400,  3000, 'Partial'),
    (4, 4, '2025-03-13', 6000, 450,  1500, 7950,  7950, 'Paid'),
    (5, 5, '2025-03-01', 8000, 900,  2000, 10900, 5000, 'Partial');

-- ─────────────────────────────────────────────────────────────────────────────
--  4.  VIEWS  (Chapter 3.2 of project document)
-- ─────────────────────────────────────────────────────────────────────────────

-- View 1: Patient + Doctor + Appointment date together
CREATE OR REPLACE VIEW PATIENT_APPOINTMENTS AS
    SELECT
        P.name             AS patient_name,
        D.name             AS doctor_name,
        D.specialization,
        A.appointment_date,
        A.appointment_time,
        A.status,
        A.notes
    FROM APPOINTMENT A
    INNER JOIN PATIENT P ON A.patient_id = P.patient_id
    INNER JOIN DOCTOR  D ON A.doctor_id  = D.doctor_id;

-- View 2: Patients with unpaid bills
CREATE OR REPLACE VIEW PENDING_BILLS AS
    SELECT
        P.name           AS patient_name,
        P.phone,
        B.bill_id,
        B.total_amount,
        B.paid_amount,
        (B.total_amount - B.paid_amount) AS outstanding_amount,
        B.payment_status
    FROM BILLING B
    INNER JOIN PATIENT P ON B.patient_id = P.patient_id
    WHERE B.payment_status IN ('Pending', 'Partial');

-- View 3: Doctor with department details
CREATE OR REPLACE VIEW DOCTOR_DEPT AS
    SELECT
        D.doctor_id,
        D.name             AS doctor_name,
        D.specialization,
        D.salary,
        DEP.dept_name,
        DEP.location       AS dept_location
    FROM DOCTOR D
    INNER JOIN DEPARTMENT DEP ON D.dept_id = DEP.dept_id;

-- View 4: Full billing summary (joins 3 tables)
CREATE OR REPLACE VIEW BILLING_SUMMARY AS
    SELECT
        B.bill_id,
        P.name             AS patient_name,
        D.name             AS doctor_name,
        B.bill_date,
        B.room_charges,
        B.medicine_charges,
        B.doctor_charges,
        B.total_amount,
        B.paid_amount,
        B.payment_status
    FROM BILLING B
    INNER JOIN PATIENT   P  ON B.patient_id   = P.patient_id
    INNER JOIN ADMISSION AD ON B.admission_id = AD.admission_id
    INNER JOIN DOCTOR    D  ON AD.doctor_id   = D.doctor_id;

-- ─────────────────────────────────────────────────────────────────────────────
--  5.  TRIGGERS  (Chapter 3.3 of project document)
-- ─────────────────────────────────────────────────────────────────────────────

DELIMITER $$

-- Trigger 1: BEFORE INSERT on DOCTOR
-- Enforces minimum salary of ₹30,000 at DB level
-- (application layer also enforces this via @Min(30000))
DROP TRIGGER IF EXISTS before_insert_doctor$$
CREATE TRIGGER before_insert_doctor
BEFORE INSERT ON DOCTOR
FOR EACH ROW
BEGIN
    IF NEW.salary < 30000 THEN
        SET NEW.salary = 30000;
    END IF;
END$$

-- Trigger 2: AFTER INSERT on BILLING
-- Automatically syncs total_charges in ADMISSION whenever a bill is generated
DROP TRIGGER IF EXISTS after_insert_billing$$
CREATE TRIGGER after_insert_billing
AFTER INSERT ON BILLING
FOR EACH ROW
BEGIN
    UPDATE ADMISSION
    SET total_charges = NEW.total_amount
    WHERE admission_id = NEW.admission_id;
END$$

-- Trigger 3: AFTER UPDATE on BILLING (when payment is recorded)
-- Re-syncs admission total_charges on bill update as well
DROP TRIGGER IF EXISTS after_update_billing$$
CREATE TRIGGER after_update_billing
AFTER UPDATE ON BILLING
FOR EACH ROW
BEGIN
    IF NEW.total_amount <> OLD.total_amount THEN
        UPDATE ADMISSION
        SET total_charges = NEW.total_amount
        WHERE admission_id = NEW.admission_id;
    END IF;
END$$

-- Trigger 4: BEFORE DELETE on PATIENT
-- Prevents deleting a patient who still has billing records
DROP TRIGGER IF EXISTS before_delete_patient$$
CREATE TRIGGER before_delete_patient
BEFORE DELETE ON PATIENT
FOR EACH ROW
BEGIN
    DECLARE bill_count INT DEFAULT 0;
    SELECT COUNT(*) INTO bill_count
    FROM BILLING
    WHERE patient_id = OLD.patient_id;

    IF bill_count > 0 THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Cannot delete patient: existing billing records found.';
    END IF;
END$$

-- Trigger 5: BEFORE INSERT on APPOINTMENT
-- Prevents booking the same doctor for two patients at the same time slot
DROP TRIGGER IF EXISTS before_insert_appointment$$
CREATE TRIGGER before_insert_appointment
BEFORE INSERT ON APPOINTMENT
FOR EACH ROW
BEGIN
    DECLARE conflict_count INT DEFAULT 0;
    SELECT COUNT(*) INTO conflict_count
    FROM APPOINTMENT
    WHERE doctor_id        = NEW.doctor_id
      AND appointment_date = NEW.appointment_date
      AND appointment_time = NEW.appointment_time
      AND status          != 'Cancelled';

    IF conflict_count > 0 THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Doctor already has an appointment at this date and time.';
    END IF;
END$$

DELIMITER ;

-- ─────────────────────────────────────────────────────────────────────────────
--  6.  STORED PROCEDURES & CURSORS  (Chapter 3.3)
-- ─────────────────────────────────────────────────────────────────────────────

DELIMITER $$

-- Procedure 1: List all doctors with their salaries (cursor demo)
DROP PROCEDURE IF EXISTS show_doctors$$
CREATE PROCEDURE show_doctors()
BEGIN
    DECLARE done     INT DEFAULT 0;
    DECLARE v_name   VARCHAR(50);
    DECLARE v_salary INT;

    DECLARE doctor_cursor CURSOR FOR
        SELECT name, salary FROM DOCTOR ORDER BY salary DESC;

    DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = 1;

    OPEN doctor_cursor;
    read_loop: LOOP
        FETCH doctor_cursor INTO v_name, v_salary;
        IF done = 1 THEN
            LEAVE read_loop;
        END IF;
        SELECT v_name AS doctor_name, v_salary AS salary;
    END LOOP;
    CLOSE doctor_cursor;
END$$

-- Procedure 2: Show patients with pending bills (cursor + join)
DROP PROCEDURE IF EXISTS show_pending_patients$$
CREATE PROCEDURE show_pending_patients()
BEGIN
    DECLARE done     INT DEFAULT 0;
    DECLARE v_name   VARCHAR(50);
    DECLARE v_amount INT;

    DECLARE pending_cursor CURSOR FOR
        SELECT P.name, B.total_amount - B.paid_amount AS outstanding
        FROM BILLING B
        INNER JOIN PATIENT P ON B.patient_id = P.patient_id
        WHERE B.payment_status IN ('Pending', 'Partial')
        ORDER BY outstanding DESC;

    DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = 1;

    OPEN pending_cursor;
    read_loop: LOOP
        FETCH pending_cursor INTO v_name, v_amount;
        IF done = 1 THEN
            LEAVE read_loop;
        END IF;
        SELECT v_name AS patient_name, v_amount AS outstanding_amount;
    END LOOP;
    CLOSE pending_cursor;
END$$

-- Procedure 3: Safe patient insert with exception handling
DROP PROCEDURE IF EXISTS add_patient$$
CREATE PROCEDURE add_patient(
    IN p_name      VARCHAR(50),
    IN p_dob       DATE,
    IN p_gender    VARCHAR(10),
    IN p_phone     VARCHAR(15),
    IN p_email     VARCHAR(50),
    IN p_address   VARCHAR(100)
)
BEGIN
    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
        GET DIAGNOSTICS CONDITION 1
            @msg = MESSAGE_TEXT;
        SELECT CONCAT('Error: ', @msg) AS result;
        ROLLBACK;
    END;

    START TRANSACTION;
    INSERT INTO PATIENT (name, dob, gender, phone, email, address)
    VALUES (p_name, p_dob, p_gender, p_phone, p_email, p_address);
    COMMIT;
    SELECT CONCAT('Patient "', p_name, '" registered successfully. ID: ', LAST_INSERT_ID()) AS result;
END$$

-- Procedure 4: Process bill payment (transaction demo)
DROP PROCEDURE IF EXISTS process_payment$$
CREATE PROCEDURE process_payment(
    IN p_bill_id       INT,
    IN p_payment_amount INT
)
BEGIN
    DECLARE v_total    INT;
    DECLARE v_paid     INT;
    DECLARE v_new_paid INT;
    DECLARE v_status   VARCHAR(20);

    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
        ROLLBACK;
        SELECT 'Error: Payment processing failed. Transaction rolled back.' AS result;
    END;

    START TRANSACTION;

    SELECT total_amount, paid_amount
    INTO   v_total, v_paid
    FROM   BILLING
    WHERE  bill_id = p_bill_id
    FOR UPDATE;  -- row-level lock (prevents lost update)

    IF v_total IS NULL THEN
        ROLLBACK;
        SELECT 'Error: Bill not found.' AS result;
    ELSEIF p_payment_amount <= 0 THEN
        ROLLBACK;
        SELECT 'Error: Payment amount must be positive.' AS result;
    ELSEIF (v_paid + p_payment_amount) > v_total THEN
        ROLLBACK;
        SELECT CONCAT('Error: Payment ₹', p_payment_amount,
                      ' exceeds outstanding balance ₹', (v_total - v_paid)) AS result;
    ELSE
        SET v_new_paid = v_paid + p_payment_amount;

        IF v_new_paid >= v_total THEN
            SET v_status = 'Paid';
        ELSE
            SET v_status = 'Partial';
        END IF;

        UPDATE BILLING
        SET paid_amount    = v_new_paid,
            payment_status = v_status
        WHERE bill_id = p_bill_id;

        COMMIT;
        SELECT CONCAT('Payment ₹', p_payment_amount,
                      ' recorded. New status: ', v_status) AS result;
    END IF;
END$$

-- Procedure 5: Discharge a patient (atomicity demo — both fields update or neither)
DROP PROCEDURE IF EXISTS discharge_patient$$
CREATE PROCEDURE discharge_patient(
    IN p_admission_id  INT,
    IN p_discharge_date DATE,
    IN p_total_charges  INT
)
BEGIN
    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
        ROLLBACK;
        SELECT 'Error: Discharge failed. Both discharge date and charges rolled back.' AS result;
    END;

    START TRANSACTION;

    UPDATE ADMISSION
    SET discharge_date = p_discharge_date,
        total_charges  = p_total_charges
    WHERE admission_id = p_admission_id;

    IF ROW_COUNT() = 0 THEN
        ROLLBACK;
        SELECT 'Error: Admission not found.' AS result;
    ELSE
        COMMIT;
        SELECT CONCAT('Patient discharged. Admission #', p_admission_id,
                      ' closed with charges ₹', p_total_charges) AS result;
    END IF;
END$$

DELIMITER ;

-- ─────────────────────────────────────────────────────────────────────────────
--  7.  SAMPLE QUERIES  (Chapter 3 — for reference and testing)
-- ─────────────────────────────────────────────────────────────────────────────

-- ── Aggregate functions ──────────────────────────────────────────────────────

-- Q1: Doctor salary stats
SELECT
    COUNT(doctor_id) AS total_doctors,
    AVG(salary)      AS average_salary,
    MAX(salary)      AS highest_salary,
    MIN(salary)      AS lowest_salary
FROM DOCTOR;

-- Q2: Number of doctors per department
SELECT
    DEP.dept_name,
    COUNT(D.doctor_id) AS number_of_doctors
FROM DOCTOR D
INNER JOIN DEPARTMENT DEP ON D.dept_id = DEP.dept_id
GROUP BY D.dept_id, DEP.dept_name
ORDER BY number_of_doctors DESC;

-- Q3: Patients whose total bill > ₹7000
SELECT
    P.name,
    SUM(B.total_amount) AS total_billed
FROM BILLING B
INNER JOIN PATIENT P ON B.patient_id = P.patient_id
GROUP BY B.patient_id, P.name
HAVING SUM(B.total_amount) > 7000;

-- ── SET operations ───────────────────────────────────────────────────────────

-- Q4: All phone numbers (doctor + patient, no duplicates)
SELECT phone FROM DOCTOR
UNION
SELECT phone FROM PATIENT;

-- Q5: Patient IDs from both APPOINTMENT and BILLING (with duplicates)
SELECT patient_id FROM APPOINTMENT
UNION ALL
SELECT patient_id FROM BILLING;

-- Q6: Patients who have BOTH an appointment AND a billing record
SELECT patient_id FROM APPOINTMENT
WHERE patient_id IN (SELECT patient_id FROM BILLING);

-- ── Subqueries ───────────────────────────────────────────────────────────────

-- Q7: Names of patients with Pending payment status
SELECT name FROM PATIENT
WHERE patient_id IN (
    SELECT patient_id FROM BILLING
    WHERE payment_status = 'Pending'
);

-- Q8: Doctor with the highest salary
SELECT name, salary FROM DOCTOR
WHERE salary = (SELECT MAX(salary) FROM DOCTOR);

-- Q9: Patients who have NOT made any appointment
SELECT name FROM PATIENT
WHERE patient_id NOT IN (
    SELECT DISTINCT patient_id FROM APPOINTMENT
    WHERE patient_id IS NOT NULL
);

-- ── JOINs ────────────────────────────────────────────────────────────────────

-- Q10: Patient name, doctor name, appointment date (INNER JOIN)
SELECT
    P.name  AS patient_name,
    D.name  AS doctor_name,
    A.appointment_date,
    A.status
FROM APPOINTMENT A
INNER JOIN PATIENT P ON A.patient_id = P.patient_id
INNER JOIN DOCTOR  D ON A.doctor_id  = D.doctor_id;

-- Q11: All doctors and their appointments including doctors with NO appointments (LEFT JOIN)
SELECT
    D.name           AS doctor_name,
    D.specialization,
    A.appointment_id,
    A.status
FROM DOCTOR D
LEFT JOIN APPOINTMENT A ON D.doctor_id = A.doctor_id;

-- Q12: Patient, doctor, and billing amount together (3-way JOIN)
SELECT
    P.name         AS patient_name,
    D.name         AS doctor_name,
    B.total_amount,
    B.payment_status
FROM BILLING B
INNER JOIN PATIENT   P  ON B.patient_id   = P.patient_id
INNER JOIN ADMISSION AD ON B.admission_id = AD.admission_id
INNER JOIN DOCTOR    D  ON AD.doctor_id   = D.doctor_id;

-- ── Concurrency (locking) examples ──────────────────────────────────────────

-- Row-level lock: prevent lost update on billing
-- START TRANSACTION;
-- SELECT * FROM BILLING WHERE bill_id = 7002 FOR UPDATE;
-- UPDATE BILLING SET paid_amount = 6300, payment_status = 'Paid' WHERE bill_id = 7002;
-- COMMIT;

-- Table-level lock: bulk medicine restock
-- LOCK TABLE MEDICINE WRITE;
-- UPDATE MEDICINE SET stock_qty = stock_qty + 100 WHERE medicine_id = 4;
-- UNLOCK TABLES;

-- ─────────────────────────────────────────────────────────────────────────────
--  8.  VERIFY DATA
-- ─────────────────────────────────────────────────────────────────────────────
SELECT 'DEPARTMENT'   AS tbl, COUNT(*) AS rows FROM DEPARTMENT   UNION ALL
SELECT 'DOCTOR'       AS tbl, COUNT(*) AS rows FROM DOCTOR       UNION ALL
SELECT 'PATIENT'      AS tbl, COUNT(*) AS rows FROM PATIENT      UNION ALL
SELECT 'APPOINTMENT'  AS tbl, COUNT(*) AS rows FROM APPOINTMENT  UNION ALL
SELECT 'ADMISSION'    AS tbl, COUNT(*) AS rows FROM ADMISSION    UNION ALL
SELECT 'MEDICAL_RECORD' AS tbl, COUNT(*) AS rows FROM MEDICAL_RECORD UNION ALL
SELECT 'MEDICINE'     AS tbl, COUNT(*) AS rows FROM MEDICINE     UNION ALL
SELECT 'BILLING'      AS tbl, COUNT(*) AS rows FROM BILLING;
