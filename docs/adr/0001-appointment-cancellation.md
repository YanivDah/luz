
# ADR-0001: Preserve Cancelled Appointments

Status: Accepted

Date: 2026-10-19

## Context

Luz allows business owners to cancel appointments.

We need to decide whether cancelled appointments
should be deleted or preserved in the database.

## Decision

Appointments will not be deleted when cancelled.

Instead, their status will change from
CONFIRMED to CANCELLED.

Cancelled appointments will remain in the database
but will no longer block availability.

## Alternatives Considered

Permanently delete cancelled appointments.

This approach is simpler but removes booking history.

## Consequences

Positive:
- Booking history is preserved.
- Business owners can review past cancellations.
- Appointments retain their unique identifiers.

Negative:
- Availability calculations must ignore cancelled appointments.
- The database will store cancelled appointments as well.

## Superseded By

None.
