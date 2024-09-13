import React from 'react';
import '../App.css';

const ProfileCard = () => {
  return (
    <article className="profile-card">
      <h2>Profile</h2>
      <div className="user-report border-radius">
        <img
          className="user-img"
          src="images/image-dev02.png"
          alt="user_avatar"
        />
        <div>
          <span>Dev02 Team</span>
          <p>Time Tracking Application</p>
        </div>
      </div>
      <div className="time-selection border-bot-radius height">
        <button className="daily-para" type="button">Daily</button>
        <button className="weekly-para active" type="button">Weekly</button>
        <button className="monthly-para" type="button">Monthly</button>
      </div>
    </article>
  );
};

export default ProfileCard;
