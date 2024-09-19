import React from 'react';
import { useLocation } from 'react-router-dom';
import { useNavigate } from 'react-router-dom';
import '../styles/App.css';

const ProfileCard = () => {
  const location = useLocation();
  const username = sessionStorage.getItem('username');
  //const username = location.state?.username; 
  const navigate = useNavigate();

  const handleWeeklyClick = () => {
    navigate('/home/weekly');
  };
  const handleDailyClick = () => {
    navigate('/home/daily');
  };

  const handleMonthlyClick = () => {
    navigate('/home/monthly');
  };

  return (
    <article className="profile-card">
      <h2>Profile</h2>
      <div className="user-report border-radius">
        <img
          className="user-img"
          src="../images/image-dev02.png"
          alt="user_avatar"
        />
        <div>
          <span>Merhaba</span>
          <p>{username}</p>
          <span></span>
        </div>
      </div>
      <div className="time-selection border-bot-radius height">
      <button className="weekly-para active" type="button" onClick={handleDailyClick}>
      Daily
    </button>
        <button className="weekly-para active" type="button" onClick={handleWeeklyClick}>
        Weekly
      </button>
      <button className="weekly-para active" type="button" onClick={handleMonthlyClick}>
      Monthly
    </button>
      </div>
    </article>
  );
};

export default ProfileCard;
