import React from 'react';
import { Link } from 'react-router-dom';

const Time = () => {
  return (
    <div>
      <h2>Time Tracking</h2>
      <ul>
        <li><Link to="/time/add">Add Net Time</Link></li>
        <li><Link to="/time/daily">Get All Daily Time by User</Link></li>
        <li><Link to="/time/weekly">Get All Weekly Time by User</Link></li>
        <li><Link to="/time/monthly">Get All Monthly Time by User</Link></li>
        <li><Link to="/time/previousDay">Get All Previous Day</Link></li>
        <li><Link to="/time/previousWeek">Get All Previous Week</Link></li>
        <li><Link to="/time/previousMonth">Get All Previous Month</Link></li>
        <li><Link to="/time/all">Get All Time</Link></li>
        <li><Link to="/time/update">Update Time</Link></li>
        <li><Link to="/time/delete">Delete Time</Link></li>
      </ul>
    </div>
  );
};

export default Time;
