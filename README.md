# Parking Lot Reservation System

A complete backend REST API for a smart parking lot reservation system, built with Java and Spring Boot. This project allows for the management of parking infrastructure and handles the entire customer reservation lifecycle, from booking to cancellation.

**[➡️ View Live Demo](https://parkinglotmanagement-1.onrender.com)**

---

## Features

* **Full CRUD Functionality:** Endpoints for managing floors, slots, and reservations.
* **Intelligent Booking Logic:** Prevents double bookings and dynamically calculates fees based on vehicle type and duration (with partial hours rounded up).
* **Extensible Design:** Easily supports new vehicle types and rate changes via a centralized Enum.
* **Concurrent Booking Protection:** Implemented optimistic locking (`@Version`) to safely handle simultaneous reservation attempts.
* **API Documentation:** Automatically generated, interactive API documentation using Swagger/OpenAPI.
* **Pagination & Sorting:** Efficiently handles large datasets on the availability endpoint.
* **Robust Error Handling:** A global exception handler provides clear, consistent error responses for various scenarios.
* **Data Validation:** Uses Jakarta Bean Validation to ensure the integrity of all incoming data.

## Tech Stack

* **Language:** Java 21
* **Framework:** Spring Boot 3+
* **Data:** Spring Data JPA, Hibernate
* **Database:** PostgreSQL
* **Build Tool:** Maven
* **Testing:** JUnit 5, Mockito
* **Deployment:** Docker, Render

## API Documentation

Interactive API documentation is available through Swagger UI. This allows you to view all endpoints and test them directly from your browser.

**[➡️ View Live API Docs (Swagger UI)](https://parkinglotmanagement-1.onrender.com/swagger-ui.html)**

---

## API Endpoints

The base URL for the deployed application is `https://parkinglotmanagement-1.onrender.com`.

| Method | Endpoint                      | Description                               |
| :----- | :---------------------------- | :---------------------------------------- |
| `POST` | `/api/floors`                 | Creates a new parking floor.              |
| `POST` | `/api/floors/{floorId}/slots` | Creates a new slot on a specific floor.   |
| `POST` | `/api/reserve`                | Creates a new reservation.                |
| `GET`  | `/api/availability`           | Gets available slots for a time range.    |
| `GET`  | `/api/reservations/{id}`      | Retrieves a specific reservation.         |
| `DELETE`| `/api/reservations/{id}`      | Cancels a specific reservation.           |

---

## Local Setup & Run Instructions

### Prerequisites
* Java JDK 17 or newer
* Maven
* A running instance of PostgreSQL

### Configuration
1.  **Create Database:** Before running locally, ensure you have a PostgreSQL database created.
    ```sql
    CREATE DATABASE parking_lot_db;
    ```
2.  **Update Credentials:** Open the `src/main/resources/application.properties` file and update the `spring.datasource.url`, `username`, and `password` with your local PostgreSQL credentials.

### Running the Application
1.  **Clone the repository:**
    ```bash
    git clone [https://github.com/lakshmidhar2006/ParkingLotManagement.git](https://github.com/lakshmidhar2006/ParkingLotManagement.git)
    cd ParkingLotManagement
    ```
2.  **Run with Maven:**
    ```bash
    # For Windows (PowerShell)
    .\mvnw spring-boot:run

    # For macOS/Linux
    ./mvnw spring-boot:run
    ```
The application will start on `http://localhost:8080`.