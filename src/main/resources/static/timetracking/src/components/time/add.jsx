import React, { useState, useEffect } from "react";
import "../../styles/signup.css";
import { useNavigate } from 'react-router-dom';

const AddTimeEntry = () => {
  const [error, setError] = useState("");
  const navigate = useNavigate();
  const [courses, setCourses] = useState([]);
  const [selectedCourseId, setSelectedCourseId] = useState("");
  const [formData, setFormData] = useState({
    startDateTime: "",
    endDateTime: "",
  });

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

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    // Eğer value'nun formatı "yyyy-MM-ddTHH:mm" şeklindeyse, sonuna ":00" ekleyelim
    if (name === "startDateTime" || name === "endDateTime") {
      setFormData((prevData) => ({ 
        ...prevData, 
        [name]: value.endsWith(":00") ? value : `${value}:00` 
      }));
    } else {
      setFormData((prevData) => ({ ...prevData, [name]: value }));
    }
  };
  

  const handleSelectChange = (e) => {
    setSelectedCourseId(e.target.value);
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
      const response = await fetch("http://localhost:8080/timeEntry/save", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
          "Authorization": `Bearer ${token}`,
        },
        body: JSON.stringify({
          courseId: selectedCourseId,
          startDateTime: formData.startDateTime,
          endDateTime: formData.endDateTime,
        }),
      });

      if (response.ok) {
        alert("Time entry added successfully!");
        setFormData({ startDateTime: "", endDateTime: "" });
        setSelectedCourseId("");
        // İsterseniz buraya yönlendirme ekleyebilirsiniz
      } else {
        const errorData = await response.json();
        setError(`Error adding time entry: ${errorData.message || 'Unknown error'}`);
      }
    } catch (error) {
      setError("Network error: " + error.message);
    }
  };

  return (
    <div className="signup-card">
      <h2>Add Time Entry</h2>
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
          type="datetime-local"
          name="startDateTime"
          placeholder="Start DateTime"
          value={formData.startDateTime}
          onChange={handleInputChange}
          required
        />
        <input
          type="datetime-local"
          name="endDateTime"
          placeholder="End DateTime"
          value={formData.endDateTime}
          onChange={handleInputChange}
          required
        />
        <button className="button-reg" type="submit">Add Time Entry</button>
      </form>

      <button className="button-reg" onClick={() => navigate("/home/weekly")}>
        X
      </button>
    </div>
  );
};

export default AddTimeEntry;
