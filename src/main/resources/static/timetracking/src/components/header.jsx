import React from 'react';
import { Link } from 'react-router-dom';
import '../styles/App.css'; // CSS dosyanızı dahil edin.

const Header = () => {
  return (
    <header className='header'>
      {/* Navigation Bar */}
      <h1>Time Tracking Application</h1>
      <nav>
        <ul>
          <li>
            <Link to="/user">User</Link>
            <ul>
              <li><Link to="/user/signin">Sign In</Link></li>
              <li><Link to="/user/signup">Sign Up</Link></li>
              <li><Link to="/user/update">Update User</Link></li>
            </ul>
          </li>
          <li>
            <Link to="/course">Course</Link>
            <ul>
              <li><Link to="/course/create">Create Course</Link></li>
              <li><Link to="/course/get">Get Course</Link></li>
              <li><Link to="/course/getAll">Get All Courses</Link></li>
              <li><Link to="/course/update">Update Course</Link></li>
              <li><Link to="/course/delete">Delete Course</Link></li>
            </ul>
          </li>
          <li>
            <Link to="/time">Time</Link>
            <ul>
              <li><Link to="/time/add">Add Net Time</Link></li>
              <li><Link to="/time/daily">Get All Daily Time by User</Link></li>
              {/* Daha fazla Time bağlantısı burada */}
            </ul>
          </li>
        </ul>
      </nav>
    </header>
  );
};

export default Header;
