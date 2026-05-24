import React, { useState, useEffect } from 'react';
import { getFriends, addFriend, removeFriend, getUsers } from '../services/api';
import { UserMinus, UserPlus, Users } from 'lucide-react';

const FriendsList = ({ currentUser }) => {
  const [friends, setFriends] = useState([]);
  const [allUsers, setAllUsers] = useState([]);
  const [loading, setLoading] = useState(true);
  const [selectedUserId, setSelectedUserId] = useState('');

  useEffect(() => {
    fetchData();
  }, [currentUser]);

  const fetchData = async () => {
    try {
      setLoading(true);
      const [friendsRes, usersRes] = await Promise.all([
        getFriends(currentUser.userId),
        getUsers()
      ]);
      setFriends(friendsRes.data.friendsList || []);
      
      // Filter out self and existing friends
      const existingFriendIds = (friendsRes.data.friendsList || []).map(f => f.userId);
      const available = usersRes.data.filter(u => 
        u.userId !== currentUser.userId && !existingFriendIds.includes(u.userId)
      );
      setAllUsers(available);
      if (available.length > 0) {
        setSelectedUserId(available[0].userId);
      }
    } catch (error) {
      console.error("Failed to fetch friends data", error);
    } finally {
      setLoading(false);
    }
  };

  const handleAddFriend = async (e) => {
    e.preventDefault();
    if (!selectedUserId) return;
    try {
      await addFriend(currentUser.userId, selectedUserId);
      await fetchData();
    } catch (error) {
      console.error("Failed to add friend", error);
    }
  };

  const handleRemoveFriend = async (friendId) => {
    try {
      await removeFriend(currentUser.userId, friendId);
      await fetchData();
    } catch (error) {
      console.error("Failed to remove friend", error);
    }
  };

  if (loading) {
    return <div>Loading friends...</div>;
  }

  return (
    <div>
      <div className="flex-between" style={{ marginBottom: '2rem' }}>
        <div>
          <h1>Friends</h1>
          <p className="text-secondary">Manage your network</p>
        </div>
        <div className="glass-panel" style={{ padding: '0.75rem 1.5rem', display: 'flex', alignItems: 'center', gap: '1rem' }}>
          <Users className="text-accent-primary" />
          <span style={{ fontWeight: 600 }}>{friends.length} Friends</span>
        </div>
      </div>

      <div className="grid-cards">
        <div className="glass-panel">
          <h3 style={{ marginBottom: '1.5rem' }}>Add New Friend</h3>
          <form onSubmit={handleAddFriend} style={{ display: 'flex', gap: '1rem', alignItems: 'flex-end' }}>
            <div className="input-group" style={{ flex: 1, marginBottom: 0 }}>
              <label className="input-label">Select User</label>
              <select 
                className="input-field" 
                value={selectedUserId}
                onChange={(e) => setSelectedUserId(e.target.value)}
              >
                {allUsers.length === 0 && <option value="">No available users</option>}
                {allUsers.map(u => (
                  <option key={u.userId} value={u.userId}>{u.username}</option>
                ))}
              </select>
            </div>
            <button type="submit" className="btn btn-primary" disabled={allUsers.length === 0}>
              <UserPlus size={18} />
              Add
            </button>
          </form>
        </div>

        <div className="glass-panel" style={{ gridColumn: 'span 2' }}>
          <h3 style={{ marginBottom: '1.5rem' }}>Your Friends</h3>
          {friends.length === 0 ? (
            <p className="text-secondary">You don't have any friends yet.</p>
          ) : (
            <div style={{ display: 'flex', flexDirection: 'column', gap: '1rem' }}>
              {friends.map(friend => (
                <div key={friend.userId} className="transaction-item glass-panel" style={{ padding: '1rem' }}>
                  <div className="transaction-info">
                    <h4>{friend.username}</h4>
                    <p>{friend.email}</p>
                  </div>
                  <button 
                    className="btn btn-danger" 
                    onClick={() => handleRemoveFriend(friend.userId)}
                  >
                    <UserMinus size={18} />
                    Remove
                  </button>
                </div>
              ))}
            </div>
          )}
        </div>
      </div>
    </div>
  );
};

export default FriendsList;
