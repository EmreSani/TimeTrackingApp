import React, { useEffect, useState } from 'react';
import ProfileCard from '../profilecard';
import ThemeCard from '../themecard';
import { fetchCourses } from '../../api/fetchCourse'; // API işlevini içe aktarın
import { fetchPreviousMonthTimeEntries } from '../../api/fetchpreviousmonthtime'; // API işlevini içe aktarın
import { useAuth } from '../../api/useAuth'; // Kullanıcı doğrulama hook'u, auth token'ını almak için
import { getRandomColor } from '../../api/getrandomcolor';

const Monthly = () => {
  const [courses, setCourses] = useState([]);
  const [previousMonthEntries, setPreviousMonthEntries] = useState([]);
  const [error, setError] = useState(null);
  const { token } = useAuth(); // Hook'tan token alın
  const color = getRandomColor(); // Arka plan rengi
  
  

  useEffect(() => {
    const getCourses = async () => {
      try {
        if (token) {
          const url = 'http://localhost:8080/course/getAllUsersCourses'; // Burada uygun API_URL'yi belirleyin
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

    const getPreviousMonthEntries = async () => {
      try {
        if (token) {
          const urlPrev = 'http://localhost:8080/timeEntry/getAllPreviousMonthTimeEntriesByUser'; // Burada uygun API_URL'yi belirleyin
          const data = await fetchPreviousMonthTimeEntries(token, urlPrev);
          
          if (Array.isArray(data)) {
            setPreviousMonthEntries(data);
          } else {
            setError('Unexpected data format received for previous month time entries');
          }
        } else {
          setError('No token available');
        }
      } catch (error) {
        setError('Failed to load previous month time entries');
      }
    };

    getCourses();
    getPreviousMonthEntries();
  }, [token]);

  // `previousMonthEntries` ile `courses`'ı ilişkilendirin
  const getPreviousHours = (courseId) => {
    const courseEntries = previousMonthEntries.filter(entry => entry.courseId === courseId);
    return courseEntries.reduce((total, entry) => total + (entry.totalMinutesWorked / 60), 0); // Toplam saat
  };

  return (
    <div className="wrapper">
      <ProfileCard />
      <div className="theme-card-container">
        {error && <p style={{ color: 'red' }}>{error}</p>}
        {courses.length > 0 ? (
          courses.map(course => (
            <ThemeCard
              key={course.id} // courseId'yi kullanarak kartları ayırt edebilirsiniz
              title={course.courseName}
              currentHours={course.timeEntry?.reduce((total, entry) => total + (entry.monthlyHours || 0), 0) || 0}
              previousHours={getPreviousHours(course.id)} // Önceki hafta saatler
              backgroundColor={color}
              iconSrc="images/icon-placeholder.svg" // İkonun gerçek yolunu belirleyin
              cardClass="default" // Kart sınıfını uygun şekilde ayarlayın
              state="Last Month"
            />
          ))
        ) : (
          <p>No courses available</p>
        )}
      </div>
    </div>
  );
};

export default Monthly;
