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
          setCourses(data.object || []); 
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
      if (!selectedCourseId) return; 

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

  return (
    <div className="signup-card">
      <h1>Update Course</h1>
      {error && <p style={{ color: "red" }}>{error}</p>}

      <select onChange={handleSelectChange} value={selectedCourseId}>
        <option value="">Select a Course</option>
        {courses.map((course) => (
          <option key={course.id} value={course.id}>
            {course.courseName}
          </option>
        ))}
      </select>

      <form onSubmit={handleSubmit}>
        <input
          type="text"
          name="courseName"
          placeholder="Course Name"
          value={formData.courseName}
          onChange={handleInputChange}
          required
        />
        <textarea
          name="description"
          placeholder="Description"
          value={formData.description}
          onChange={handleInputChange}
          required
        />
        <button className="button-reg" type="submit">Update Course</button>
      </form>

      <button className="button-reg" onClick={() => navigate("/home/weekly")}>
        Cancel
      </button>
    </div>
  );
};

export default UpdateCourse;
