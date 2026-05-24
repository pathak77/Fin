import React, { useState, useEffect } from 'react';
import { getTransactionsByGiver } from '../services/api';
import { 
  BarChart, Bar, XAxis, YAxis, CartesianGrid, Tooltip, ResponsiveContainer,
  LineChart, Line
} from 'recharts';
import { Wallet, ArrowUpRight, ArrowDownRight, Activity } from 'lucide-react';

const Dashboard = ({ currentUser }) => {
  const [transactions, setTransactions] = [useState([]), useState([])][0];
  const [givenTransactions, setGivenTransactions] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    fetchData();
  }, [currentUser]);

  const fetchData = async () => {
    try {
      setLoading(true);
      // Fetch given transactions
      const res = await getTransactionsByGiver(currentUser.userId);
      setGivenTransactions(res.data);
    } catch (error) {
      console.error("Failed to fetch dashboard data", error);
    } finally {
      setLoading(false);
    }
  };

  if (loading) {
    return <div>Loading dashboard...</div>;
  }

  // Calculate stats
  const totalGiven = givenTransactions.reduce((acc, t) => acc + t.amount, 0);
  const totalPaid = givenTransactions.filter(t => t.paid).reduce((acc, t) => acc + t.amount, 0);
  const totalPending = totalGiven - totalPaid;

  // Process data for charts
  const chartData = givenTransactions.reduce((acc, t) => {
    const date = new Date(t.createdAt).toLocaleDateString();
    const existing = acc.find(item => item.date === date);
    if (existing) {
      existing.amount += t.amount;
    } else {
      acc.push({ date, amount: t.amount });
    }
    return acc;
  }, []).sort((a, b) => new Date(a.date) - new Date(b.date));

  return (
    <div>
      <div className="flex-between" style={{ marginBottom: '2rem' }}>
        <div>
          <h1>Dashboard</h1>
          <p className="text-secondary">Overview of your expenditures</p>
        </div>
        <div className="glass-panel" style={{ padding: '0.75rem 1.5rem', display: 'flex', alignItems: 'center', gap: '1rem' }}>
          <Activity className="text-accent-primary" />
          <span style={{ fontWeight: 600 }}>Active Status</span>
        </div>
      </div>

      <div className="grid-cards">
        <div className="glass-panel">
          <div className="flex-between" style={{ marginBottom: '1rem' }}>
            <h4 className="text-secondary">Total Lent</h4>
            <div style={{ padding: '0.5rem', background: 'rgba(139, 92, 246, 0.1)', borderRadius: 'var(--radius-sm)' }}>
              <ArrowUpRight className="text-accent-primary" size={20} />
            </div>
          </div>
          <h2 style={{ fontSize: '2.5rem' }}>${totalGiven.toFixed(2)}</h2>
        </div>

        <div className="glass-panel">
          <div className="flex-between" style={{ marginBottom: '1rem' }}>
            <h4 className="text-secondary">Settled Amount</h4>
            <div style={{ padding: '0.5rem', background: 'rgba(16, 185, 129, 0.1)', borderRadius: 'var(--radius-sm)' }}>
              <Wallet className="text-success" size={20} />
            </div>
          </div>
          <h2 style={{ fontSize: '2.5rem' }}>${totalPaid.toFixed(2)}</h2>
        </div>

        <div className="glass-panel">
          <div className="flex-between" style={{ marginBottom: '1rem' }}>
            <h4 className="text-secondary">Pending Returns</h4>
            <div style={{ padding: '0.5rem', background: 'rgba(239, 68, 68, 0.1)', borderRadius: 'var(--radius-sm)' }}>
              <ArrowDownRight className="text-danger" size={20} />
            </div>
          </div>
          <h2 style={{ fontSize: '2.5rem' }}>${totalPending.toFixed(2)}</h2>
        </div>
      </div>

      <div className="glass-panel" style={{ height: '400px', marginBottom: '2rem' }}>
        <h3 style={{ marginBottom: '1.5rem' }}>Expenditure Trend</h3>
        <ResponsiveContainer width="100%" height="100%">
          <BarChart data={chartData}>
            <CartesianGrid strokeDasharray="3 3" stroke="rgba(255,255,255,0.1)" />
            <XAxis dataKey="date" stroke="#A1A1AA" />
            <YAxis stroke="#A1A1AA" />
            <Tooltip 
              contentStyle={{ background: '#121214', border: '1px solid rgba(255,255,255,0.1)', borderRadius: '8px' }}
              itemStyle={{ color: '#F8F9FA' }}
            />
            <Bar dataKey="amount" fill="url(#colorGradient)" radius={[4, 4, 0, 0]} />
            <defs>
              <linearGradient id="colorGradient" x1="0" y1="0" x2="0" y2="1">
                <stop offset="0%" stopColor="#8B5CF6" stopOpacity={1}/>
                <stop offset="100%" stopColor="#3B82F6" stopOpacity={0.8}/>
              </linearGradient>
            </defs>
          </BarChart>
        </ResponsiveContainer>
      </div>
    </div>
  );
};

export default Dashboard;
