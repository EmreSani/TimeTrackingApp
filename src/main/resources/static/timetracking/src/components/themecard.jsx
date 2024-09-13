import React from 'react';
import '../App.css'; 

const ThemeCard = ({ title, currentHours, previousHours, backgroundColor, iconSrc, cardClass }) => {
  return (
    <article className={`theme-card ${cardClass}`}>
      <h2>{title}</h2>
      <div className="icon-box border-top-radius" style={{ backgroundColor }}>
        <img className="logo-card" src={iconSrc} alt="" height="35px" />
      </div>
      <img
        className={`icon-ellipsis card-${cardClass}`}
        src="images/icon-ellipsis.svg"
        alt=""
      />
      <div className="data-card border-radius">
        <p className="card-title">{title}</p>
        <div className="card-info">
          <p className="para-current">{currentHours}hrs</p>
          <span className="span-previous">Last Week - {previousHours}hrs</span>
        </div>
      </div>
      <div className={`theme-card-info card-${cardClass} border-bot-radius hidden`}>
        <ul className="theme-card-list">
          <li>Rename</li>
          <li>Save as PDF</li>
          <li>Delete</li>
        </ul>
      </div>
    </article>
  );
};

export default ThemeCard;
