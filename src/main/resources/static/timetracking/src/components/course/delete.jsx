import React, { useState, useEffect } from "react";
import "../../styles/signup.css";
import { useNavigate } from 'react-router-dom';

const UpdateCourse = () => {
  const [error, setError] = useState("");
  const navigate = useNavigate();
  const [formData, setFormData] = useState({
    courseName: "",
    description: "",
  });
  const [courses, setCourses] = useState([]);
  const [selectedCourseId, setSelectedCourseId] = useState("");

  useEffect(() => {
    const fetchCourses = async () => {
      const token = localStorage.getItem('token');
      if (!token) {
        setError("No token found, please log in again.");
        return;
      }

      try {
        const response = await fetch("http://localhost:8080/course/getAllUsersCourses", {
          method: "GET",
          headers: {
            "Content-Type": "application/json",
            "Authorization": `Bearer ${token}`,
          },
        });

        if (response.ok) {
          const data = await response.json();
          console.log(data); // Yanıtı konsola yazdır
          setCourses(data.object || []); // Gelen kurs verilerini state'e kaydedin
        } else {
          const errorData = await response.json();
          setError(`Error fetching courses: ${errorData.message || 'Unknown error'}`);
        }
      } catch (error) {
        setError("Network error: " + error.message);
      }
    };

    fetchCourses();
  }, []);

  useEffect(() => {
    const fetchCourseData = async () => {
      if (!selectedCourseId) return; // Eğer bir kurs seçilmediyse veri çekme

      const token = localStorage.getItem('token');
      if (!token) {
        setError("No token found, please log in again.");
        return;
      }

      try {
        const response = await fetch(`http://localhost:8080/course/${selectedCourseId}`, {
          method: "GET",
          headers: {
            "Content-Type": "application/json",
            "Authorization": `Bearer ${token}`,
          },
        });

        if (response.ok) {
          const data = await response.json();
          const courseData = data.object || data;
          setFormData({
            courseName: courseData.courseName || "",
            description: courseData.description || "",
          });
        } else {
          const errorData = await response.json();
          setError(`Error fetching course data: ${errorData.message || 'Unknown error'}`);
        }
      } catch (error) {
        setError("Network error: " + error.message);
      }
    };

    fetchCourseData();
  }, [selectedCourseId]); 

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setFormData((prevData) => ({ ...prevData, [name]: value }));
  };

  const handleSelectChange = (e) => {
    setSelectedCourseId(e.target.value); 
    setFormData({ courseName: "", description: "" }); 
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError("");

    const token = localStorage.getItem('token');
    if (!token) {
      setError("No token found, please log in again.");
      return;
    }

    try {
      const response = await fetch(`http://localhost:8080/course/updateCourse/${selectedCourseId}`, {
        method: "PUT",
        headers: {
          "Content-Type": "application/json",
          "Authorization": `Bearer ${token}`,
        },
        body: JSON.stringify(formData),
      });

      if (response.ok) {
        console.log("Course updated successfully!");
        alert("Course updated successfully!");
        setTimeout(() => {
          navigate("/home/weekly");
        }, 2000);
      } else {
        const errorData = await response.json();
        setError(`Error updating course: ${errorData.message || 'Unknown error'}`);
      }
    } catch (error) {
      setError("Network error: " + error.message);
    }
  };

  const handleDelete = async () => {
    if (window.confirm("Are you sure you want to delete this course?")) {
      const token = localStorage.getItem('token');
      if (!token) {
        setError("No token found, please log in again.");
        return;
      }

      try {
        const response = await fetch(`http://localhost:8080/course/${selectedCourseId}`, {
          method: "DELETE",
          headers: {
            "Content-Type": "application/json",
            "Authorization": `Bearer ${token}`,
          },
        });

        if (response.ok) {
          alert("Course deleted successfully!");
          setCourses(courses.filter(course => course.id !== selectedCourseId)); // Silinen kursu listeden çıkar
          setSelectedCourseId(""); 
          setFormData({ courseName: "", description: "" }); 
        } else {
          const errorData = await response.json();
          setError(`Error deleting course: ${errorData.message || 'Unknown error'}`);
        }
      } catch (error) {
        setError("Network error: " + error.message);
      }
    }
  };

  return (
    <div className="signup-card">
      <h1>Delete Course</h1>
      {error && <p style={{ color: "red" }}>{error}</p>}
      
      <select onChange={handleSelectChange} value={selectedCourseId}>
        <option value="">Select a Course</option>
        {courses.map((course) => (
          <option key={course.id} value={course.id}>
            {course.courseName}
          </option>
        ))}
      </select>

      <form onSubmit={handleDelete}>
        
        <button className="button-reg" type="submit">Delete Course</button>
      </form>

      <button className="button-reg" onClick={() => navigate("/home/weekly")}>
        X
      </button>
    </div>
  );
};

export default UpdateCourse;
