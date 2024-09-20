import React, { useState } from 'react';
import { Link } from 'react-router-dom';
import '../styles/App.css'; // CSS dosyanızı dahil edin.

const Header = () => {
  const [isUserMenuOpen, setUserMenuOpen] = useState(false);
  const [isCourseMenuOpen, setCourseMenuOpen] = useState(false);
  const [isTimeMenuOpen, setTimeMenuOpen] = useState(false);

  const toggleUserMenu = () => setUserMenuOpen(!isUserMenuOpen);
  const toggleCourseMenu = () => setCourseMenuOpen(!isCourseMenuOpen);
  const toggleTimeMenu = () => setTimeMenuOpen(!isTimeMenuOpen);

  return (
    <header className='header'>
      <h1>Time Tracking Application</h1>
      <nav>
        <ul>
          <li>
            <button onClick={toggleUserMenu}>User</button>
            {isUserMenuOpen && (
              <ul>
                <li><Link to="/user/update">Update User</Link></li>
              </ul>
            )}
          </li>
          <li>
            <button onClick={toggleCourseMenu}>Course</button>
            {isCourseMenuOpen && (
              <ul>
                <li><Link to="/course/create">Create Course</Link></li>
                <li><Link to="/course/update">Update Course</Link></li>
                <li><Link to="/course/delete">Delete Course</Link></li>
              </ul>
            )}
          </li>
          <li>
            <button onClick={toggleTimeMenu}>Time</button>
            {isTimeMenuOpen && (
              <ul>
                <li><Link to="/time/add">Add Net Time</Link></li>
                <li><Link to="/time/daily">Get All Daily Time by User</Link></li>
                {/* Daha fazla Time bağlantısı burada */} 
              </ul>
            )}
          </li>
        </ul>
      </nav>
    </header>
  );
};

export default Header;
