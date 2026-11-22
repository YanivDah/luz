import { useState, useEffect } from 'react';
import type { Service } from '../types/service';

function BookingPage() {

    const [services, setServices] = useState<Service[]>([]);
    useEffect(() => {
        fetch('/api/businesses/1/services')
            .then(response => response.json())
            .then(data => setServices(data));
    }, []);
    const [selectedService, setSelectedService] = useState<number | null>(null);
    const [selectedDate, setSelectedDate] = useState<string>('');

    const today = new Date();
    const maxDate = new Date(today);
    maxDate.setDate(maxDate.getDate() + 30);
    const minDateString = today.toLocaleDateString('en-CA');
    const maxDateString = maxDate.toLocaleDateString('en-CA');

    const [availableSlots, setAvailableSlots] = useState<string[]>([]);
    const [isLoading, setIsLoading] = useState(false);
    const [error, setError] = useState<string | null>(null);

    useEffect(() => {
        if (selectedService === null || selectedDate === '') {
            return;
        }

        const controller = new AbortController();

        fetch(
            `/api/businesses/1/services/${selectedService}/availability?date=${selectedDate}`,
            { signal: controller.signal }
        )
            .then(response => {
                if(!response.ok) {
                    throw new Error('Failed to load availability');
                }
                return response.json();
            })
            .then(data => setAvailableSlots(data))
            .catch(error => {
                if (error.name !== 'AbortError') {
                    console.error(error);
                    setError('Could not load available times. Please try again.');
                    setAvailableSlots([]);
                }
            })
            .finally(() => {
                if (!controller.signal.aborted) {
                    setIsLoading(false);
                }
            });

        return () => controller.abort();

    }, [selectedService, selectedDate]);

    function formatTime(slot: string): string {
        return new Date(slot).toLocaleTimeString('he-IL', {
            timeZone: 'Asia/Jerusalem',
            hour: '2-digit',
            minute: '2-digit',
        })
    }
    const [selectedSlot, setSelectedSlot] = useState<string | null>(null);

    return( 
        <main>
            <h1>Book an appointment</h1>
            <h2>Select a service</h2>
            {services.map(service => (
                <div key={service.id}>
                    <h3>{service.name}</h3>
                    <p>Duration: {service.durationMinutes} minutes</p>
                    <p>Price: ${service.price}</p>
                    <button onClick={() => {
                            setSelectedService(service.id);
                            setSelectedSlot(null);
                            setAvailableSlots([]);
                            setIsLoading(selectedDate !== '');
                            setError(null);}}>
                        {selectedService === service.id ? 'selected' : 'select'}
                    </button>
                </div>
            ))}

            <h2>Select a date</h2>
            <input
                type="date"
                value={selectedDate}
                min={minDateString}
                max={maxDateString}
                onChange={event => {
                    setSelectedDate(event.target.value);
                    setSelectedSlot(null);
                    setAvailableSlots([]);
                   setIsLoading(selectedService !== null && event.target.value !== '');
                    setError(null);
                }}
            />
            <h2>Select a time</h2>
            {isLoading && <p>Loading available times...</p>}
            {error && <p role="alert">{error}</p>}

            {selectedService !== null && selectedDate !== '' && !isLoading && !error && availableSlots.length === 0 && (
                <p>No available times for this service on this date.</p>
            )}
            {availableSlots.map(slot => (
                <button key={slot} onClick={() => setSelectedSlot(slot)}>
                    {formatTime(slot)}
                    {selectedSlot === slot ? ' ✓' : ''}
                </button>
            ))}
            
            <button disabled={selectedService === null || selectedDate === '' || selectedSlot === null}>
                Continue
            </button>
        </main>
    )
}

export default BookingPage;