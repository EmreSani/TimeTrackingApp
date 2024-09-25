import React, { useEffect, useState } from 'react';
import ProfileCard from '../profilecard';
import ThemeCard from '../themecard';
import { fetchCourses } from '../../api/fetchCourse'; 
import { fetchPreviousDayTimeEntries } from '../../api/fetchpreviousdaytime'; 
import { useAuth } from '../../api/useAuth'; 
import { getRandomColor } from '../../api/getrandomcolor';

const Daily = () => {
  const [courses, setCourses] = useState([]);
  const [previousDayEntries, setPreviousDayEntries] = useState([]);
  const [error, setError] = useState(null);
  const { token } = useAuth(); 
  const color = getRandomColor(); 

  useEffect(() => {
    const getCourses = async () => {
      try {
        if (token) {
          const url = 'http://localhost:8080/course/getAllUsersCourses'; 
          const data = await fetchCourses(token, url);
          
          if (Array.isArray(data)) {
            setCourses(data);
          } else {
            setError('Unexpected data format received for courses');
          }
        } else {
          setError('No token available');
        }
      } catch (error) {
        setError('Failed to load courses');
      }
    };

    const getPreviousDayEntries = async () => {
      try {
        if (token) {
          const urlPrev = 'http://localhost:8080/timeEntry/getAllPreviousDayTimeEntriesByUser'; 
          const data = await fetchPreviousDayTimeEntries(token, urlPrev);
          
          if (Array.isArray(data)) {
            setPreviousDayEntries(data);
          } else {
            setError('Unexpected data format received for previous day time entries');
          }
        } else {
          setError('No token available');
        }
      } catch (error) {
        setError('Failed to load previous day time entries');
      }
    };

    getCourses();
    getPreviousDayEntries();
  }, [token]);

  
  const getPreviousHours = (courseId) => {
    const courseEntries = previousDayEntries.filter(entry => entry.courseId === courseId);
    return courseEntries.reduce((total, entry) => total + (entry.totalMinutesWorked / 60), 0); 
  };

  return (
    <div className="wrapper">
      <ProfileCard />
      <div className="theme-card-container">
        {error && <p style={{ color: 'red' }}>{error}</p>}
        {courses.length > 0 ? (
          courses.map(course => (
            <ThemeCard
              key={course.id} 
              title={course.courseName}
              currentHours={course.timeEntry?.reduce((total, entry) => total + (entry.dailyHours || 0), 0) || 0}
              previousHours={getPreviousHours(course.id).toFixed(2)} 
              backgroundColor={color}
              iconSrc="images/icon-placeholder.svg" 
              cardClass="default" 
              state="Previous Day"
            />
          ))
        ) : (
          <p>No courses available</p>
        )}
      </div>
    </div>
  );
};

export default Daily;
