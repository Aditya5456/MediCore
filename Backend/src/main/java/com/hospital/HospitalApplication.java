package com.hospital;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.Environment;

/**
 * ─────────────────────────────────────────────────────────────────────────────
 *  HospitalApplication  –  Spring Boot entry point
 *
 *  @SpringBootApplication is a shortcut for three annotations combined:
 *
 *    @Configuration       → This class can declare @Bean methods
 *    @EnableAutoConfiguration → Spring Boot auto-configures beans based on
 *                               classpath (e.g. sees MySQL driver → sets up
 *                               DataSource automatically)
 *    @ComponentScan       → Scans com.hospital and all sub-packages for
 *                           @Component, @Service, @Repository, @Controller
 *
 *  How to run:
 *    mvn spring-boot:run
 *    OR
 *    java -jar target/hospital-backend-1.0.0.jar
 *
 *  Server starts at:  http://localhost:8080
 * ─────────────────────────────────────────────────────────────────────────────
 */
@SpringBootApplication
@Slf4j
public class HospitalApplication {

    public static void main(String[] args) {
        var context = SpringApplication.run(HospitalApplication.class, args);

        Environment env = context.getEnvironment();
        String port    = env.getProperty("server.port", "8080");
        String appName = env.getProperty("spring.application.name", "hospital-backend");

        log.info("""
                \n
                ╔══════════════════════════════════════════════════════╗
                ║         Hospital Management System – Backend         ║
                ╠══════════════════════════════════════════════════════╣
                ║  Application : {}
                ║  Local URL   : http://localhost:{}
                ║  API Base    : http://localhost:{}/api
                ╠══════════════════════════════════════════════════════╣
                ║  Patient API    →  /api/patients                     ║
                ║  Doctor API     →  /api/doctors                      ║
                ║  Appointment API→  /api/appointments                 ║
                ║  Billing API    →  /api/billing                      ║
                ╚══════════════════════════════════════════════════════╝
                """, appName, port, port);
    }
}
