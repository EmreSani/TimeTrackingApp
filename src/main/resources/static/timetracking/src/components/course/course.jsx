import React from 'react';
import { Link } from 'react-router-dom';

const Course = () => {
  return (
    <div>
      <h2>Course Management</h2>
      <ul>
        <li><Link to="/course/create">Create Course</Link></li>
        <li><Link to="/course/get">Get Course</Link></li>
        <li><Link to="/course/getAll">Get All Courses</Link></li>
        <li><Link to="/course/update">Update Course</Link></li>
        <li><Link to="/course/delete">Delete Course</Link></li>
      </ul>
    </div>
  );
};

export default Course;
