
# Luz — User Flows

## 1. Customer Booking Flow

1. Customer opens the business's public booking page.
2. System displays active services with name, duration, and price.
3. Customer selects a service.
4. System displays available dates and time slots.
5. Customer selects a date and time.
6. Customer enters their name and email.
7. Customer reviews the booking details and confirms.
8. System validates the request and rechecks availability.
9. If successful:
   - A confirmed appointment is created.
   - A confirmation screen is displayed.
   - The appointment becomes visible to the business owner.
10. If unsuccessful:
    - An error message is displayed.
    - The customer can select another time slot.

## 2. Business Owner Flow

1. Business owner logs in.
2. System displays the owner's business dashboard.
3. Owner can navigate to:
   - Appointments: view and cancel appointments.
   - Services: create, edit, and deactivate services.
   - Working Hours: configure recurring weekly hours.
   - Availability Exceptions: block dates, block time ranges, or override working hours on specific dates.
4. System validates changes before saving them.
5. Changes that conflict with future confirmed appointments are rejected.

## 3. MVP Boundaries

- One owner, one service provider, and one calendar per business.
- Multiple businesses with isolated data.
- No customer accounts.
- No online payments.
- No email or SMS notifications.
- Appointment management uses a list, not a graphical weekly calendar.
- Time slots start at 15-minute intervals.
