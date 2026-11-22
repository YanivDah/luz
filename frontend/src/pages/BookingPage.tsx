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
    const [bookingStep, setBookingStep] = useState<1 | 2>(1);
    const [customerName, setCustomerName] = useState<string>('');
    const [customerPhone, setCustomerPhone] = useState<string>('');
    const [bookingConfirmed, setBookingConfirmed] = useState(false);
    const [isSubmitting, setIsSubmitting] = useState(false);
    const [bookingError, setBookingError] = useState<string | null>(null);
    const [availabilityRefresh, setAvailabilityRefresh] = useState(0);

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

    }, [selectedService, selectedDate, availabilityRefresh]);

    function formatTime(slot: string): string {
        return new Date(slot).toLocaleTimeString('he-IL', {
            timeZone: 'Asia/Jerusalem',
            hour: '2-digit',
            minute: '2-digit',
        })
    }
    const [selectedSlot, setSelectedSlot] = useState<string | null>(null);

    const phoneDigits = customerPhone.replace(/\D/g, '');
    const isPhoneValid = phoneDigits.length >= 9;

    async function handleBooking(){
        if(selectedService === null || selectedSlot === null || isSubmitting) {
            return;
        }
        setBookingError(null);
        setIsSubmitting(true);

        try {
        const response = await fetch('/api/businesses/1/appointments', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({
                serviceId: selectedService,
                startTime: selectedSlot,
                customerName: customerName.trim(),
                customerPhone: customerPhone.trim(),
            }),
        });
        if (response.status === 201) {
        setBookingConfirmed(true);
        } else if (response.status === 409) {
            setBookingError('This time slot is no longer available. Please choose another time.');
            setSelectedSlot(null);
            setAvailableSlots([]);
            setIsLoading(true);
            setBookingStep(1);
            setAvailabilityRefresh(prev => prev + 1);
        } else {
            setBookingError('Could not complete the booking. Please try again.');
        }
        } catch (error) {
            console.error(error);
            setBookingError('Unable to connect to the server. Please try again.');
        } finally { 
            setIsSubmitting(false);
        }
    }

    return( 
        <main>

            {bookingConfirmed ? (
                <section>
                    <h1>Booking confirmed!</h1>
                    <p>Your appointment has been successfully booked.</p>
                    <p>Date: {selectedDate}</p>
                    <p>Time: {selectedSlot ? formatTime(selectedSlot) : ''}</p>
                </section>
            ) : (
                <>

            {bookingStep === 1 ? (
                <>
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
            {bookingError && <p role="alert">{bookingError}</p>}
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

            <button 
            disabled={selectedService === null || selectedDate === '' || selectedSlot === null}
            onClick={() => setBookingStep(2)}>
                Continue
            </button>
            
            </>
            ) : (
                <section> 
                    <h1>Complete your booking</h1>
                    <p>Enter your details to confirm the appointment.</p>
                    <input type="text"
                    placeholder="Your Name"
                    value={customerName}
                    onChange={event => setCustomerName(event.target.value)} />

                    <input type="tel" 
                    placeholder="Your Phone Number" 
                    value={customerPhone}
                    onChange={event => setCustomerPhone(event.target.value)} />  
                      
                    <button onClick={() => setBookingStep(1)}>
                        Back
                    </button>

                    <button
                    disabled={customerName.trim() === '' || isPhoneValid === false || isSubmitting}
                    onClick={handleBooking}>
                    {isSubmitting ? 'Booking...' : 'Confirm Booking'}
                    </button>

                    {bookingError && <p role="alert">{bookingError}</p>}

                </section>
            )}

                </> 
            )}

        </main>
    )
}

export default BookingPage;