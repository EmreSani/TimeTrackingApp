import React, { useState, useEffect } from "react";
import "../../styles/signup.css"; // CSS'yi burada belirteceğiz
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
    password: "",
    email: "",
  });

  useEffect(() => {
    const fetchUserData = async () => {
      const token = localStorage.getItem('token'); // LocalStorage'dan token alınıyor
  
      if (!token) {
        setError("No token found, please log in again.");
        return;
      }
  
      try {
        const response = await fetch("http://localhost:8080/user/userAuth", {
          method: "GET",
          headers: {
            "Content-Type": "application/json",
            "Authorization": `Bearer ${token}`, // Bearer token başlığını ekle
          },
        });
        
        if (response.ok) {
          const data = await response.json();
  
          // Gelen veriler iç içe 'object' anahtarında olduğu için ona erişiyoruz
          const userData = data.object;
  
          // Gelen verileri form alanlarına yerleştir
          setFormData({
            username: userData.username || "",
            firstName: userData.firstName || "",
            lastName: userData.lastName || "",
            ssn: userData.ssn || "",
            phoneNumber: userData.phone || "",
            password: "", // Şifreyi boş bırakmak güvenlik açısından önemli
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
    setError(""); // Hata mesajını sıfırla
  
    try {
      const response = await fetch("http://localhost:8080/user/updateUser", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify(formData),
      });
  
      if (response.ok) {
        console.log("User updated successfully!");
        setTimeout(() => {
          navigate("/");
        }, 2000);
      } else {
        const errorData = await response.json();
        setError(`Error updating user: ${errorData.message || 'Unknown error'}`);
      }
    } catch (error) {
      setError("Network error: " + error.message);
    }
  };

  return (
    <div className="signup-card">
      <h2>Update User</h2>
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
          name="password"
          placeholder="Password"
          value={formData.password}
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
