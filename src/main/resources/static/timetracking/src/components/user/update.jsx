import React, { useState, useEffect } from "react";
import "../../styles/signup.css"; 
import { useNavigate } from 'react-router-dom';

const UpdateUser = () => {
  const [error, setError] = useState("");
  const navigate = useNavigate();
  const [formData, setFormData] = useState({
    username: "",
    firstName: "",
    lastName: "",
    ssn: "",
    phoneNumber: "",
    currentPassword: "",
    newPassword: "",
    email: "",
  });

  useEffect(() => {
    const fetchUserData = async () => {
      const token = localStorage.getItem('token'); 
  
      if (!token) {
        setError("No token found, please log in again.");
        return;
      }
      
  
      try {
        const response = await fetch("http://localhost:8080/user/userAuth", {
          method: "GET",
          headers: {
            "Content-Type": "application/json",
            "Authorization": `Bearer ${token}`,
          },
        });
        
        if (response.ok) {
          const data = await response.json();
  
          
          const userData = data.object;
  
          
          setFormData({
            username: userData.username || "",
            firstName: userData.firstName || "",
            lastName: userData.lastName || "",
            ssn: userData.ssn || "",
            phoneNumber: userData.phone || "",
            currentPassword: userData.currentPassword || "",  
            newPassword: userData.newPassword || "",  
            email: userData.email || "",
          });
        } else {
          const errorData = await response.json();
          setError(`Error fetching user data: ${errorData.message || 'Unknown error'}`);
        }
      } catch (error) {
        setError("Network error: " + error.message);
        console.error("Network Error:", error);
      }
    };
  
    fetchUserData();
  }, []);
  
  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setFormData((prevData) => ({ ...prevData, [name]: value }));
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
      const response = await fetch("http://localhost:8080/user/updateUser", {
        method: "PATCH",
        headers: {
          "Content-Type": "application/json",
          "Authorization": `Bearer ${token}`
        },
        body: JSON.stringify(formData),
      });
  
      if (response.ok) {
        console.log("User updated successfully!");
        alert("User updated successfully!");
        setTimeout(() => {
          navigate("/home/weekly");
        }, 2000);
      } else {
        const errorData = await response.json();
        
        // Hataları ayıklayıp göstermek için
        if (errorData instanceof Object) {
          setError(Object.values(errorData).join(", "));
        } else {
          setError(`Error updating user: ${errorData.message || 'Unknown error'}`);
        }
      }
    } catch (error) {
      setError("Network error: " + error.message);
    }
};


  return (
    <div className="signup-card">
      <h1>Update User</h1>
      {error && <p style={{ color: "red" }}>{error}</p>} {/* Hata mesajı */}
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
        />
        <input
          type="text"
          name="phoneNumber"
          placeholder="Phone Number->555-555-5555"
          value={formData.phoneNumber}
          onChange={handleInputChange}
        />
        <input
          type="password"
          name="currentPassword"
          placeholder="Current Password"
          value={formData.currentPassword}
          onChange={handleInputChange}
        />
        
        <input
          type="password"
          name="newPassword"
          placeholder="New Password"
          value={formData.newPassword}
          onChange={handleInputChange}
        />
        <input
          type="email"
          name="email"
          placeholder="Email"
          value={formData.email}
          onChange={handleInputChange}
          required
        />
        
        <button className="button-reg" type="submit">Update</button>        
      </form>

      <button className="button-reg" onClick={() => navigate("/home/weekly")}>
        X
      </button>
    </div>
  );
};

export default UpdateUser;
