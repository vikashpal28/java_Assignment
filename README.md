# Social Media Interaction API (Spring Boot)

A high-performance Spring Boot microservice acting as an API gateway with integrated guardrails for handling social media interactions (Users, Bots, Posts, Comments, and Likes).

## Features

- **PostgreSQL & Redis Backed Architecture**: Built with robust relational data persistence via PostgreSQL and high-speed data access/concurrency controls via Redis.
- **RESTful API Endpoints**:
  - `POST /user`: Register a new user.
  - `POST /bot`: Register a new bot.
  - `POST /api/posts`: Create a new post.
  - `POST /api/posts/{postId}/comments`: Add a comment to a specific post.
  - `POST /api/posts/{postId}/like`: Add a like to a specific post.
- **Smart Batching Notification Engine**: Includes a scheduled `NotificationSweeper` (runs every 5 minutes) that processes pending push notifications stored in Redis and groups interactions into a summarized push notification.
- **High Concurrency & Guardrails**: Designed to maintain strict statelessness and handle data integrity/race conditions using atomic operations in Redis.

## Tech Stack

- Java 17
- Spring Boot 3.5.14
- Spring Data JPA
- Spring Data Redis
- PostgreSQL
- Lombok
- Docker (services via `services.docker_compose.yml`)
- Maven

## Getting Started

### Prerequisites

- Java 17
- Maven
- PostgreSQL
- Redis

### Build & Run

You can run the application directly from Maven:

```bash
cd Assignment
./mvnw spring-boot:run
```

Ensure that your Redis and PostgreSQL instances are running and configured properly. You can use the provided docker-compose configuration to spin up the required backing services.

## Project Structure

- `com.example.Assignment.controller`: REST API endpoints.
- `com.example.Assignment.service`: Business logic layer.
- `com.example.Assignment.entity`: JPA Entities representing the database schema (`User`, `Bot`, `Post`, `Comment`, `Author`).
- `com.example.Assignment.dto`: Data Transfer Objects for API request and response payloads.
- `com.example.Assignment.repository`: Spring Data JPA and Redis repositories.
- `com.example.Assignment.config`: Application configuration, including cache management and scheduled tasks (`NotificationSweeper`).

## Architecture Details

This project is built to handle highly concurrent scenarios efficiently. The combination of JPA for persistent storage and Redis for state handling (like notifications and rate-limiting) ensures that horizontal scaling and data integrity are maintained. The intelligent notification sweeper groups rapid bot/user actions to avoid spamming the end user.