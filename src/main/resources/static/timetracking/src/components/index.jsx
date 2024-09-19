import React from 'react';
import { Routes, Route } from 'react-router-dom';

// Bileşenler
import Home from './home/home';
import Home2 from './home/home2';
import User from './user/user';
import Course from './course/course';
import Time from './time/time';
import Weekly from './home/weekly';
import Daily from './home/daily';
import Monthly from './home/monthly';


const Index = () => {
  return (
    <div className="index-content">
      <Routes>
        <Route path="/" element={<Home />} />
        <Route path="/user/*" element={<User />} />
        <Route path="/course/*" element={<Course />} />
        <Route path="/time/*" element={<Time />} />
        <Route path="/home/home2" element={<Home2 />} />
        <Route path="/home/weekly" element={<Weekly />} />
        <Route path="/home/daily" element={<Daily />} />
        <Route path="/home/monthly" element={<Monthly />} />
        

        
        {/* Diğer yönlendirmeler buraya eklenebilir */}
      </Routes>
    </div>
  );
};

export default Index;
