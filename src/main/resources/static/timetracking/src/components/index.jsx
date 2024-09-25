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
import Signup from './user/signup';
import UpdateUser from './user/update';
import CreateCourse from './course/create';
import UpdateCourse from './course/update';
import DeleteCourse from './course/delete';
import AddTime from './time/add';
import DeleteTime from './time/delete';
import GetAllTime from './time/getalltime';
import SignOut from './user/signout';
import UpdateTime from './time/update';



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
        <Route path="/user/signup" element={<Signup />} />
        <Route path="/user/update" element={<UpdateUser />} />
        <Route path="/course/create" element={<CreateCourse />} />
        <Route path="/course/update" element={<UpdateCourse />} />
        <Route path="/course/delete" element={<DeleteCourse />} />
        <Route path="/time/add" element={<AddTime />} />
        <Route path="/time/delete" element={<DeleteTime />} />
        <Route path="/time/getalltime" element={<GetAllTime />} />
        <Route path="/user/signout" element={<SignOut />} />
        <Route path="/time/update" element={<UpdateTime />} />
        

        
        
      </Routes>
    </div>
  );
};

export default Index;
