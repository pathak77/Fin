import React, { useState } from 'react';
import { BrowserRouter as Router, Routes, Route, Link, useLocation } from 'react-router-dom';
import { LayoutDashboard, Users, CreditCard, LogOut } from 'lucide-react';
import MockLogin from './components/MockLogin';
import Dashboard from './components/Dashboard';
import FriendsList from './components/FriendsList';
import TransactionList from './components/TransactionList';

const Sidebar = ({ currentUser, onLogout }) => {
  const location = useLocation();

  const isActive = (path) => {
    return location.pathname === path ? 'active' : '';
  };

  return (
    <div className="sidebar">
      <div>
        <h2 className="text-gradient">FinApp</h2>
        <p className="text-secondary" style={{ fontSize: '0.875rem', marginTop: '0.5rem' }}>
          Welcome, {currentUser.username}
        </p>
      </div>

      <nav style={{ flex: 1, display: 'flex', flexDirection: 'column', gap: '0.5rem' }}>
        <Link to="/" className={`nav-item ${isActive('/')}`}>
          <LayoutDashboard size={20} />
          Dashboard
        </Link>
        <Link to="/transactions" className={`nav-item ${isActive('/transactions')}`}>
          <CreditCard size={20} />
          Transactions
        </Link>
        <Link to="/friends" className={`nav-item ${isActive('/friends')}`}>
          <Users size={20} />
          Friends
        </Link>
      </nav>

      <button className="btn btn-outline" onClick={onLogout} style={{ width: '100%' }}>
        <LogOut size={18} />
        Logout
      </button>
    </div>
  );
};

function App() {
  const [currentUser, setCurrentUser] = useState(null);

  if (!currentUser) {
    return <MockLogin onLogin={setCurrentUser} />;
  }

  return (
    <Router>
      <div className="app-container">
        <Sidebar currentUser={currentUser} onLogout={() => setCurrentUser(null)} />
        <main className="main-content animate-fade-in">
          <Routes>
            <Route path="/" element={<Dashboard currentUser={currentUser} />} />
            <Route path="/transactions" element={<TransactionList currentUser={currentUser} />} />
            <Route path="/friends" element={<FriendsList currentUser={currentUser} />} />
          </Routes>
        </main>
      </div>
    </Router>
  );
}

export default App;
