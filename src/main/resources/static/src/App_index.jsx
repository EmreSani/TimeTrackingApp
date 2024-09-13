import React from 'react';
import {Header} from './components/header';
import ProfileCard from './components/profilecard';
import ThemeCard from './components/themecard';
import Footer from './components/footer';
import './App.css';


export const AppIndex = () => {
  return (
    
    
    
    
    <main className="wrapper">  
    <Header/>  
      <ProfileCard/>
      <div className="theme-card-container">
      <ThemeCard
        title="JAVA"
        currentHours="32"
        previousHours="36"
        backgroundColor="var(--soft-blue)"
        iconSrc="images/icon-java.svg"
        cardClass="work"
      />
      <ThemeCard
        title="Spring Boot"
        currentHours="10"
        previousHours="8"
        backgroundColor="var(--soft-blue)"
        iconSrc="images/icon-spring.svg"
        cardClass="play"
      />
      <ThemeCard
        title="React"
        currentHours="4"
        previousHours="7"
        backgroundColor="var(--study-red)"
        iconSrc="images/icon-react.svg"
        cardClass="study"
      />
      <ThemeCard
        title="SQL"
        currentHours="4"
        previousHours="5"
        backgroundColor="var(--green-exercise)"
        iconSrc="images/icon-sql.svg"
        cardClass="exercise"
      />
      <ThemeCard
        title="JavaScript"
        currentHours="2"
        previousHours="2"
        backgroundColor="var(--orange-self)"
        iconSrc="images/icon-js.svg"
        cardClass="self-care"
      />
      <ThemeCard
        title="Bootstrap"
        currentHours="5"
        previousHours="10"
        backgroundColor="var(--violet-social)"
        iconSrc="images/icon-bootstrap.svg"
        cardClass="social"
      />
      
      
      </div>     
      
      <Footer />
      
    </main>
    
  );
};

