import React from 'react';
import '../styles/App.css'; 
import { getRandomColor} from '../api/getrandomcolor';

const ThemeCard = ({ title, currentHours, previousHours, iconSrc, cardClass,state }) => {
  const backgroundColor=getRandomColor();  
  


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
          <p className="para-current">{currentHours} hrs</p>
          <span className="span-previous">{state} - {previousHours} hrs</span>
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
