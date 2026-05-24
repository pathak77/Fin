import React, { useState, useEffect } from 'react';
import { getTransactionsByGiver, getFriends, createTransaction, updateTransactionStatus } from '../services/api';
import { Check, Clock, Plus, DollarSign } from 'lucide-react';

const TransactionList = ({ currentUser }) => {
  const [transactions, setTransactions] = useState([]);
  const [friends, setFriends] = useState([]);
  const [loading, setLoading] = useState(true);
  
  const [newAmount, setNewAmount] = useState('');
  const [selectedFriendId, setSelectedFriendId] = useState('');

  useEffect(() => {
    fetchData();
  }, [currentUser]);

  const fetchData = async () => {
    try {
      setLoading(true);
      const [transRes, friendsRes] = await Promise.all([
        getTransactionsByGiver(currentUser.userId),
        getFriends(currentUser.userId)
      ]);
      setTransactions(transRes.data);
      const userFriends = friendsRes.data.friendsList || [];
      setFriends(userFriends);
      if (userFriends.length > 0) {
        setSelectedFriendId(userFriends[0].userId);
      }
    } catch (error) {
      console.error("Failed to fetch transactions", error);
    } finally {
      setLoading(false);
    }
  };

  const handleCreateTransaction = async (e) => {
    e.preventDefault();
    if (!selectedFriendId || !newAmount) return;
    try {
      await createTransaction(currentUser.userId, selectedFriendId, parseFloat(newAmount));
      setNewAmount('');
      await fetchData();
    } catch (error) {
      console.error("Failed to create transaction", error);
    }
  };

  const handleMarkPaid = async (ledgerId) => {
    try {
      await updateTransactionStatus(ledgerId, true);
      await fetchData();
    } catch (error) {
      console.error("Failed to update status", error);
    }
  };

  if (loading) {
    return <div>Loading transactions...</div>;
  }

  return (
    <div>
      <div className="flex-between" style={{ marginBottom: '2rem' }}>
        <div>
          <h1>Transactions</h1>
          <p className="text-secondary">Manage your lent money</p>
        </div>
      </div>

      <div className="grid-cards">
        <div className="glass-panel" style={{ height: 'fit-content' }}>
          <h3 style={{ marginBottom: '1.5rem' }}>New Transaction</h3>
          <form onSubmit={handleCreateTransaction}>
            <div className="input-group">
              <label className="input-label">Select Friend</label>
              <select 
                className="input-field"
                value={selectedFriendId}
                onChange={(e) => setSelectedFriendId(e.target.value)}
              >
                {friends.length === 0 && <option value="">No friends available</option>}
                {friends.map(f => (
                  <option key={f.userId} value={f.userId}>{f.username}</option>
                ))}
              </select>
            </div>
            <div className="input-group">
              <label className="input-label">Amount ($)</label>
              <input 
                type="number" 
                step="0.01"
                min="0"
                className="input-field" 
                value={newAmount}
                onChange={(e) => setNewAmount(e.target.value)}
                placeholder="0.00"
              />
            </div>
            <button type="submit" className="btn btn-primary" style={{ width: '100%', marginTop: '1rem' }} disabled={friends.length === 0}>
              <Plus size={18} />
              Add Transaction
            </button>
          </form>
        </div>

        <div className="glass-panel" style={{ gridColumn: 'span 2' }}>
          <h3 style={{ marginBottom: '1.5rem' }}>Recent Transactions</h3>
          {transactions.length === 0 ? (
            <p className="text-secondary">No transactions found.</p>
          ) : (
            <div style={{ display: 'flex', flexDirection: 'column', gap: '1rem' }}>
              {transactions.sort((a, b) => new Date(b.createdAt) - new Date(a.createdAt)).map(t => (
                <div key={t.ledgerId} className="transaction-item glass-panel" style={{ padding: '1.25rem' }}>
                  <div style={{ display: 'flex', gap: '1rem', alignItems: 'center' }}>
                    <div style={{ padding: '0.75rem', background: 'rgba(139, 92, 246, 0.1)', borderRadius: 'var(--radius-sm)' }}>
                      <DollarSign className="text-accent-primary" size={24} />
                    </div>
                    <div className="transaction-info">
                      <h4>To: User #{t.receiverId}</h4>
                      <p>{new Date(t.createdAt).toLocaleString()}</p>
                    </div>
                  </div>
                  
                  <div style={{ display: 'flex', alignItems: 'center', gap: '1.5rem' }}>
                    <div style={{ textAlign: 'right' }}>
                      <div style={{ fontSize: '1.25rem', fontWeight: 600 }}>${t.amount.toFixed(2)}</div>
                      <div className={`badge ${t.paid ? 'badge-success' : 'badge-warning'}`} style={{ marginTop: '0.25rem', display: 'inline-block' }}>
                        {t.paid ? 'Settled' : 'Pending'}
                      </div>
                    </div>
                    
                    {!t.paid && (
                      <button 
                        className="btn btn-outline" 
                        onClick={() => handleMarkPaid(t.ledgerId)}
                        title="Mark as paid"
                      >
                        <Check size={18} className="text-success" />
                      </button>
                    )}
                  </div>
                </div>
              ))}
            </div>
          )}
        </div>
      </div>
    </div>
  );
};

export default TransactionList;
