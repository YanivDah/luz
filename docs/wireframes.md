
# Luz — Wireframes

## Customer Screens

### Screen 1 — Service Selection

Purpose: Allow customers to select a service offered by the business.

```text
+--------------------------------------+
| LUZ              Daniel's Barbershop |
+--------------------------------------+
|                                      |
| Choose a service                     |
|                                      |
| +----------------------------------+ |
| | Haircut                          | |
| | 30 minutes                 80 ILS| |
| +----------------------------------+ |
|                                      |
| +----------------------------------+ |
| | Haircut + Beard                  | |
| | 45 minutes                110 ILS| |
| +----------------------------------+ |
|                                      |
| +----------------------------------+ |
| | Long Haircut                     | |
| | 60 minutes                140 ILS| |
| +----------------------------------+ |
|                                      |
+--------------------------------------+
```

Behavior:
- Displays only active services belonging to the selected business.
- Shows service name, duration, and price.
- Selecting a service navigates to the date and time selection screen.

### Screen 2 — Date & Time Selection

Purpose: Allow customers to select an available appointment.

```text
+--------------------------------------+
| LUZ              Daniel's Barbershop |
+--------------------------------------+
|                                      |
| Haircut + Beard                      |
| 45 minutes                    110 ILS|
|                                      |
| Choose a date                        |
|                                      |
| < Previous week       Next week >    |
|                                      |
| Mon   Tue   Wed   Thu   Fri          |
|  19    20    21    22    23          |
|                                      |
| Available times                      |
|                                      |
| +--------+ +--------+ +--------+     |
| | 09:00  | | 09:15  | | 10:30  |     |
| +--------+ +--------+ +--------+     |
|                                      |
| +--------+ +--------+                |
| | 11:00  | | 13:30  |                |
| +--------+ +--------+                |
|                                      |
|            [ Continue ]              |
+--------------------------------------+
```

Behavior:
- Displays one week at a time.
- Allows navigation up to 30 days ahead.
- Shows only available starting times.
- Available slots are calculated using the
  selected service's duration.
- Slots follow 15-minute starting intervals.
- The customer selects a date and time.
- Continue opens the booking details screen.

### Screen 3 — Booking Confirmation

Purpose: Collect customer details and create an appointment.

```text
+--------------------------------------+
| LUZ              Daniel's Barbershop |
+--------------------------------------+
|                                      |
| Confirm your booking                 |
|                                      |
| Service: Haircut + Beard             |
| Date:    October 20, 2026            |
| Time:    09:00 - 09:45               |
| Price:   110 ILS                     |
|                                      |
| Full name                            |
| +----------------------------------+ |
| |                                  | |
| +----------------------------------+ |
|                                      |
| Email address                        |
| +----------------------------------+ |
| |                                  | |
| +----------------------------------+ |
|                                      |
|          [ Confirm Booking ]         |
+--------------------------------------+
```

Behavior:
- Displays the selected booking details.
- Requires a valid name and email address.
- Submits the booking request to the backend.
- The backend rechecks all availability rules.
- On success, displays a confirmation screen
  with a unique booking reference.
- On failure, displays an error and allows
  selecting another time without losing
  the customer's information.
- No online payment is collected.

## Business Owner Screens

### Screen 4 — Login

Purpose: Allow the business owner to access their management area.

```text
+--------------------------------------+
|                 LUZ                  |
|                                      |
|          Business Owner Login        |
|                                      |
| Email                                |
| [                                  ] |
|                                      |
| Password                             |
| [                                  ] |
|                                      |
|              [ Log In ]              |
+--------------------------------------+
```

Behavior:
- Requires email and password.
- Displays an error for invalid credentials.
- Grants access only to the owner's business.

### Screen 5 — Dashboard & Appointments

Purpose: View and manage appointments.

```text
+--------------------------------------------------+
| LUZ                  Daniel's Barbershop | Logout|
+--------------------------------------------------+
| Appointments | Services | Hours | Exceptions     |
+--------------------------------------------------+
|                                                  |
| Appointments                                     |
| Date: [ October 20, 2026 ]                      |
|                                                  |
| Time   Customer       Service         Status     |
| ------------------------------------------------ |
| 09:00  Yoav Cohen     Haircut         Confirmed  |
| 10:30  Dana Levi      Haircut+Beard   Confirmed  |
| 12:00  Itay Bar       Haircut         Cancelled  |
|                                                  |
| [ View Details ]   [ Cancel Appointment ]        |
+--------------------------------------------------+
```

Behavior:
- Displays appointments for a selected date.
- Shows customer details, service and status.
- Allows cancellation of confirmed appointments.
- Cancelled appointments remain in history.
- Cancelled appointments no longer block availability.

### Screen 6 — Service Management

Purpose: Manage the business's services.

```text
+--------------------------------------------+
| Services                      [ Add Service ]|
+--------------------------------------------+
| Name              Duration   Price   Status |
| ------------------------------------------ |
| Haircut           30 min     80 ILS  Active |
| Haircut + Beard   45 min    110 ILS  Active |
|                                            |
| [ Edit ] [ Deactivate ]                    |
+--------------------------------------------+
```

Behavior:
- Owner can add, edit and deactivate services.
- Each service has a name, duration and price.
- Duration is 15–180 minutes in multiples of 15.
- Existing appointments retain their original
  duration and price after service updates.

### Screen 7 — Working Hours

Purpose: Configure recurring weekly availability.

```text
+----------------------------------------+
| Working Hours                          |
+----------------------------------------+
| Monday     [09:00] - [17:00]  [Open]   |
| Tuesday    [09:00] - [17:00]  [Open]   |
| Wednesday  [09:00] - [17:00]  [Open]   |
| Thursday   [09:00] - [17:00]  [Open]   |
| Friday     [09:00] - [14:00]  [Open]   |
| Saturday                       [Closed] |
| Sunday     [09:00] - [17:00]  [Open]   |
|                                        |
|               [ Save ]                 |
+----------------------------------------+
```

Behavior:
- Owner configures weekly working hours.
- Closed days have no available booking slots.
- Changes conflicting with future confirmed
  appointments are rejected.

### Screen 8 — Availability Exceptions

Purpose: Override availability for specific dates.

```text
+----------------------------------------+
| Availability Exceptions                |
+----------------------------------------+
| Date:       [ 2026-10-25 ]             |
|                                        |
| Action:                                |
| ( ) Close entire day                   |
| ( ) Block specific hours               |
| ( ) Override working hours             |
|                                        |
| Start:      [ 12:00 ]                  |
| End:        [ 16:00 ]                  |
|                                        |
|           [ Save Exception ]           |
+----------------------------------------+
```

Behavior:
- Owner can close a specific date.
- Owner can block a specific time range.
- Owner can override working hours for one date.
- Recurring weekly hours remain unchanged.
- Changes conflicting with future confirmed
  appointments are rejected.



