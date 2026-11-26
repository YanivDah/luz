import './App.css'
import { BrowserRouter, Routes, Route } from 'react-router';
import BookingPage from './pages/BookingPage'
import LoginPage from './pages/LoginPage';
import OwnerDashboard from './pages/OwnerDashboard';

function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/book" element={<BookingPage />} />
        <Route path="/signin" element={<LoginPage />} />
        <Route path="/dashboard" element={<OwnerDashboard />} />
      </Routes>
    </BrowserRouter>
  );
}

export default App  
