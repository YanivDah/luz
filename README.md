# Luz

A scheduling application for small businesses.

## Project goals

- Allow customers to book available appointments.
- Allow business owners to manage services and appointments.
- Practice building and understanding a complete web application.

## Planned technology stack

- Frontend: React and TypeScript
- Backend: Java 21 and Spring Boot
- Database: PostgreSQL

## Development environment

- Ubuntu 24.04 through WSL
- Node.js 24
- Java 21

## Current status

- Spring Boot backend with `/api/hello` and `/api/status` endpoints.
- React and TypeScript frontend with a basic Luz welcome page.
- Frontend and backend currently run independently.
- No database connection yet.

## Run locally

Prerequisites: Java 21 and nvm with the Node.js version from `.nvmrc`.

Run these commands from the repository root in separate terminals.

### Backend

cd backend
./mvnw spring-boot:run

Backend endpoint: http://localhost:8080/api/hello

### Frontend

nvm use
cd frontend
npm ci
npm run dev

Open the local URL printed by Vite, usually http://localhost:5173.

## Checks

From the backend directory:
./mvnw test

From the frontend directory:
npm run build
npm run lint