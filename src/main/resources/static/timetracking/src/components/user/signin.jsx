import React, { useState } from 'react';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';
import '../../styles/loginstyle.css'; // CSS dosyasını dahil ediyoruz

const Signin = () => {
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState('');
  const [message, setMessage] = useState('');

  const navigate = useNavigate();

  const handleSubmit = async (e) => {
    e.preventDefault();

    const loginData = {
      username,
      password,
    };

    try {
      const response = await axios.post('http://localhost:8080/user/login', loginData);

      if (response.data && response.data.token) {
        localStorage.setItem('token', response.data.token); // Token'ı localStorage'a kaydedin
        sessionStorage.setItem('username', username);
        setMessage('Login Successful');
        navigate('/home/weekly');
      } else {
        setError('Login failed. No token received.');
      }
    } catch (error) {
      setError('Login failed. Please try again.');
    }
  };

  return (
    <div className="container">
      <div className="card">
        <h1>Sign In Page</h1>
        {error && <p style={{ color: 'red' }}>{error}</p>}
        {message && <p style={{ color: 'green' }}>{message}</p>}
        <form onSubmit={handleSubmit}>
          <div className="input-group">
            <label>Username: </label>
            <input 
              type="text" 
              value={username} 
              onChange={(e) => setUsername(e.target.value)} 
              required 
            />
          </div>
          <div className="input-group">
            <label>Password: </label>
            <input 
              type="password" 
              value={password} 
              onChange={(e) => setPassword(e.target.value)} 
              required 
            />
          </div>
          <button type="submit" className='signin_btn'>Sign In</button>
        </form>
      </div>
    </div>
  );
};

export default Signin;
