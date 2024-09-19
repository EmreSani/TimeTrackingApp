import { useContext } from 'react';
import { AuthContext } from './AuthContext'; // AuthContext'i içe aktarın

export const useAuth = () => {
  const { token } = useContext(AuthContext); // AuthContext'ten token alın
  return { token };
};
