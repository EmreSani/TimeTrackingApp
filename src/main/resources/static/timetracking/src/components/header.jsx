import React, { useState } from 'react';
import { Link, useLocation } from 'react-router-dom';
import '../styles/App.css'; 

const Header = () => {
  const [isUserMenuOpen, setUserMenuOpen] = useState(false);
  const [isCourseMenuOpen, setCourseMenuOpen] = useState(false);
  const [isTimeMenuOpen, setTimeMenuOpen] = useState(false);

  const location = useLocation(); // Mevcut URL yolunu almak için useLocation kullanın

  const toggleUserMenu = () => setUserMenuOpen(!isUserMenuOpen);
  const toggleCourseMenu = () => setCourseMenuOpen(!isCourseMenuOpen);
  const toggleTimeMenu = () => setTimeMenuOpen(!isTimeMenuOpen);

  return (
    <header className='header'>
      <h1>Time Tracking Application</h1>
      {/* Eğer sayfa "/login" ya da "/register" değilse navbarı göster */}
      {location.pathname !== '/user/signin' && location.pathname !== '/' && location.pathname !== '/user/signup' && (
        <nav>
          <ul>
            <li>
              <button onClick={toggleUserMenu}>User</button>
              {isUserMenuOpen && (
                <ul>
                  <li><Link to="/user/update">Update User</Link></li>
                  <li><Link to="/user/signout">Sign Out</Link></li>
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
                  <li><Link to="/time/add">Add Time</Link></li>
                  <li><Link to="/time/update">Update Time</Link></li>
                  <li><Link to="/time/getalltime">Get All Daily Time</Link></li>
                </ul>
              )}
            </li>
          </ul>
        </nav>
      )}
    </header>
  );
};

export default Header;
