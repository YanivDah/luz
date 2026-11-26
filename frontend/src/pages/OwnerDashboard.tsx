import { useEffect, useState } from 'react';
import type { CurrentOwner } from '../types/currentOwner';
import { useNavigate } from 'react-router';
import type { OwnerAppointment } from '../types/ownerAppointment';

function OwnerDashboard() {
    const [currentOwner, setCurrentOwner] = useState<CurrentOwner | null>(null);
    const [appointments, setAppointments] = useState<OwnerAppointment[]>([]);
    const [loadedBusinessId, setLoadedBusinessId] = useState<number | null>(null);
    const [appointmentsError, setAppointmentsError] = useState<string | null>(null);
    const [logoutError, setLogoutError] = useState<string | null>(null);
    const navigate = useNavigate();
    
    useEffect(() => {
        const controller = new AbortController();

        fetch('/api/auth/me', {
            signal: controller.signal
        })
            .then(response => {
                if (response.status === 401) {
                    navigate('/signin', { replace: true });
                    return null;
                }
                if (!response.ok) {
                    throw new Error('Failed to load current owner');
                }
                return response.json();
            })
            .then((data: CurrentOwner | null) => {
                if (data !== null && !controller.signal.aborted) {
                setCurrentOwner(data);
                }
            })
            .catch(error => {
                if (error.name !== 'AbortError') {
                    console.error(error);
                }
            });

        return () => controller.abort();
    }, [navigate]);

    useEffect(() => {
        if (currentOwner === null) {
            return;
        }

        const controller = new AbortController();

        fetch(`/api/businesses/${currentOwner.businessId}/appointments`, {
            signal: controller.signal
        })
            .then(response => {
                if (!response.ok) {
                    throw new Error('Failed to load appointments');
                }

                return response.json();
            })
            .then((data: OwnerAppointment[]) => {
                if (!controller.signal.aborted) {
                    setAppointments(data);
                }
            })
            .catch(error => {
                if (error.name !== 'AbortError') {
                    console.error(error);
                    setAppointmentsError('Could not load appointments. Please try again.');
                }
            })
            .finally(() => {
                if (!controller.signal.aborted) {
                    setLoadedBusinessId(currentOwner?.businessId);
                }
            });

        return () => controller.abort();
    }, [currentOwner]);

    function formatDateTime(instant: string): string {
        return new Date(instant).toLocaleString('en-GB', {
            timeZone: 'Asia/Jerusalem',
            dateStyle: 'medium',
            timeStyle: 'short'
        });
    }

    const isLoadingAppointments =
        currentOwner !== null &&
        loadedBusinessId !== currentOwner.businessId;
    
    async function handleLogout() {
        setLogoutError(null);
        try {
            const csrfResponse = await fetch('/api/csrf');

            if (!csrfResponse.ok) {
                throw new Error('Failed to fetch CSRF token');
            }

            const csrfData = await csrfResponse.json();

            const logoutResponse = await fetch('/logout', {
                method: 'POST',
                headers: {
                    [csrfData.headerName]: csrfData.token
                }
            });

            if (!logoutResponse.ok) {
                throw new Error('Logout failed');
            }

            navigate('/signin', { replace: true });

        } catch (error) {
            console.error('Logout failed:', error);
            setLogoutError('Could not logout. Please try again.');
        }
    }

    return (
        <main>
            <h1>Owner Dashboard</h1>
            <button type="button" onClick={handleLogout}>
                Logout
            </button>
            {logoutError && <p role="alert">{logoutError}</p>}
            {currentOwner && ( <p>Logged in as: {currentOwner.email}</p> )}
            <h2>Appointments</h2>
            <p>Appointments loaded: {appointments.length}</p>
            {isLoadingAppointments && <p>Loading appointments...</p>}

            {appointmentsError && <p role="alert">{appointmentsError}</p>}
            {!isLoadingAppointments &&
            !appointmentsError &&
            loadedBusinessId === currentOwner?.businessId &&
            appointments.length === 0 && (
                <p>No appointments yet.</p>
            )}

            {!isLoadingAppointments && !appointmentsError &&
            appointments.map(appointment => (
                <div key={appointment.id}>
                    <h3>{appointment.customerName}</h3>
                    <p>Service: {appointment.serviceName}</p>
                    <p>Phone: {appointment.customerPhone}</p>
                    <p>Status: {appointment.status}</p>
                    <p>Start Time: {formatDateTime(appointment.startTime)}</p>
                    <p>End Time: {formatDateTime(appointment.endTime)}</p>
                </div>
            ))}
        </main>
    );
}

export default OwnerDashboard;