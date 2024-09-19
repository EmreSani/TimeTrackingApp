import React from 'react';
import { Link } from 'react-router-dom';

const User = () => {
  return (
    <div>
      <h2>User Management</h2>
      <ul>
        <li><Link to="/user/signin">Sign In</Link></li>
        <li><Link to="/user/signup">Sign Up</Link></li>
        <li><Link to="/user/update">Update User</Link></li>
      </ul>
    </div>
  );
};

export default User;
