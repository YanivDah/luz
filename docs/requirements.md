
# Luz — MVP Requirements

## Customer Booking Flow

### FR-01 — Service Selection
When a customer visits a business's public booking page, the system displays all active services offered by that business, including:
- Service name
- Duration in minutes
- Price in ILS

### FR-02 — Available Time Slots
After selecting a service, the system displays available time slots, one week at a time, up to 30 days in advance.

A time slot is available only if:
- It starts on a 15-minute interval.
- The entire service fits within the business's working hours.
- It does not overlap with an existing confirmed appointment.
- The business is not closed on the selected date.
- The appointment starts at least 60 minutes from the current time.

### FR-03 — Booking Confirmation
After selecting a service and an available time slot, the customer enters their name and email address.

When the customer confirms the booking, the system:
- Validates the customer's information.
- Rechecks availability and prevents overlapping bookings, including concurrent requests.
- Creates a confirmed appointment if the slot is still available.
- Displays a confirmation with the appointment details.
- Displays an appropriate error if the booking fails, allowing the customer to select another time slot without re-entering their information.

The confirmed appointment becomes visible to the business owner.

Email and SMS notifications are outside the MVP scope.

## Business Owner Management

### FR-04 — Service Management
The business owner can create, edit, and deactivate services.

Each service has a name and a duration between 15 and 180 minutes, in multiples of 15.

Changes to a service's duration do not affect existing appointments.

### FR-05 — Working Hours
The business owner can configure weekly working hours and override them for specific dates.

### FR-06 — Availability Management
The business owner can block specific time periods or entire days.

Changes that conflict with future confirmed appointments are rejected.

### FR-07 — Appointment Management
The business owner can view appointments and cancel confirmed appointments.

Cancelled appointments remain in the database with the status CANCELLED and no longer block availability.

### FR-08 — Business Owner Authentication
The business owner can log in securely and manage only their own business.
