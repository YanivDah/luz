import { useState } from 'react';

function LoginPage() {
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [loginError, setLoginError] = useState<string | null>(null);
    const [isSubmitting, setIsSubmitting] = useState(false);
    const [isLoggedIn, setIsLoggedIn] = useState(false);

    async function handleSubmit(event: React.SubmitEvent<HTMLFormElement>) {
        event.preventDefault();
        if (isSubmitting) {
            return;
        }
        setIsSubmitting(true);

        try {
        const csrfResponse = await fetch('/api/csrf');
        const formData = new URLSearchParams();

        formData.append('username', email);
        formData.append('password', password);

        if (!csrfResponse.ok) {
            throw new Error('Failed to fetch CSRF token');
        }

        const csrfData = await csrfResponse.json();

        const loginResponse = await fetch('/login', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded',
                [csrfData.headerName]: csrfData.token
            },
            body: formData
        });

        if (loginResponse.status === 200) {
            setLoginError(null);
            setIsLoggedIn(true);
            console.log('Login successful');
        } else if (loginResponse.status === 401) {
            setLoginError('Invalid email or password');
        } else {
            setLoginError('Login failed. Please try again.');
        } 
        } catch (error) {
                console.error('Login request failed:', error);
                setLoginError('Unable to connect to the server. Please try again.');
            } finally {
                setIsSubmitting(false);
            }
    }

    if (isLoggedIn) {
        return (
            <main>
                <h1>Welcome to Luz!</h1>
                <p>You are successfully logged in.</p>
            </main>
        );
    }
    return (
        <main>
            <h1>Business Owner Login</h1>

            <form onSubmit={handleSubmit}>
                <div>
                    <label htmlFor="email">Email</label>
                    <input
                        id="email"
                        type="email"
                        value={email}
                        onChange={event => setEmail(event.target.value)}
                        required
                    />
                </div>

                <div>
                    <label htmlFor="password">Password</label>
                    <input
                        id="password"
                        type="password"
                        value={password}
                        onChange={event => setPassword(event.target.value)}
                        required
                    />
                </div>

                <button 
                type="submit"
                disabled={!(email.trim() || !password || isSubmitting)}>
                    {isSubmitting ? 'Logging in...' : 'Login'}
                </button>

                {loginError && <p role="alert">{loginError}</p>}
            </form>
        </main>
    );
}

export default LoginPage;