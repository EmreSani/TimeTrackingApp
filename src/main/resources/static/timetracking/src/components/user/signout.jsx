import React, { useEffect } from 'react';
import { useNavigate } from 'react-router-dom';

const SignOut = () => {
  const navigate = useNavigate();

  useEffect(() => {
    
    const signOutUser = () => {
      localStorage.removeItem('token');
      navigate('/'); 
    };

    signOutUser();
  }, [navigate]);

  return (
    <div className="signout-container">
      <h2>Signing Out...</h2>
    </div>
  );
};

export default SignOut;
