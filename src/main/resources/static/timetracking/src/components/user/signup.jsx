import React, { useState } from "react";
import "../../styles/signup.css"; 
import { useNavigate } from 'react-router-dom';


const Signup = ({ onClose }) => {
  const [error, setError] = useState("");
  const navigate = useNavigate();
  const [formData, setFormData] = useState({
    username: "",
    firstName: "",
    lastName: "",
    ssn: "",
    phoneNumber: "",
    password: "",
    email: "",
  });

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setFormData((prevData) => ({ ...prevData, [name]: value }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError(""); 
  
    try {
      const response = await fetch("http://localhost:8080/user/register", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify(formData),
      });
  
      if (response.ok) {
        alert("User registered successfully!");
        console.log("User registered successfully!");
        
        setFormData({
          username: "",
          firstName: "",
          lastName: "",
          ssn: "",
          phoneNumber: "",
          password: "",
          email: "",
        });
        setTimeout(() => {
          navigate("/");
        }, 1000);
        
        
      } else {
        const errorData = await response.json();
        setError(`Error registering user: ${errorData.message || 'Unknown error'}`);
        console.error("Error registering user:", errorData);
      }
    } catch (error) {
      setError("Network error: " + error.message);
      console.error("Network error:", error);
    }
  };
  

  return (
      <div className="signup-card">
        <h2>Sign Up</h2>
        <form onSubmit={handleSubmit}>
          <input
            type="text"
            name="username"
            placeholder="Username"
            value={formData.username}
            onChange={handleInputChange}
            required
          />
          <input
            type="text"
            name="firstName"
            placeholder="First Name"
            value={formData.firstName}
            onChange={handleInputChange}
            required
          />
          <input
            type="text"
            name="lastName"
            placeholder="Last Name"
            value={formData.lastName}
            onChange={handleInputChange}
            required
          />
          <input
            type="text"
            name="ssn"
            placeholder="SSN"
            value={formData.ssn}
            onChange={handleInputChange}
            //required
          />
          <input
            type="text"
            name="phoneNumber"
            placeholder="Phone Number->555-555-5555"
            value={formData.phoneNumber}
            onChange={handleInputChange}
            //required
          />
          <input
            type="password"
            name="password"
            placeholder="Password"
            value={formData.password}
            onChange={handleInputChange}
            required
          />
          <input
            type="email"
            name="email"
            placeholder="Email"
            value={formData.email}
            onChange={handleInputChange}
            required
          />
          
          <button className="button-reg" type="submit">Register</button>        
        </form>
        <button className="button-reg" onClick={() => navigate("/")}>
          X
        </button>
      </div>
      
    
  );
};

export default Signup;
