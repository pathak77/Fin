import React, { useState, useEffect } from 'react';
import { getUsers, createUser } from '../services/api';
import { User, LogIn, UserPlus } from 'lucide-react';

const MockLogin = ({ onLogin }) => {
  const [users, setUsers] = useState([]);
  const [loading, setLoading] = useState(true);
  const [newUsername, setNewUsername] = useState('');
  const [newEmail, setNewEmail] = useState('');

  useEffect(() => {
    fetchUsers();
  }, []);

  const fetchUsers = async () => {
    try {
      const response = await getUsers();
      setUsers(response.data);
    } catch (error) {
      console.error("Failed to fetch users:", error);
    } finally {
      setLoading(false);
    }
  };

  const handleCreateUser = async (e) => {
    e.preventDefault();
    if (!newUsername || !newEmail) return;
    try {
      const res = await createUser({ username: newUsername, email: newEmail });
      await fetchUsers();
      onLogin(res.data);
    } catch (error) {
      console.error("Failed to create user:", error);
    }
  };

  if (loading) {
    return <div className="app-container" style={{ justifyContent: 'center', alignItems: 'center' }}><p>Loading...</p></div>;
  }

  return (
    <div className="app-container" style={{ justifyContent: 'center', alignItems: 'center' }}>
      <div className="glass-panel" style={{ width: '100%', maxWidth: '400px' }}>
        <h2 className="text-gradient" style={{ textAlign: 'center', marginBottom: '2rem' }}>Welcome to FinApp</h2>
        
        {users.length > 0 && (
          <div style={{ marginBottom: '2rem' }}>
            <h4 style={{ marginBottom: '1rem', color: 'var(--text-secondary)' }}>Select Existing User</h4>
            <div style={{ display: 'flex', flexDirection: 'column', gap: '0.5rem' }}>
              {users.map(user => (
                <button 
                  key={user.userId} 
                  className="btn btn-outline" 
                  style={{ width: '100%', justifyContent: 'flex-start' }}
                  onClick={() => onLogin(user)}
                >
                  <User size={18} />
                  {user.username}
                </button>
              ))}
            </div>
          </div>
        )}

        <div>
          <h4 style={{ marginBottom: '1rem', color: 'var(--text-secondary)' }}>Or Create New User</h4>
          <form onSubmit={handleCreateUser}>
            <div className="input-group">
              <label className="input-label">Username</label>
              <input 
                type="text" 
                className="input-field" 
                value={newUsername} 
                onChange={(e) => setNewUsername(e.target.value)} 
                placeholder="Enter username"
              />
            </div>
            <div className="input-group">
              <label className="input-label">Email</label>
              <input 
                type="email" 
                className="input-field" 
                value={newEmail} 
                onChange={(e) => setNewEmail(e.target.value)} 
                placeholder="Enter email address"
              />
            </div>
            <button type="submit" className="btn btn-primary" style={{ width: '100%', marginTop: '1rem' }}>
              <UserPlus size={18} />
              Create & Login
            </button>
          </form>
        </div>
      </div>
    </div>
  );
};

export default MockLogin;
