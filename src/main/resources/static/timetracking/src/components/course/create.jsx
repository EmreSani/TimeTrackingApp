import React, { useState } from "react";
import "../../styles/signup.css"; // CSS dosyasını burada belirteceğiz
import { useNavigate } from 'react-router-dom';

const CourseCreateCard = ({ onClose }) => {
  const [error, setError] = useState("");
  const navigate = useNavigate();
  const [formData, setFormData] = useState({
    courseName: "",
    description: "",
    
  });

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setFormData((prevData) => ({ ...prevData, [name]: value }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    try {
      const response = await fetch("http://localhost:8080/course/save", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${localStorage.getItem("token")}`, // Eğer JWT token kullanıyorsanız
        },
        body: JSON.stringify(formData),
      });

      if (response.ok) {
        console.log("Course created successfully!");
        alert("Course created successfully!");
        setFormData({
          courseName: "",
          description: "",
          
        });
        setTimeout(() => {
          navigate("/home/weekly");
        }, 1000);
      } else {
        console.error("Error creating course.");
        const errorData = await response.json();
        setError(`Error registering user: ${errorData.message || 'Unknown error'}`);
      }
    } catch (error) {
      setError("Network error: " + error.message);
      console.error("Network error:", error);
    }
  };

  return (
    <div className="signup-card">
    <p>Course Register</p>
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
          <button className="button-reg" type="submit">Course Register</button>        
        </form>
        <button className="button-reg" onClick={() => navigate("/home/weekly")}>
          X
        </button>
      </div>
    
  );
};

export default CourseCreateCard;
