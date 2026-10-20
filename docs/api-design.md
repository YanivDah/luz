
# Luz — REST API Design

Status: Draft

Base URL: /api

## 1. Public Booking API

These endpoints do not require customer login.

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | /businesses/{businessId}/services | List active services |
| GET | /businesses/{businessId}/services/{serviceId}/availability?date=YYYY-MM-DD | Get available time slots |
| POST | /businesses/{businessId}/appointments | Create an appointment |

### Create Appointment

Request:

POST /api/businesses/{businessId}/appointments

Example JSON body:

```json
{
  "serviceId": 5,
  "startTime": "2026-10-22T10:00:00+03:00",
  "customerName": "Yoav Cohen",
  "customerEmail": "yoav@example.com"
}
```

On success:
- Return 201 Created.
- Return the appointment ID, service, date, time,
  price and status.

On failure:
- 400 Bad Request: Invalid input.
- 404 Not Found: Business or service not found.
- 409 Conflict: Time slot no longer available.

## 2. Business Owner API

These endpoints require authentication and authorization.

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | /owner/appointments | View business appointments |
| PATCH | /owner/appointments/{id} | Cancel an appointment |
| GET | /owner/services | List all business services |
| POST | /owner/services | Add a service |
| PATCH | /owner/services/{id} | Update or deactivate a service |
| GET | /owner/working-hours | Get weekly working hours |
| PUT | /owner/working-hours | Replace weekly working hours |
| GET | /owner/availability-exceptions | List date exceptions |
| POST | /owner/availability-exceptions | Create an exception |
| DELETE | /owner/availability-exceptions/{id} | Remove an exception |

Business owners can access only their own business data.
The backend determines the business from the
authenticated owner, not from a client-supplied ID.

## 3. General API Rules

- All request and response bodies use JSON.
- The backend validates incoming data.
- Business logic belongs in the Service layer.
- Database constraints provide additional protection.
- Appointment creation must be transactional.
- API errors should return appropriate HTTP status codes.
